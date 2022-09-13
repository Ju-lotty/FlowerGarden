
package com.project.flowergarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class StoreDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail)
        intent.getStringExtra("owner").let {
            Toast.makeText(this,"${it}",Toast.LENGTH_SHORT).show()
        }
    }
}