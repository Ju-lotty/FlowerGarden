package com.project.flowergarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import com.project.flowergarden.databinding.ActivityMainUserBinding
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        val home1 = Home()
        val home2 = NearLocation()
        val home4 = Information()
        showFragment(home1)

        binding.bottomNavigationView.run{
            setOnItemSelectedListener { it ->
                when(it.itemId){
                    R.id.ic_home -> showFragment(home1)
                    R.id.ic_map ->  showFragment(home2)
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