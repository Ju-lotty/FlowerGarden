package com.project.flowergarden


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.flowergarden.AddressWebView.Companion.ADDRESS_REQUEST_CODE
import com.project.flowergarden.addressapi.GeocodeAPI
import com.project.flowergarden.databinding.ActivityJoinOwnerBinding
import com.project.flowergarden.entity.Geocode
import com.project.flowergarden.entity.OwnerEntity
import kotlinx.android.synthetic.main.activity_join_owner.*
import kotlinx.android.synthetic.main.activity_join_owner.closeTime
import kotlinx.android.synthetic.main.activity_join_owner.openTime
import kotlinx.android.synthetic.main.fragment_near_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
        openTimeinit()
        closeTimeinit()

        addressButton.setOnClickListener {
            Intent(this, AddressWebView::class.java).apply {
                startActivityForResult(this, ADDRESS_REQUEST_CODE)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ADDRESS_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    // 주소를 가져와서 보여주는 부분
                    val addressData = data?.extras?.getString("address")
                    address.text = addressData

                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val gecodeAPI = retrofit.create(GeocodeAPI::class.java)

                    gecodeAPI.getAddress("${addressData}","r6ybv8etcx","qqkyzHKELK73depj7486JvrjZyDhR2GDovdDu7BF")
                        .enqueue(object: Callback<Geocode> {
                            override fun onResponse(call: Call<Geocode>, response: Response<Geocode>) {
                                if(response.isSuccessful.not()) {
                                    return
                                }
                                response.body()?.let {
                                    it.addresses.forEach {
                                        x_Result.text = it.x.toString()
                                        y_Result.text = it.y.toString()
                                        Log.d("결과", "결과는 : $it")
                                    }
                                }
                            }
                            override fun onFailure(call: Call<Geocode>, t: Throwable) {
                                Log.d("Fail", "실패")
                            }
                        })

                }
            }
        }
    }

    private fun openTimeinit() = with(binding) {
        openTime.setOnClickListener {
            addressButton.visibility = View.GONE
            timeCheckButton.visibility = View.VISIBLE
            selectOpenTime.visibility = View.VISIBLE
            selectOpenTime.setOnTimeChangedListener { view, hourOfDay, minute ->
                openTime.text = "$hourOfDay" + ":" + "$minute"
                Log.d("결과는!", "$hourOfDay $minute")
                Log.d("openTime 결과는!", "$openTime")
                when(minute) {
                    0 -> openTime.text = "$hourOfDay" + ":" + "00"
                }
            }
            timeCheckButton.setOnClickListener {
                selectOpenTime.visibility = View.GONE
                timeCheckButton.visibility = View.GONE
                addressButton.visibility = View.VISIBLE
            }
        }
    }

    private fun closeTimeinit() = with(binding) {
        closeTime.setOnClickListener {
            addressButton.visibility = View.GONE
            timeCheckButton.visibility = View.VISIBLE
            selectCloseTime.visibility = View.VISIBLE
            selectCloseTime.setOnTimeChangedListener { view, hourOfDay, minute ->
                closeTime.text = "$hourOfDay" + ":" + "$minute"
                Log.d("결과는!", "$hourOfDay $minute")
                Log.d("openTime 결과는!", "$openTime")
                when(minute) {
                    0 -> closeTime.text = "$hourOfDay" + ":" + "00"
                }
            }
            timeCheckButton.setOnClickListener {
                selectCloseTime.visibility = View.GONE
                timeCheckButton.visibility = View.GONE
                addressButton.visibility = View.VISIBLE
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
            val number = number.text.toString()
            val opentime = openTime.text.toString()
            val closetime = closeTime.text.toString()
            val openday = openDay.text.toString()
            val address = address.text.toString()
            val x = x_Result.text.toString()
            val y = y_Result.text.toString()

            //유저 만들기 값은 (id, pw)
            auth!!.createUserWithEmailAndPassword(id, pw).addOnCompleteListener { Task ->
                //성공하면!
                if(Task.isSuccessful) {
                    //OwnerEntity 데이터 클래스의 값 추가하기
                    val owner = OwnerEntity(id, pw, storename, number, opentime, closetime, openday, address, x, y)
                    Log.d("회원가입", "회원가입 성공")
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