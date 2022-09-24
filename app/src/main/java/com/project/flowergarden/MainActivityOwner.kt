package com.project.flowergarden

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.project.flowergarden.databinding.ActivityMainOwnerBinding


class MainActivityOwner : AppCompatActivity() {

    private lateinit var binding: ActivityMainOwnerBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var OwnerDB: DatabaseReference //실시간 데이터베이스
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        user = FirebaseAuth.getInstance().currentUser
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        userID = user!!.uid

        OwnerDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val nickname = snapshot.child("storename").value.toString()
                binding.nickNameTextView.text = "${nickname}"
                val address = snapshot.child("address").value.toString()
                binding.addressTextView.text = "${address}"
                //val nickname = snapshot.value
                //binding.nickNameTextView.text = "${nickname.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}