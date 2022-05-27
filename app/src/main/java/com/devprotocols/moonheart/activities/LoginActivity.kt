package com.devprotocols.moonheart.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devprotocols.moonheart.databinding.ActivityLoginBinding
import com.devprotocols.moonheart.utils.BaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.kaopiz.kprogresshud.KProgressHUD

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: KProgressHUD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initClickListeners()
    }

    private fun initViews() {
        mAuth = FirebaseAuth.getInstance()
        dialog = BaseUtils.progressDialog(this)
    }

    private fun initClickListeners() {
        binding.txtRegister.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener{
            validateFields()
        }
        binding.txtForgot.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }

    private fun validateFields() {
       if (TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' })) {
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
        authenticateUser()
    }

    private fun authenticateUser() {
        mAuth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnCompleteListener{
                dialog.dismiss()
                when {
                    it.isSuccessful -> {
                        finish()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                    else -> {
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
    }
}