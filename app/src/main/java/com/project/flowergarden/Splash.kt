package com.project.flowergarden

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.project.flowergarden.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animation()

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            val intent = Intent(this, StartActivity::class.java)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            startActivity(intent)
            finish()
        }

        handler.postDelayed(runnable, 5000)

    }

    private fun animation() {
        binding.bottomMiddle.playAnimation()
        binding.topVertical.playAnimation()
        imageAnimation()
    }
    @SuppressLint("ResourceType")
    private fun imageAnimation() {
        val animation = AnimationUtils.loadAnimation(this@Splash, R.drawable.custom_fade_in)
        binding.img.animation = animation
    }
}