package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity(){
    private var mail = ""
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        mail=intent.getStringExtra("key").toString()



    }
}