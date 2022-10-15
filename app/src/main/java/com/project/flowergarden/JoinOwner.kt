package com.project.flowergarden


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.flowergarden.databinding.ActivityJoinOwnerBinding
import com.project.flowergarden.ownerregister.First

class JoinOwner : AppCompatActivity() {

    private lateinit var binding: ActivityJoinOwnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val first = First()
        supportFragmentManager.beginTransaction().replace(R.id.container, first).commit()
    }
}