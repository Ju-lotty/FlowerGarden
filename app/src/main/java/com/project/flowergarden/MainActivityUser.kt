package com.project.flowergarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.project.flowergarden.databinding.ActivityMainUserBinding
import com.project.flowergarden.entity.StoreAdapter
import com.project.flowergarden.userfragment.Home
import com.project.flowergarden.userfragment.Information
import com.project.flowergarden.userfragment.Like
import com.project.flowergarden.userfragment.NearLocation

class MainActivityUser : AppCompatActivity() {

    private lateinit var binding: ActivityMainUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val home1 = Home()
        val home2 = NearLocation()
        val home3 = Like()
        val home4 = Information()
        showFragment(home1)

        binding.bottomNavigationView.run{
            setOnItemSelectedListener { it ->
                when(it.itemId){
                    R.id.ic_home -> showFragment(home1)
                    R.id.ic_map -> showFragment(home2)
                    R.id.ic_like-> showFragment(home3)
                    R.id.ic_information -> showFragment(home4)
                    else -> true
                }
            }
        }
    }


    private fun showFragment(fragment: Fragment): Boolean{
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }
        return true
    }

}