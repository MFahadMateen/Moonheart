package com.devprotocols.moonheart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.devprotocols.moonheart.databinding.ActivityForgotPasswordBinding
import com.devprotocols.moonheart.utils.BaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.kaopiz.kprogresshud.KProgressHUD

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var dialog: KProgressHUD
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.txtBack.setOnClickListener {
            finish()
        }
        binding.btnSend.setOnClickListener{
            validateEmail()
        }
    }

    private fun validateEmail() {
        if (TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' })) {
            binding.etEmail.error = "Email is required!"
            binding.etEmail.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
            binding.etEmail.error = "Invalid email!"
            binding.etEmail.requestFocus()
            return
        }
        dialog.show()
        sendEmail()
    }

    private fun sendEmail() {
        mAuth.sendPasswordResetEmail(binding.etEmail.text.toString())
            .addOnCompleteListener{
                dialog.dismiss()
            when{
                it.isSuccessful ->{
                    Toast.makeText(this, "Email Sent!", Toast.LENGTH_SHORT).show()
                    finish()
                }else ->{
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