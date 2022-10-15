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
import com.project.flowergarden.R
import com.project.flowergarden.StartActivity
import com.project.flowergarden.databinding.FragmentFirstBinding


class First : Fragment() {

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
        var check = false
        var check2 = false
        var check3 = false
        emailEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.contains("@", ignoreCase = true) && s.contains(".com", ignoreCase = true)) {
                    emailCheckTextView.visibility = View.GONE
                    check = true
                    if (check2 && check3) {
                        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        nextButton.isEnabled = true
                    }
                } else {
                    emailCheckTextView.visibility = View.VISIBLE
                    nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                    nextButton.isEnabled = false
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
                    nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                    nextButton.isEnabled = false
                } else {
                    lengthCheckTextView.visibility = View.GONE
                    check2 = true
                    if (check && check3) {
                        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        nextButton.isEnabled = true
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
                    if (check && check2) {
                        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        nextButton.isEnabled = true
                    }
                } else {
                    resultCheckTextView.visibility = View.VISIBLE
                    nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                    nextButton.isEnabled = false
                }
            }
        })
    }

    private fun buttoninit() = with(binding) {

        nextButton.setOnClickListener {
            val bundle = Bundle()
            val second = Second()

            bundle.putString("Email", emailEditTextView.text.toString())
            bundle.putString("Password", passwordCheckEditTextView.text.toString())
            second.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, second).commit()

        }
    }

}