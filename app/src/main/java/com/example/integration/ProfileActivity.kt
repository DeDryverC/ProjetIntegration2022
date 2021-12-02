package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btn_parametre = findViewById<Button>(R.id.profile_btn_paramètre)
        val btn_historique = findViewById<Button>(R.id.profile_btn_historique)
        val switch_moderator = findViewById<SwitchCompat>(R.id.moderator_btn_switch)
        val btn_moderator_act = findViewById<Button>(R.id.moderator_btn_activity)


        /*btn_historique.setOnClickListener{
            val intent = Intent(this, ParametreActivity::class.java)
            startActivity(intent)
        }*/
        /*btn_parametre.setOnClickListener{
            val intent = Intent(this, HistoriqueActivity::class.java)
            startActivity(intent)
        }
         */
    }

}