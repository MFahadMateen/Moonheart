package com.devprotocols.moonheart.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devprotocols.moonheart.databinding.ActivityRegisterBinding
import com.devprotocols.moonheart.utils.BaseUtils
import com.devprotocols.moonheart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kaopiz.kprogresshud.KProgressHUD

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: KProgressHUD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initClickListeners()
    }

    private fun initViews() {
        dialog = BaseUtils.progressDialog(this)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initClickListeners() {
        binding.txtLogin.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        if (TextUtils.isEmpty(binding.etName.text.toString().trim { it <= ' ' })) {
            binding.etName.error = "Name is required!"
            binding.etName.requestFocus()
            return
        } else if (TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' })) {
            binding.etEmail.error = "Email is required!"
            binding.etEmail.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
            binding.etEmail.error = "Invalid email!"
            binding.etEmail.requestFocus()
            return
        } else if (TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' })) {
            binding.etPassword.error = "Password is required!"
            binding.etPassword.requestFocus()
            return
        } else if (binding.etPassword.text.toString().length < 8) {
            binding.etPassword.error = "Password length must be greater or equal to 6!"
            binding.etPassword.requestFocus()
            return
        }
        dialog.show()
        registerUser()
    }

    private fun registerUser() {
        mAuth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener {

                when {
                    it.isSuccessful -> {
                        saveUserToDatabase()
                    }
                    else -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun saveUserToDatabase() {
        val ref = FirebaseDatabase.getInstance().getReference(Constants.users).child(mAuth.uid.toString())
        val hashmap = HashMap<String, Any>()
        hashmap["name"] = binding.etName.text.toString()
        hashmap["email"] = binding.etEmail.text.toString()
        hashmap["userId"] = mAuth.uid.toString()
        ref.setValue(hashmap).addOnCompleteListener {
            dialog.dismiss()
            if (it.isSuccessful){
                finishAffinity()
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            }else{
                Toast.makeText(
                    this, "Couldn't save data!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}
