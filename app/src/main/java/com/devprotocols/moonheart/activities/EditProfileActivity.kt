package com.devprotocols.moonheart.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.databinding.ActivityEditProfileBinding
import com.devprotocols.moonheart.models.UserModel
import com.devprotocols.moonheart.utils.BaseUtils
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.kaopiz.kprogresshud.KProgressHUD


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var dialog: KProgressHUD
    private lateinit var mAuth: FirebaseAuth
    private var uri: Uri? = null
    private var isCover: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initClickListeners()
        getUserDetails()
    }

    private fun getUserDetails() {
        dialog.show()
        FirebaseDatabase.getInstance().getReference(Constants.users).child(mAuth.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    binding.etName.setText(user?.name)
                    binding.etEmail.setText(user?.email)
                    binding.etAge.setText(user?.age)
                    Glide.with(this@EditProfileActivity).load(user?.image)
                        .placeholder(R.drawable.placeholder).into(binding.imgProfile)
                    Glide.with(this@EditProfileActivity).load(user?.cover)
                        .placeholder(R.drawable.cover).into(binding.imgCover)
                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                }

            })
    }

    private fun initClickListeners() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnSave.setOnClickListener {
            updateData()
        }
        binding.imgEdit.setOnClickListener {
            chooseImage()
            isCover = false

        }
        binding.editCover.setOnClickListener {
            chooseImage()
            isCover = true
        }
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        resultLauncher.launch(intent)
    }

    private fun launchCameraIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        resultLauncher.launch(intent)
    }

    private fun chooseImage() {
        ImagePickerActivity.showImagePickerOptions(
            this,
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent()
                }
            })
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                uri = data!!.getParcelableExtra("path")
                Glide.with(this@EditProfileActivity).load(uri)
                    .into(if (isCover) binding.imgCover else binding.imgProfile)
                uploadImageToServer()
            }
        }

    private fun uploadImageToServer() {
        dialog.show()
        val ref = FirebaseStorage.getInstance()
            .getReference(if (isCover) Constants.cover else Constants.profile)
            .child(mAuth.uid.toString() + "." + BaseUtils.getFileExtension(uri!!, this))
        ref.putFile(uri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val hashMap = HashMap<String, Any>()
                if (isCover)
                    hashMap["cover"] = it.toString()
                else
                    hashMap["image"] = it.toString()
                FirebaseDatabase.getInstance().getReference(Constants.users)
                    .child(mAuth.uid.toString())
                    .updateChildren(hashMap).addOnSuccessListener {
                        dialog.dismiss()
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Image uploaded successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun updateData() {
        val hashMap = HashMap<String, Any>()
        hashMap["name"] = binding.etName.text.toString()
        hashMap["age"] = binding.etAge.text.toString()
        FirebaseDatabase.getInstance().getReference(Constants.users).child(mAuth.uid.toString())
            .updateChildren(hashMap).addOnCompleteListener {
                when {
                    it.isSuccessful -> {
                        finish()
                    }
                    else -> {
                        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun initViews() {
        mAuth = FirebaseAuth.getInstance()
        dialog = BaseUtils.progressDialog(this)
    }
}