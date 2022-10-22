package com.project.flowergarden

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_custom_dialog.*

class custom_dialog(context: Context): Dialog(context)  {

    private val button: Button by lazy {
        findViewById(R.id.closeButton)
    }

    var check = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_dialog)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        button.setOnClickListener {
            imageView.visibility = View.GONE
            button.visibility = View.GONE
            dismiss()
        }
    }
}