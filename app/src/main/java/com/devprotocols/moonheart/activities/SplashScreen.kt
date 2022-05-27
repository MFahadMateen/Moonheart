package com.devprotocols.moonheart.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.devprotocols.moonheart.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            startNewActivity()
        }, 3000)
    }

    private fun startNewActivity() {
        finish()
        if (mAuth.currentUser != null){
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        }else{
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
        }


    }
}