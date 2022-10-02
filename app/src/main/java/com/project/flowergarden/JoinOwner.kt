package com.project.flowergarden


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.flowergarden.AddressWebView.Companion.ADDRESS_REQUEST_CODE
import com.project.flowergarden.databinding.ActivityJoinOwnerBinding
import com.project.flowergarden.entity.OwnerEntity
import kotlinx.android.synthetic.main.activity_join_owner.*


class JoinOwner : AppCompatActivity() {

    private lateinit var binding: ActivityJoinOwnerBinding


    private var auth: FirebaseAuth? = null //파이어베이스 인증

    private lateinit var OwnerDB : DatabaseReference //실시간 데이터베이스

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        JoinOwnerinit()

        addressButton.setOnClickListener {
            Intent(this, AddressWebView::class.java).apply {
                startActivityForResult(this, ADDRESS_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ADDRESS_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    // 주소를 가져와서 보여주는 부분
                    val addressData = data?.extras?.getString("address")
                    address.text = addressData
                }
            }
        }
    }

    private fun JoinOwnerinit() = with(binding) {
        //점주로 회원가입 버튼을 누르면
        joinOwnerButton.setOnClickListener {

            //아이디 비번 점주명 주소 값 설정
            val id = idEditTextView.text.toString()
            val pw = passwordEditTextView.text.toString()
            val storename = storenameEditTextView.text.toString()
            val address = address.text.toString()

            //유저 만들기 값은 (id, pw)
            auth!!.createUserWithEmailAndPassword(id, pw).addOnCompleteListener { Task ->
                //성공하면!
                if(Task.isSuccessful) {
                    //OwnerEntity 데이터 클래스의 값 추가하기
                    val owner = OwnerEntity(id, pw, storename, address)
                    //아이디 비번 점주명 주소 값 설정 한 값의 경로 지정!
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