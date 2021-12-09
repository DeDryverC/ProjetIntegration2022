package com.example.integration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class ClassementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classement)
        // finding the UI elements

        val actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra("key").toString().replaceAfter("@", "").replace("@", "")


    }
}