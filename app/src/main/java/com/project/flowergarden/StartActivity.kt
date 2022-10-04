package com.project.flowergarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            Log.v("TAG","조인오너 버튼 눌림")
            val intent = Intent(this@StartActivity, JoinOwner::class.java)
            startActivity(intent)
        }
        registerUserButton.setOnClickListener {
            val intent = Intent(this@StartActivity, JoinUser::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val intent = Intent(this@StartActivity, Login::class.java)
            startActivity(intent)
        }
    }
}