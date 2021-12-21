package com.example.integration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore

class PointsReceivedActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    private var mail = ""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_received);

        mail=intent.getStringExtra("key").toString()

        plusUn()

        val btn_go_on = findViewById<Button>(R.id.btn_go_on);
        btn_go_on.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("key",mail)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    private fun updateActionBar(){
        val actionBar = supportActionBar
        //mail passé avec l'intent via 3 fichiers
        val docRef = db.collection("clients").document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val points =  document.data?.getValue("points")
                    actionBar!!.title = mail.replaceAfter("@", "").replace("@", "") + " : $points points "
                }
            }
    }

    private fun plusUn() {

        val db2 = db.collection("clients").document(mail)
        var newScore: Int
        db.runTransaction { transaction ->
            val snapshot = transaction.get(db2)
            newScore = (snapshot.getDouble("points")!! + 1).toInt()
            transaction.update(db2, "points", newScore)
        }
        Thread.sleep(500)
        Toast.makeText(this, "Vous avez gagné un point", Toast.LENGTH_SHORT).show();
        updateActionBar()
    }


}