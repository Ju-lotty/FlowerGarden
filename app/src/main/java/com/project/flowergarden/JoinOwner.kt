package com.project.flowergarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.flowergarden.databinding.ActivityJoinOwnerBinding
import com.project.flowergarden.entity.OwnerEntity


class JoinOwner : AppCompatActivity() {

    private lateinit var binding: ActivityJoinOwnerBinding


    private var auth: FirebaseAuth? = null //파이어베이스 인증

    private lateinit var OwnerDB : DatabaseReference //실시간 데이터베이스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        JoinOwnerinit()
    }
    private fun JoinOwnerinit() = with(binding) {

        joinOwnerButton.setOnClickListener {
            val id = idEditTextView.text.toString()
            val pw = passwordEditTextView.text.toString()
            val storename = storenameEditTextView.text.toString()

            auth!!.createUserWithEmailAndPassword(id, pw).addOnCompleteListener { Task ->
                if(Task.isSuccessful) {
                    val owner = OwnerEntity(id, pw, storename)

                    FirebaseDatabase.getInstance().getReference("Owner")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(owner).addOnCompleteListener {
                            Toast.makeText(this@JoinOwner, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@JoinOwner, StartActivity::class.java)
                            startActivity(intent)
                        }
                }
            }
        }

    }
}