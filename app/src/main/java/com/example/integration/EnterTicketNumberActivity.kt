package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class EnterTicketNumberActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    val tickets = db.collection("tickets");
    private var mail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_ticket);

        mail=intent.getStringExtra("key").toString()


        val spinner: Spinner = findViewById(R.id.spinner_tec)
        val btn_envoyer_ticket = findViewById<Button>(R.id.button_envoyer_ticket);
        val editTextNumber = findViewById<EditText>(R.id.enter_ticket_number);


        btn_envoyer_ticket.setOnClickListener {
            val ticket_number = editTextNumber.text.toString();
            val spinner_value = spinner.selectedItem.toString();

            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())

            val ticket = hashMapOf(
                    "action" to "ticket",
                    "tec" to spinner_value,
                    "user" to mail,
                    "date" to currentDate,
                    "points" to 1
            )

            val action = hashMapOf(
                "action" to "ticket",
                "date" to currentDate,
                "location" to "/",
                "points" to 1,
                "user" to mail
            )


            val docRef = db.collection("tickets").document("$ticket_number");
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (!document.exists()) {
                            tickets.document(ticket_number).set(ticket);
                            db.collection("action").document().set(action)
                            val intent = Intent(this, PointsReceivedActivity::class.java)
                            intent.putExtra("key",mail)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Ce ticket à déjà été enregistré", Toast.LENGTH_SHORT).show();

                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                    }

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