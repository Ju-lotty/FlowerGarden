package com.project.flowergarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.flowergarden.databinding.ActivityJoinUserBinding
import com.project.flowergarden.entity.UserEntity

class JoinUser : AppCompatActivity() {

    private lateinit var binding: ActivityJoinUserBinding


    private var auth: FirebaseAuth? = null //파이어베이스 인증

    private lateinit var UserDB : DatabaseReference //실시간 데이터베이스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        JoinUserinit()
    }
    private fun JoinUserinit() = with(binding) {

        joinUserButton.setOnClickListener {
            val id = idEditTextView.text.toString()
            val pw = passwordEditTextView.text.toString()
            val nickname = nicknameEditTextView.text.toString()

            auth!!.createUserWithEmailAndPassword(id, pw).addOnCompleteListener { Task ->
                if(Task.isSuccessful) {
                    val user = UserEntity(id, pw, nickname)

                    FirebaseDatabase.getInstance().getReference("User")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user).addOnCompleteListener {
                            Toast.makeText(this@JoinUser, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@JoinUser, StartActivity::class.java)
                            startActivity(intent)
                        }
                }
            }
        }
    }
}