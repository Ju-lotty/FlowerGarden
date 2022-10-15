package com.project.flowergarden.userfragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.project.flowergarden.MainActivityUser
import com.project.flowergarden.R
import com.project.flowergarden.StartActivity
import com.project.flowergarden.databinding.FragmentInformationBinding
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_information.*
import kotlinx.android.synthetic.main.fragment_like.*

class Information : Fragment() {

    private lateinit var binding: FragmentInformationBinding

    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var UserDB: DatabaseReference //실시간 데이터베이스
    private var userID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        logoutButtonClicked()
        secessionButtonClicked()
        showUserNickname()
        likeButtoninit()
    }



    private fun showUserNickname() {
        user = FirebaseAuth.getInstance().currentUser
        UserDB = FirebaseDatabase.getInstance().getReference("User")
        userID = user!!.uid

        UserDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val nickname = snapshot.child("nickname").value.toString()
                binding.userNameTextView.text = "${nickname.toString()}님"
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun likeButtoninit() {
        likeButton.setOnClickListener {
            val like = Like()
            activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container, like)
                .commit()
        }
    }

    private fun logoutButtonClicked() {
        logoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle("로그아웃") //제목
                .setMessage("로그아웃을 하시겠습니까?") // 메시지
                .setPositiveButton("확인") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    activity?.let {
                        val intent = Intent(context, StartActivity::class.java)
                        startActivity(intent)
                    }
                }
                .setNegativeButton("취소") { _, _ -> }
                .create()
                .show()
        }
    }
    private fun secessionButtonClicked() {
        secessionButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle("회원탈퇴") //제목
                .setMessage("회원탈퇴를 하시겠습니까? \n탈퇴한 계정은 복구가 불가합니다.") // 메시지
                .setPositiveButton("확인") { _, _ ->
                    FirebaseAuth.getInstance().currentUser?.delete()
                    activity?.let {
                        val intent = Intent(context, StartActivity::class.java)
                        startActivity(intent)
                    }
                }
                .setNegativeButton("취소") { _, _ -> }
                .create()
                .show()
        }
    }

}