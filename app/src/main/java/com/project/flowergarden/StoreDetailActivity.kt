
package com.project.flowergarden

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main_owner.*

class StoreDetailActivity : AppCompatActivity() {

    private lateinit var OwnerDB: DatabaseReference

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail)
        val secondIntent = intent
        nickNameTextView.text = secondIntent.getStringExtra("storeName")
        addressTextView.text = secondIntent.getStringExtra("address")



    }
}