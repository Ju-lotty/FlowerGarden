
package com.project.flowergarden

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main_owner.addressTextView
import kotlinx.android.synthetic.main.activity_main_owner.nickNameTextView
import kotlinx.android.synthetic.main.activity_store_detail.*

class StoreDetailActivity : AppCompatActivity() {

    private lateinit var OwnerDB: DatabaseReference

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail)
        val secondIntent = intent
        nickNameTextView.text = secondIntent.getStringExtra("storeName")
        addressTextView.text = secondIntent.getStringExtra("address")
        timeTextView.text = secondIntent.getStringExtra("opentime") + "~" + secondIntent.getStringExtra("closetime")
        numberTextView.text = secondIntent.getStringExtra("storeNumber")
        openDayTextView.text = secondIntent.getStringExtra("openday")
    }
}