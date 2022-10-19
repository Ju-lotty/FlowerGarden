package com.project.flowergarden


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import com.project.flowergarden.databinding.ActivityMainOwnerBinding
import kotlinx.android.synthetic.main.activity_main_owner.*
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule


class MainActivityOwner : AppCompatActivity() {

    private lateinit var binding: ActivityMainOwnerBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var OwnerDB: DatabaseReference //실시간 데이터베이스
    private var userID: String? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        userID = user!!.uid

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val pathReference = storageReference.child("images/").child(auth!!.currentUser?.email.toString())
            pathReference.downloadUrl.addOnSuccessListener {
                storeImage.setImageURI(it)
                Glide.with(this).load(it).into(storeImage) // GlideApp 사용
                progressBar.visibility = View.GONE
            }


            OwnerDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {

                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nickname = snapshot.child("storename").value.toString()
                    val address = snapshot.child("address").value.toString()
                    val opentime = snapshot.child("opentime").value.toString()
                    val closetime = snapshot.child("closetime").value.toString()
                    val storeNumber = snapshot.child("number").value.toString()
                    val openday = snapshot.child("openday").value.toString()
                    val information = snapshot.child("information").value.toString()
                    binding.nickNameTextView.text = nickname
                    binding.addressTextView.text = address
                    binding.timeTextView.text = opentime + "~" + closetime
                    binding.numberTextView.text = storeNumber
                    binding.openDayTextView.text = openday
                    binding.informationTextView.text = information
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }





