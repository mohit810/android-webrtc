package com.example.webrtcapp.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.webrtcapp.R
import com.example.webrtcapp.helpers.Constants.permissions
import com.example.webrtcapp.helpers.Constants.requestcode
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isPermissionGranted()) {
            askPermissions()
        }

        button.setOnClickListener {
            if (isPermissionGranted() && !TextUtils.isEmpty(editTextUnique.text)) {
                val intent = Intent(this, CallActivity::class.java)
                intent.putExtra("uid", editTextUnique.text.toString())
                startActivity(intent)
            } else if (!isPermissionGranted()) {
                askPermissions()
            } else if (TextUtils.isEmpty(editTextUnique.text)) {
                Toast.makeText(this, "Please enter a unique id in the text box.",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun askPermissions() {
        ActivityCompat.requestPermissions(this, permissions, requestcode)
    }

    private fun isPermissionGranted(): Boolean {

        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }

        return true
    }

}