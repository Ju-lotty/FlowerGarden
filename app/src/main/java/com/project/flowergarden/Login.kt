package com.project.flowergarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.project.flowergarden.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    private lateinit var OwnerDB : DatabaseReference //실시간 데이터베이스
    private lateinit var UserDB : DatabaseReference //실시간 데이터베이스
    private var userID: String? = null
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        loginInit()
    }
    private fun loginInit() = with(binding) {

        loginButton.setOnClickListener {
            val id = idEditTextView.text.toString()
            val pw = passwordEditTextView.text.toString()

            auth!!.signInWithEmailAndPassword(id, pw).addOnCompleteListener { Task ->
                user = FirebaseAuth.getInstance().currentUser
                userID = user!!.uid
                if(Task.isSuccessful) {
                    //OwnerDB에서 데이터를 가져왔을때
                    OwnerDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            //데이터를 할당
                            val data = snapshot.value
                            //데이터가 없을경우 해당유저는 User라는 의미이니 User화면으로 이동 아닌경우는 Owner화면으로 이동
                            if(data == null){
                                Toast.makeText(this@Login, "로그인을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@Login, MainActivityUser::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this@Login, "로그인을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@Login, MainActivityOwner::class.java)
                                startActivity(intent)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
            }
        }
    }
}