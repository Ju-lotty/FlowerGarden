
package com.project.flowergarden

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main_owner.addressTextView
import kotlinx.android.synthetic.main.activity_main_owner.nickNameTextView
import kotlinx.android.synthetic.main.activity_store_detail.*
import kotlinx.android.synthetic.main.activity_store_detail.numberTextView
import kotlinx.android.synthetic.main.activity_store_detail.openDayTextView
import kotlinx.android.synthetic.main.activity_store_detail.timeTextView

class StoreDetailActivity : AppCompatActivity() {

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
        val email = secondIntent.getStringExtra("email")
        Log.d("Store 결과", "$email")



        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val pathReference = storageReference.child("images/").child(email.toString())
        pathReference.downloadUrl.addOnSuccessListener {
            Log.d("Store it 결과", "$it")
            Glide.with(this).load(it).into(detailStoreImage)
            progressBar.visibility = View.GONE
        }
        val number = numberTextView.text.toString()
        Log.d("번호 결과는 : ", "$number")
        numberTextView.setOnClickListener {
            Log.d("번호 결과", "클릭 됨")
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent);
        }

    }
}