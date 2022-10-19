package com.project.flowergarden.ownerregister

import android.animation.ObjectAnimator
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.flowergarden.R
import com.project.flowergarden.databinding.FragmentSecondBinding
import kotlinx.android.synthetic.main.activity_join_user.*

class Second : Fragment() {
    private lateinit var binding: FragmentSecondBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        checkinit()
        ObjectAnimator.ofInt(seekBar, "progress", 80).start()
        binding.seekBar.isEnabled = false

        backButton.setOnClickListener {
            val first = First()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, first).commit()
        }

        buttoninit()

    }

    private fun checkinit() = with(binding) {
        nextButton.isEnabled = false
        var check = false
        var check2 = false
        var check3 = false
        storeNameEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                storeNameCheckTextView.visibility = View.VISIBLE
                nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                nextButton.isEnabled = false
                check = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.length >= 3) {
                    storeNameCheckTextView.visibility = View.GONE
                } else {
                    storeNameCheckTextView.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty() && s!!.length >= 3) {
                    storeNameCheckTextView.visibility = View.GONE
                    check = true
                    if (check2 && check3) {
                        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        nextButton.isEnabled = true
                    }
                }
            }
        })
        numberEditTextView.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        numberEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                nextButton.isEnabled = false
                check2 = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                numberCheckTextView.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty() && s.length >= 11) {
                    check2 = true
                    if(check && check3) {
                        numberCheckTextView.visibility = View.GONE
                        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        nextButton.isEnabled = true
                    }
                } else {
                    check2 = false
                    numberCheckTextView.visibility = View.VISIBLE
                    nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                    nextButton.isEnabled = false
                }
            }
        })
        informationEditTextView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                check3 = false
                nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_regect))
                nextButton.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s!!.isEmpty()) {
                    check3 = false
                } else if (s!!.isNotEmpty()) {
                    check3 = true
                    if (check && check2) {
                        nextButton.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_register))
                        nextButton.isEnabled = true
                    }
                }
            }

        })
    }

    private fun buttoninit() = with(binding) {

        nextButton.setOnClickListener {
            val bundle = Bundle()
            val third = Third()
            val Email1 = arguments?.getString("Email")
            val Password1 = arguments?.getString("Password")

            bundle.putString("storename", storeNameEditTextView.text.toString())
            bundle.putString("number", numberEditTextView.text.toString())
            bundle.putString("information", informationEditTextView.text.toString())

            bundle.putString("Email1", Email1)
            bundle.putString("Password1", Password1)
            third.arguments = bundle
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, third).commit()

        }
    }
}