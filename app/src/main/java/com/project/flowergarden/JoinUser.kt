package com.project.flowergarden

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.project.flowergarden.databinding.ActivityJoinUserBinding
import com.project.flowergarden.entity.OwnerEntity
import com.project.flowergarden.entity.UserEntity

class JoinUser : AppCompatActivity() {

    private lateinit var binding: ActivityJoinUserBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    var check = false
    var check2 = false
    var check3 = false
    var check4 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        seekBar()
        backButtonClicked()
        checkinit()
        joinUserinit()
        auth = FirebaseAuth.getInstance()
    }

    private fun seekBar() {
        ObjectAnimator.ofInt(binding.seekBar, "progress", 30).start()
        binding.seekBar.isEnabled = false
    }

    private fun backButtonClicked() = with(binding) {
        backButton.setOnClickListener {
            val intent = Intent(this@JoinUser, StartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkinit() = with(binding) {
        joinUserButton.isEnabled = false
        emailEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.contains("@", ignoreCase = true) && s.contains(".com", ignoreCase = true)) {
                    emailCheckTextView.visibility = View.GONE
                    check = true

                    if(check) {
                        ObjectAnimator.ofInt(seekBar, "progress",60).start()
                    }

                    if (check2 && check3 && check4) {
                        joinUserButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        joinUserButton.isEnabled = true
                    }
                } else {
                    emailCheckTextView.visibility = View.VISIBLE
                    joinUserButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                    joinUserButton.isEnabled = false
                }
                Log.d("결과1", "$s")
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        passwordEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length < 8) {
                    lengthCheckTextView.visibility = View.VISIBLE
                    joinUserButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                    joinUserButton.isEnabled = false
                } else {
                    lengthCheckTextView.visibility = View.GONE
                    check2 = true
                    if(check2) {
                        ObjectAnimator.ofInt(seekBar, "progress",80).start()
                    }
                    if (check && check3 && check4) {
                        joinUserButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        joinUserButton.isEnabled = true
                    }
                }
                Log.d("결과2", "$s")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        passwordCheckEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (passwordEditTextView.text.toString() == passwordCheckEditTextView.text.toString()) {
                    resultCheckTextView.visibility = View.GONE
                    check3 = true
                    if(check3) {
                        ObjectAnimator.ofInt(seekBar, "progress",100).start()
                    }
                    if (check && check2 && check4) {
                        joinUserButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        joinUserButton.isEnabled = true
                    }
                } else {
                    resultCheckTextView.visibility = View.VISIBLE
                    joinUserButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                    joinUserButton.isEnabled = false
                }
            }
        })
    }

    private fun joinUserinit() = with(binding){
        joinUserButton.setOnClickListener {
            ObjectAnimator.ofInt(seekBar, "progress",150).start()
            progressBar.visibility = View.VISIBLE

            val email = emailEditTextView.text.toString()
            val password = passwordEditTextView.text.toString()
            val nickname = nickNAmeEditTextView.text.toString()

            auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { Task ->
                //성공하면!
                if(Task.isSuccessful) {
                    //OwnerEntity 데이터 클래스의 값 추가하기
                    val user = UserEntity(email, password, nickname)
                    Log.d("회원가입", "회원가입 성공")
                    //아이디 비번 점주명 주소 값 설정 한 값의 경로 지정!
                    FirebaseDatabase.getInstance().getReference("User")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user).addOnCompleteListener {
                            Toast.makeText(this@JoinUser, "환영합니다! \n ${nickname}님!!", Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
                            val intent = Intent(this@JoinUser, StartActivity::class.java)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            startActivity(intent)
                        }
                } else{
                    Log.e("가입결과","${Task.exception?.message}")
                    when(Task.exception?.message) {
                        "The email address is badly formatted." ->  {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@JoinUser,"이메일 형식으로 입력하시오.", Toast.LENGTH_SHORT).show()
                            emailEditTextView.text = null
                            return@addOnCompleteListener
                        }

                        "The given password is invalid. [ Password should be at least 6 characters ]" ->  {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@JoinUser,"비밀번호는 8자리 이상입니다.", Toast.LENGTH_SHORT).show()
                            passwordCheckEditTextView.text = null
                            passwordEditTextView.text = null
                            return@addOnCompleteListener
                        }

                        "The email address is already in use by another account." ->  {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@JoinUser,"이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
                            emailEditTextView.text = null
                            return@addOnCompleteListener
                        }
                    }
                }
            }
        }
    }
}