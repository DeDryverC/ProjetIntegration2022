package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private var mail = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mail = intent.getStringExtra("key").toString()

        val btn_parametre = findViewById<Button>(R.id.profile_btn_parametre)
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

        setupCompontents()
    }

    private fun setupCompontents() {
        db.collection("clients")
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    if(document.data.getValue("email").toString() == mail ) {
                        val points = document.data.getValue("points").toString()
                        val name = document.data.getValue("name").toString()
                        findViewById<TextView>(R.id.profile_points).text = points
                        findViewById<TextView>(R.id.profile_name).text = name
                        if (document.data.getValue("moderator") == true) {
                            val moderator_sidebar =
                                findViewById<View>(R.id.moderator_sidebar)
                            val moderator_activity =
                                findViewById<Button>(R.id.moderator_btn_activity)
                            moderator_sidebar.visibility = View.VISIBLE
                            moderator_activity.visibility = View.VISIBLE
                            moderator_activity.setOnClickListener{
                                val intent = Intent(this, ModeratorActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
    }

}