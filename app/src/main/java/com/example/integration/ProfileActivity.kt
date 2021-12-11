package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ProfileActivity : AppCompatActivity() {
    private var mail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mail=intent.getStringExtra("key").toString()

        val btn_parametre = findViewById<Button>(R.id.profile_btn_paramètre)
        val btn_historique = findViewById<Button>(R.id.profile_btn_historique)
        val switch_moderator = findViewById<Switch>(R.id.moderator_btn_switch)
        val btn_moderator_act = findViewById<Button>(R.id.moderator_btn_activity)


        btn_historique.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("key",mail)
            startActivity(intent)
        }
        btn_parametre.setOnClickListener{
            val intent = Intent(this, ParametreActivity::class.java)
            startActivity(intent)
        }

    }

}