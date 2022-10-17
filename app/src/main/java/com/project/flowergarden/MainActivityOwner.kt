package com.project.flowergarden


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.project.flowergarden.databinding.ActivityMainOwnerBinding
import kotlinx.android.synthetic.main.activity_main_owner.*
import kotlinx.android.synthetic.main.activity_main_owner.numberTextView
import kotlinx.android.synthetic.main.activity_main_owner.openDayTextView
import kotlinx.android.synthetic.main.activity_main_owner.storeImageButton
import kotlinx.android.synthetic.main.activity_store_detail.*
import kotlinx.android.synthetic.main.fragment_near_location.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivityOwner : AppCompatActivity() {

    private lateinit var binding: ActivityMainOwnerBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var OwnerDB: DatabaseReference //실시간 데이터베이스
    private var userID: String? = null

    private var fbStorage : FirebaseStorage? = null
    private var uriPhoto : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        userID = user!!.uid

        OwnerDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {

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
        initAddImageButton()
    }

    private fun initAddImageButton() {
        storeImageButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                -> { // 갤러리로 이동
                    getImageFromAlbum()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    //거부 했을 때 (생략)
                }
                else -> {
                    // 처음 권한을 시도했을 때 띄움
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_FIRST)
                }
            }
        }
    }

    private fun getImageFromAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        //모든 type
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
            return
        }
        when(requestCode){
            REQUEST_GET_IMAGE -> {
                val selectedImageURI : Uri? = data?.data
                if( selectedImageURI != null ) {
                    val imageView = findViewById<ImageView>(R.id.storeImageButton)
                    imageView.setImageURI(selectedImageURI)
                }
                else {
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun funImageUpload(view : View){

        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imgFileName = "IMAGE_" + timeStamp + "_.png"
        var storageRef = fbStorage?.reference?.child("images")?.child(imgFileName)

        storageRef?.putFile(uriPhoto!!)?.addOnSuccessListener {
            Toast.makeText(view.context, "Image Uploaded", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        const val REQUEST_FIRST = 1000
        const val REQUEST_GET_IMAGE = 2000
    }
}