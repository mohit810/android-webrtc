package com.example.webrtcapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.webrtcapp.R
import com.example.webrtcapp.helpers.JavascriptInterface
import kotlinx.android.synthetic.main.activity_call.*

class CallActivity : AppCompatActivity() {

    var uid: String? = null
    var isPeerConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        uid = intent.getStringExtra("uid")
        setupWebView()

    }

    private fun setupWebView() {

        webView.webChromeClient = object: WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }

        webView.settings.javaScriptEnabled = true  //this is a dangerous as this can introduce XSS vulnerabilities into your application
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.addJavascriptInterface(JavascriptInterface(this), "Android")

        loadVideoCall()
    }

    private fun loadVideoCall() {
        val filePath = "file:android_asset/index.html"
        webView.loadUrl(filePath)

        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                initializePeer()
            }
        }
    }

    private fun initializePeer() {

        callJavascriptFunction("javascript:init(\"${uid}\")")

    }

    private fun callJavascriptFunction(functionString: String) {
        webView.post { webView.evaluateJavascript(functionString, null) }
    }

    fun onPeerConnected() {
        isPeerConnected = true
    }

}