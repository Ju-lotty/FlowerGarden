package com.project.flowergarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.project.flowergarden.databinding.ActivityMainUserBinding
import com.project.flowergarden.entity.OwnerEntity
import com.project.flowergarden.entity.StoreAdapter

class MainActivityUser : AppCompatActivity() {

    private lateinit var binding: ActivityMainUserBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증
    private val adapter = StoreAdapter()

    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var UserDB: DatabaseReference //실시간 데이터베이스
    private lateinit var OwnerDB: DatabaseReference
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewFlipper()
        binding.storeList.layoutManager = LinearLayoutManager(this)
        binding.storeList.adapter = adapter
        auth = FirebaseAuth.getInstance()

        user = FirebaseAuth.getInstance().currentUser
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        UserDB = FirebaseDatabase.getInstance().getReference("User")
        userID = user!!.uid
        OwnerDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.storeList.add(OwnerEntity(snapshot.value.toString(),"",""))
                adapter.notifyDataSetChanged()
                Log.e("User", "")

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        UserDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("2", "${snapshot.value}")
                val nickname = snapshot.child("nickname").value.toString()
                binding.userNameTextView.text = "${nickname.toString()}님 환영합니다!"
                //val nickname = snapshot.value
                //binding.userNameTextView.text = "${nickname.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun viewFlipper() = with(binding) {
        viewFlipper.startFlipping()
        viewFlipper.flipInterval = 3000
        viewFlipper.setInAnimation(this@MainActivityUser, android.R.anim.slide_in_left)
        viewFlipper.setOutAnimation(this@MainActivityUser, android.R.anim.slide_out_right)
    }
}