package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ProfileActivity : AppCompatActivity() {
    private var mail = ""
    private var moderator = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        if(moderator===true) {
            val moderator_sidebar = findViewById<View>(R.id.moderator_sidebar)
            val moderator_activity = findViewById<Button>(R.id.moderator_btn_activity)
            moderator_sidebar.visibility = View.VISIBLE
            moderator_activity.visibility = View.VISIBLE

        }
        mail=intent.getStringExtra("key").toString()

        val btn_parametre = findViewById<Button>(R.id.profile_btn_paramètre)
        val btn_historique = findViewById<Button>(R.id.profile_btn_historique)


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