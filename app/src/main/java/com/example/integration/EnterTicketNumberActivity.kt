package com.example.integration

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EnterTicketNumberActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    val tickets = db.collection("tickets");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_ticket);




        val spinner: Spinner = findViewById(R.id.spinner_tec)
        val btn_envoyer_ticket = findViewById<Button>(R.id.button_envoyer_ticket);
        val editTextNumber = findViewById<EditText>(R.id.enter_ticket_number);


        btn_envoyer_ticket.setOnClickListener {
            val ticket_number = editTextNumber.text.toString();
            val spinner_value = spinner.selectedItem.toString();

            val ticket = hashMapOf(
                    "tec" to spinner_value
            )


            val docRef = db.collection("tickets").document("$ticket_number");
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (!document.exists()) {
                            tickets.document(ticket_number).set(ticket);
                            val intent = Intent(this, PointsReceivedActivity::class.java)
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