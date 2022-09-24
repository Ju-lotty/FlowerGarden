package com.project.flowergarden


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class AddressWebView : AppCompatActivity() {

    companion object {
        const val ADDRESS_REQUEST_CODE = 2928
    }


    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_web_view)
        val webView = findViewById<WebView>(R.id.webView)
        //자바스크립트 구문 허용
        webView.settings.javaScriptEnabled = true

        //자바스크립트인터페이스 추가
        webView.addJavascriptInterface(BridgeInterface(), "Android")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        //최초 웹뷰 로드
        webView.loadUrl("https://flowergarden-15279.web.app")
    }

    inner class BridgeInterface {

        @JavascriptInterface
        fun processDATA(address: String?) {
            Intent().apply {
                putExtra("address", address)
                setResult(RESULT_OK, this)
            }
            finish()
        }
    }

}