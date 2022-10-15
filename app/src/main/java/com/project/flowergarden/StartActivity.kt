package com.project.flowergarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.flowergarden.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonClickEvent()
    }

    private fun buttonClickEvent() = with(binding) {
        registerOwnerButton.setOnClickListener {
            val intent = Intent(this@StartActivity, JoinOwner::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        registerUserButton.setOnClickListener {
            val intent = Intent(this@StartActivity, JoinUser::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        loginButton.setOnClickListener {
            val intent = Intent(this@StartActivity, Login::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}