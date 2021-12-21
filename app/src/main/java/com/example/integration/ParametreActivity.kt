package com.example.integration

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ParametreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parametre)
        val btn_changer_nom = findViewById<Button>(R.id.setting_btn_changename)

        btn_changer_nom.setOnClickListener{
            /*faire form */
        }
    }
}
