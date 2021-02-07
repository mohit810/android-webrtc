package com.example.webrtcapp.helpers

import android.webkit.JavascriptInterface
import com.example.webrtcapp.activities.CallActivity

class JavascriptInterface(val callActivity: CallActivity) {

    @JavascriptInterface
    public fun onPeerConnected() {
        callActivity.onPeerConnected()
    }

}