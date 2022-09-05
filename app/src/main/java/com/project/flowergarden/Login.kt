package com.project.flowergarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.project.flowergarden.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    private lateinit var OwnerDB : DatabaseReference //실시간 데이터베이스
    private lateinit var UserDB : DatabaseReference //실시간 데이터베이스


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        loginInit()
    }
    private fun loginInit() = with(binding) {

        loginButton.setOnClickListener {
            val id = idEditTextView.text.toString()
            val pw = passwordEditTextView.text.toString()

            auth!!.signInWithEmailAndPassword(id, pw).addOnCompleteListener { Task ->
                if(Task.isSuccessful) {
                    Toast.makeText(this@Login, "로그인을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Login, MainActivityOwner::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}