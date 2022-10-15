package com.project.flowergarden.ownerregister

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.flowergarden.AddressWebView
import com.project.flowergarden.AddressWebView.Companion.ADDRESS_REQUEST_CODE
import com.project.flowergarden.R
import com.project.flowergarden.StartActivity
import com.project.flowergarden.addressapi.GeocodeAPI
import com.project.flowergarden.databinding.FragmentThirdBinding
import com.project.flowergarden.entity.Geocode
import com.project.flowergarden.entity.OwnerEntity
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.item_store.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Third : Fragment() {
    private lateinit var binding: FragmentThirdBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    var check = false
    var check2 = false
    var check3 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        ObjectAnimator.ofInt(seekBar, "progress",100).start()
        binding.seekBar.isEnabled = false

        openTimeinit()
        closeTimeinit()
        checkinit()
        joinOwnerinit()

        auth = FirebaseAuth.getInstance()

        backButton.setOnClickListener {
            val second = Second()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, second).commit()
        }

        addressButton.setOnClickListener {
            Log.d("결과~!!~!", "주소 입력 눌림!!")
            Intent(context, AddressWebView::class.java).apply {
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
                    addressTextView.text = addressData

                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val gecodeAPI = retrofit.create(GeocodeAPI::class.java)

                    gecodeAPI.getAddress("${addressData}", "r6ybv8etcx", "qqkyzHKELK73depj7486JvrjZyDhR2GDovdDu7BF")
                        .enqueue(object : Callback<Geocode> {
                            override fun onResponse(
                                call: Call<Geocode>,
                                response: Response<Geocode>
                            ) {
                                if (response.isSuccessful.not()) {
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


    @SuppressLint("LogNotTimber")
    private fun openTimeinit() = with(binding) {
        openTime.setOnClickListener {
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
            }
        }
    }


    @SuppressLint("LogNotTimber")
    private fun closeTimeinit() = with(binding) {
        closeTime.setOnClickListener {
            timeCheckButton.visibility = View.VISIBLE
            selectCloseTime.visibility = View.VISIBLE
            selectCloseTime.setOnTimeChangedListener { view, hourOfDay, minute ->
                closeTime.text = "$hourOfDay" + ":" + "$minute"
                Log.d("결과는!", "$hourOfDay $minute")
                when(minute) {
                    0 -> closeTime.text = "$hourOfDay" + ":" + "00"
                }
            }
            timeCheckButton.setOnClickListener {
                selectCloseTime.visibility = View.GONE
                timeCheckButton.visibility = View.GONE
                if(openTime.text.isNotEmpty()) {
                    timeCheckTextView.visibility = View.GONE
                }
            }
        }
    }


    private fun checkinit() = with(binding){
        joinOwnerButton.isEnabled = false

        if(addressTextView.text.isEmpty()) {
            joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
            joinOwnerButton.isEnabled = false
            check = false
        } else {
            joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
            joinOwnerButton.isEnabled = true
            check = true
        }

        if(openTime.text.isEmpty() || closeTime.text.isEmpty()) {
            joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
            joinOwnerButton.isEnabled = false
            check2 = false
        } else if (openTime.text.isNotEmpty() && closeTime.text.isNotEmpty()) {
            joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
            joinOwnerButton.isEnabled = true
            check2 = true
        }

        openDayEditTextView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                joinOwnerButton.isEnabled = false
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                openDayCheckTextView.visibility = View.GONE
            }
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()) {
                    joinOwnerButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                    joinOwnerButton.isEnabled = true
                    openDayCheckTextView.visibility = View.GONE
                    check3 = true
                }
            }
        })
    }


    private fun joinOwnerinit() = with(binding) {
        //점주로 회원가입 버튼을 누르면
        joinOwnerButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            ObjectAnimator.ofInt(seekBar, "progress",150).start()
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

            Log.d("최종결과 email", "$email")
            Log.d("최종결과 password", "$password")
            Log.d("최종결과 storename", "$storename")
            Log.d("최종결과 number", "$number")
            Log.d("최종결과 address", "$address")
            Log.d("최종결과 opentime", "$opentime")
            Log.d("최종결과 closetime", "$closetime")
            Log.d("최종결과 openday", "$openday")
            Log.d("최종결과 x", "$x")
            Log.d("최종결과 y", "$y")

            //유저 만들기 값은 (id, pw)
            auth!!.createUserWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener { Task ->
                //성공하면!
                if(Task.isSuccessful) {
                    //OwnerEntity 데이터 클래스의 값 추가하기
                    val owner = OwnerEntity("$email", "$password", "$storename", "$number", address, opentime, closetime, openday, x, y)
                    Log.d("회원가입", "회원가입 성공")
                    //아이디 비번 점주명 주소 값 설정 한 값의 경로 지정!
                    FirebaseDatabase.getInstance().getReference("Owner")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(owner).addOnCompleteListener {
                            Toast.makeText(context, "환영합니다! \n ${storename} 점주님!", Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.VISIBLE
                            val intent = Intent(context, StartActivity::class.java)
                            startActivity(intent)
                        }
                }
            }
        }
    }
}