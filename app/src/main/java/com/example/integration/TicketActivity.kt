package com.example.integration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TicketActivity : AppCompatActivity() {
    private var mail = ""
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);


        val bouton_retour = findViewById<ImageView>(R.id.collecte_before)
        bouton_retour.setOnClickListener { finish() }

        mail=intent.getStringExtra("key").toString()

        val btn_scan = findViewById<Button>(R.id.button_scan);
        val btn_enter = findViewById<Button>(R.id.button_enter);

        val actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra("key").toString().replaceAfter("@", "").replace("@", "")

        btn_scan.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            intent.putExtra("key",mail)
            startActivity(intent)
        }

        btn_enter.setOnClickListener {
            val intent = Intent(this, EnterTicketNumberActivity::class.java)
            intent.putExtra("key",mail)
            startActivity(intent)
        }

    }


}