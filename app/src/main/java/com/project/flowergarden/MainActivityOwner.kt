package com.project.flowergarden


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.project.flowergarden.databinding.ActivityMainOwnerBinding


class MainActivityOwner : AppCompatActivity() {

    private lateinit var binding: ActivityMainOwnerBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var OwnerDB: DatabaseReference //실시간 데이터베이스
    private var userID: String? = null

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference
    var getImage = databaseReference.child("images")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        userID = user!!.uid

        val storage = Firebase.storage



        OwnerDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val nickname = snapshot.child("storename").value.toString()
                val address = snapshot.child("address").value.toString()
                val opentime = snapshot.child("opentime").value.toString()
                val closetime = snapshot.child("closetime").value.toString()
                val storeNumber = snapshot.child("number").value.toString()
                val openday = snapshot.child("openday").value.toString()
                binding.nickNameTextView.text = nickname
                binding.addressTextView.text = address
                binding.timeTextView.text = opentime + "~" + closetime
                binding.numberTextView.text = storeNumber
                binding.openDayTextView.text = openday
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    companion object {
        val LOG = MainActivityOwner
    }
}