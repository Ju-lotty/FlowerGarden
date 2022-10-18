package com.project.flowergarden


import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.project.flowergarden.databinding.ActivityJoinOwnerBinding
import com.project.flowergarden.ownerregister.First

class JoinOwner : AppCompatActivity() {

    private lateinit var binding: ActivityJoinOwnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        val first = First()
        supportFragmentManager.beginTransaction().replace(R.id.container, first).commit()
    }

}