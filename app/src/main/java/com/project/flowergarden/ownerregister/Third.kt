package com.project.flowergarden.ownerregister

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.flowergarden.AddressWebView
import com.project.flowergarden.AddressWebView.Companion.ADDRESS_REQUEST_CODE
import com.project.flowergarden.R
import com.project.flowergarden.StartActivity
import com.project.flowergarden.addressapi.GeocodeAPI
import com.project.flowergarden.databinding.FragmentThirdBinding
import com.project.flowergarden.entity.Geocode
import com.project.flowergarden.entity.OwnerEntity
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.fragment_third.addressTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class Third : Fragment() {
    private lateinit var binding: FragmentThirdBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증
    private var selectedImg: Uri? = null
    var check = false
    var check2 = false
    var check3 = false
    var check4 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        ObjectAnimator.ofInt(seekBar, "progress", 100).start()
        binding.seekBar.isEnabled = false

        openTimeinit()
        closeTimeinit()
        checkinit()
        joinOwnerinit()

        auth = FirebaseAuth.getInstance()

        backButton.setOnClickListener {
            val second = Second()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, second).commit()
        }

        addressButton.setOnClickListener {
            Intent(context, AddressWebView::class.java).apply {
                startActivityForResult(this, ADDRESS_REQUEST_CODE)
            }
        }


        photoAddButton.setOnClickListener {
            Log.d("Third", "눌림")
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && data != null) {
            selectedImg = data.data!!
            storePhoto.setImageURI(selectedImg)
        }

        when (requestCode) {
            ADDRESS_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    // 주소를 가져와서 보여주는 부분
                    val addressData = data?.extras?.getString("address")
                    check = true
                    addressTextView.text = addressData

                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val gecodeAPI = retrofit.create(GeocodeAPI::class.java)

                    gecodeAPI.getAddress(
                        "${addressData}",
                        "r6ybv8etcx",
                        "qqkyzHKELK73depj7486JvrjZyDhR2GDovdDu7BF"
                    )
                        .enqueue(object : Callback<Geocode> {
                            override fun onResponse(
                                call: Call<Geocode>,
                                response: Response<Geocode>
                            ) {
                                if (response.isSuccessful.not()) {
                                    return
                                }
                                response.body()?.let { it ->
                                    it.addresses.forEach {
                                        x_Result.text = it.x
                                        y_Result.text = it.y
                                    }
                                }
                            }

                            @SuppressLint("LogNotTimber")
                            override fun onFailure(call: Call<Geocode>, t: Throwable) {
                                Log.d("Fail", "실패")
                            }
                        })
                }
            }
        }
    }


    @SuppressLint("LogNotTimber")
    private fun openTimeinit() = with(binding) {
        openTime.setOnClickListener {
            timeCheckButton.visibility = View.VISIBLE
            selectOpenTime.visibility = View.VISIBLE
            selectOpenTime.setOnTimeChangedListener { _, hourOfDay, minute ->
                openTime.text = "$hourOfDay" + ":" + "$minute"
                check2 = true
                Log.d("결과는!", "$hourOfDay $minute")
                Log.d("openTime 결과는!", "$openTime")
                when (minute) {
                    0 -> openTime.text = "$hourOfDay" + ":" + "00"
                }
            }
            timeCheckButton.setOnClickListener {
                selectOpenTime.visibility = View.GONE
                timeCheckButton.visibility = View.GONE
            }
        }
    }


    @SuppressLint("LogNotTimber", "SetTextI18n")
    private fun closeTimeinit() = with(binding) {
        closeTime.setOnClickListener {
            timeCheckButton.visibility = View.VISIBLE
            selectCloseTime.visibility = View.VISIBLE
            selectCloseTime.setOnTimeChangedListener { _, hourOfDay, minute ->
                check3 = true
                closeTime.text = "$hourOfDay" + ":" + "$minute"
                Log.d("결과는!", "$hourOfDay $minute")
                when (minute) {
                    0 -> closeTime.text = "$hourOfDay" + ":" + "00"
                }
            }
            timeCheckButton.setOnClickListener {
                selectCloseTime.visibility = View.GONE
                timeCheckButton.visibility = View.GONE
                if (openTime.text.isNotEmpty()) {
                    timeCheckTextView.visibility = View.GONE
                }
            }
        }
    }


    private fun checkinit() = with(binding) {
        joinOwnerButton.isEnabled = false

        openDayEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                joinOwnerButton.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    check4 = true
                    if (check && check2 && check3) {
                        joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        joinOwnerButton.isEnabled = true
                    }
                }
            }
        })
    }


    @SuppressLint("LogNotTimber")
    private fun joinOwnerinit() = with(binding) {
        //점주로 회원가입 버튼을 누르면
        joinOwnerButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            ObjectAnimator.ofInt(seekBar, "progress", 150).start()
            //아이디 비번 점주명 주소 값 설정
            val email = arguments?.getString("Email1")
            val password = arguments?.getString("Password1")
            val storename = arguments?.getString("storename")
            val number = arguments?.getString("number")
            val address = addressTextView.text.toString()
            val opentime = openTime.text.toString()
            val closetime = closeTime.text.toString()
            val openday = openDayEditTextView.text.toString()
            val x = x_Result.text.toString()
            val y = y_Result.text.toString()


                var storage : FirebaseStorage? = FirebaseStorage.getInstance() //firebasestorage인스턴스생성
                //파일 이름 생성.
                var fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"

                var imagesRef = storage!!.reference.child("images/").child(fileName)    //기본 참조 위치/images/${fileName}
                imagesRef.putFile(selectedImg!!)

            val owner = OwnerEntity("$email", "$password", "$storename", "$number", address, opentime, closetime, openday, x, y, imagesRef.toString())

            //유저 만들기 값은 (id, pw)
            auth!!.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener { Task ->
                    //성공하면!
                    if (Task.isSuccessful) {
                        //아이디 비번 점주명 주소 값 설정 한 값의 경로 지정!
                        FirebaseDatabase.getInstance().getReference("Owner")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(owner)
                            .addOnCompleteListener {
                                Toast.makeText(
                                    context,
                                    "환영합니다! \n ${storename} 점주님!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.VISIBLE
                                val intent = Intent(context, StartActivity::class.java)
                                requireActivity().overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                                startActivity(intent)
                            }
                    } else {
                        Log.e("가입결과", "${Task.exception?.message}")
                        when (Task.exception?.message) {
                            "The email address is badly formatted." -> {
                                val first = First()
                                progressBar.visibility = View.GONE
                                Toast.makeText(context, "이메일 형식으로 입력하시오.", Toast.LENGTH_SHORT)
                                    .show()
                                emailEditTextView.text = null
                                requireActivity().overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.container, first).commit()
                                return@addOnCompleteListener
                            }
                            "The email address is already in use by another account." -> {
                                val first = First()
                                progressBar.visibility = View.GONE
                                Toast.makeText(context, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT)
                                    .show()
                                requireActivity().overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.container, first).commit()
                                return@addOnCompleteListener
                            }
                        }
                    }
                }
        }
    }
}