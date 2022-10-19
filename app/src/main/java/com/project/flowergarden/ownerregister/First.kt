package com.project.flowergarden.ownerregister

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.project.flowergarden.R
import com.project.flowergarden.StartActivity
import com.project.flowergarden.databinding.FragmentFirstBinding

class First : Fragment() {

    var check = false
    var check2 = false
    var check3 = false

    private var auth: FirebaseAuth? = null //파이어베이스 인증

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        checkinit()
        auth = FirebaseAuth.getInstance()

        ObjectAnimator.ofInt(binding.seekBar, "progress", 60).start()
        binding.seekBar.isEnabled = false

        backButton.setOnClickListener {
            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }

        buttoninit()
    }

    private fun checkinit() = with(binding) {
        nextButton.isEnabled = false
        emailEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                check = false
                regectbutton()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.contains("@", ignoreCase = true) && s.contains(".com", ignoreCase = true)) {
                    emailCheckTextView.visibility = View.GONE
                    check = true

                    if(check2 && check3) {
                        permitbutton()
                    }
                } else {
                    emailCheckTextView.visibility = View.VISIBLE
                    check = false
                    regectbutton()
                }
                Log.d("결과1", "$s")
            }
            override fun afterTextChanged(s: Editable?) {
                val result1 = emailEditTextView.text.toString()
                if(result1.isEmpty()) {
                    check = false
                    regectbutton()
                }
            }
        })

        passwordEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                check2 = false
                regectbutton()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length < 8) {
                    lengthCheckTextView.visibility = View.VISIBLE
                    check2 = false
                    regectbutton()
                } else {
                    lengthCheckTextView.visibility = View.GONE
                    check2 = true
                    if(check && check3) {
                        permitbutton()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val result2 = emailEditTextView.text.toString()
                if(result2.isEmpty()) {
                    check2 = false
                    regectbutton()
                }
                if (s!!.length < 8) {
                    lengthCheckTextView.visibility = View.VISIBLE
                    check2 = false
                    regectbutton()
                } else {
                    lengthCheckTextView.visibility = View.GONE
                    check2 = true
                    if(check && check3) {
                        permitbutton()
                    }
                }

                if (passwordEditTextView.text.toString() == passwordCheckEditTextView.text.toString() && result2.length < 9) {
                    resultCheckTextView.visibility = View.GONE
                    check3 = true
                    if(check && check2) {
                        permitbutton()
                    }
                } else {
                    resultCheckTextView.visibility = View.VISIBLE
                    check3 = false
                }
            }
        })

        passwordCheckEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                check3 = false
                regectbutton()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (passwordEditTextView.text.toString() == passwordCheckEditTextView.text.toString()) {
                    resultCheckTextView.visibility = View.GONE
                    check3 = true
                    if(check && check2) {
                        permitbutton()
                    }
                } else {
                    resultCheckTextView.visibility = View.VISIBLE
                    check3 = false
                    regectbutton()
                }
            }
        })
    }


    private fun permitbutton() = with(binding) {
        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
        nextButton.isEnabled = true
    }


    private fun regectbutton() = with(binding) {
        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
        nextButton.isEnabled = false
    }

    private fun buttoninit() = with(binding) {

        nextButton.setOnClickListener {
            val bundle = Bundle()
            val second = Second()

            bundle.putString("Email", emailEditTextView.text.toString())
            bundle.putString("Password", passwordCheckEditTextView.text.toString())
            second.arguments = bundle
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, second).commit()
            }
        }
    }


