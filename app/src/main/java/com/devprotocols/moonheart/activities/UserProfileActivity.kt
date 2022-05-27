package com.devprotocols.moonheart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devprotocols.moonheart.R
import com.devprotocols.moonheart.databinding.ActivityUserProfileBinding
import com.devprotocols.moonheart.models.UserModel
import com.devprotocols.moonheart.utils.BaseUtils
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaopiz.kprogresshud.KProgressHUD

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private var userId: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: KProgressHUD
    var user = UserModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        initViews()
        initClickListeners()
        if (userId != null)
            getDataFromID()
    }

    private fun getDataFromID() {
        dialog.show()
        FirebaseDatabase.getInstance().getReference(Constants.users).child(userId!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user = snapshot.getValue(UserModel::class.java)!!
                        binding.txtName.text = user.name
                        binding.txtEmail.text = user.email
                        binding.txtAge.text = user.age
                        Glide.with(this@UserProfileActivity).load(user.image).placeholder(R.drawable.placeholder)
                            .into(binding.imgProfile)
                        dialog.dismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        dialog.dismiss()
                    }

                }
            )
    }

    private fun getIntentData() {
        userId = intent.getStringExtra("userId")
    }

    private fun initClickListeners() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("name", user.name)
            intent.putExtra("image", user.image)
            startActivity(intent)
        }
    }

    private fun initViews() {
        mAuth = FirebaseAuth.getInstance()
        dialog = BaseUtils.progressDialog(this)
    }
}