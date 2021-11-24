package com.example.integration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TicketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        val btn_scan = findViewById<Button>(R.id.button_scan);
        val btn_enter = findViewById<Button>(R.id.button_enter);

        btn_scan.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }

        btn_enter.setOnClickListener {
            val intent = Intent(this, EnterTicketNumberActivity::class.java)
            startActivity(intent)
        }

    }


}