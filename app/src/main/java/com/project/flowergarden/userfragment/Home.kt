package com.project.flowergarden.userfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.project.flowergarden.StoreDetailActivity
import com.project.flowergarden.databinding.FragmentHomeBinding
import com.project.flowergarden.entity.OwnerEntity
import com.project.flowergarden.entity.StoreAdapter

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var auth: FirebaseAuth? = null //파이어베이스 인증
    private val adapter = StoreAdapter()

    //유저 정보 불러오기 (아이디, 닉네임 등)
    private var user: FirebaseUser? = null
    private lateinit var UserDB: DatabaseReference //실시간 데이터베이스
    private lateinit var OwnerDB: DatabaseReference
    private var userID: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFlipper()
        binding.storeList.layoutManager = LinearLayoutManager(activity)
        binding.storeList.adapter = adapter
        auth = FirebaseAuth.getInstance()

        user = FirebaseAuth.getInstance().currentUser
        OwnerDB = FirebaseDatabase.getInstance().getReference("Owner")
        UserDB = FirebaseDatabase.getInstance().getReference("User")
        userID = user!!.uid

        OwnerDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.setData(OwnerEntity(snapshot.value.toString(),"","")) {
                    activity?.let {
                        val intent = Intent(context, StoreDetailActivity::class.java)
                        intent.putExtra("owner","{$it}")
                        activity!!.startActivity(intent)
                    }
                }
                /*adapter.storeList.add(OwnerEntity("", "", snapshot.child("storename").value.toString()))
                adapter.notifyDataSetChanged()
                Log.e("User", "")*/
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        //UserDB.child("User").child(uid).child("nickname").addValueEventListener(object:  ValueEventListener() {
        UserDB.child(userID!!).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("2", "${snapshot.value}")
                val nickname = snapshot.child("nickname").value.toString()
                binding.userNameTextView.text = "${nickname.toString()}님 환영합니다!"
                //val nickname = snapshot.value
                //binding.userNameTextView.text = "${nickname.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun viewFlipper() = with(binding) {
        viewFlipper.startFlipping()
        viewFlipper.flipInterval = 3000
        viewFlipper.setInAnimation(activity?.applicationContext, android.R.anim.slide_in_left)
        viewFlipper.setOutAnimation(activity?.applicationContext, android.R.anim.slide_out_right)
    }
}
