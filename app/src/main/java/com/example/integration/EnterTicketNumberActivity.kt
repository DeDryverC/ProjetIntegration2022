package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class EnterTicketNumberActivity : AppCompatActivity() {
    object Singleton {
        // lien vers bucket storage
        private val BUCKET_URL: String = "gs://projetintegration-83d97.appspot.com"

        // se connecte à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la référence collecte de la DB
        val databaseRef = FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app").getReference("collectes")

        // créer liste avec les collectes
        val collecteList = arrayListOf<CollecteModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_ticket);




        val spinner: Spinner = findViewById(R.id.spinner_tec)
        val btn_envoyer_ticket = findViewById<Button>(R.id.button_envoyer_ticket);
        val editTextNumber = findViewById<EditText>(R.id.enter_ticket_number);
        btn_envoyer_ticket.setOnClickListener {
            var ticker_number = editTextNumber.text.toString();
            var spinner_value = spinner.selectedItem.toString()
            Toast.makeText(this, spinner_value, Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.tec_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }


}