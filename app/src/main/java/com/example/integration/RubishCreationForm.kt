package com.example.integration

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RubishCreationForm : AppCompatActivity() {
    private val db = Firebase.firestore
    private var nameInput: Editable? = null
    private var descriptionInput: Editable? = null
    private var mail = ""

    private val statRef = db.collection("statistique")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rubish_creation_form)
        setSpinnerView()
        setButtonAction()
        mail = intent.getStringExtra("key").toString()
    }

    private fun setSpinnerView() {
        val spinner: Spinner = findViewById(R.id.spinner_amount_rubish)
        ArrayAdapter.createFromResource(
            this,
            R.array.amount_rubish_value,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun setButtonAction() {
        val imageButton = findViewById<Button>(R.id.rubish_upload_button)
        val sendButton = findViewById<Button>(R.id.confirm_rubish_button)
        val cancelButton = findViewById<Button>(R.id.cancel_rubish_creation_form)
        imageButton.setOnClickListener {
            uploadImg()
        }
        sendButton.setOnClickListener {
            addRubish()
        }
        cancelButton.setOnClickListener {
            cancelButton()
        }

    }


    private fun uploadImg() {

        TODO("Not yet implemented")
    }

    private fun addRubish() {

        nameInput = findViewById<EditText>(R.id.rubish_creation_title_input).editableText
        descriptionInput =
            findViewById<EditText>(R.id.rubish_creation_description_input).editableText
        val amountInput = findViewById<Spinner>(R.id.spinner_amount_rubish).selectedItem
        db.collection("depots")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.getValue("name") == "null") {
                        val idResult = document.id
                        val dbRubishRef = db.collection("depots").document(idResult)
                        dbRubishRef.update(
                            mapOf(
                                "name" to nameInput.toString(),
                                "description" to descriptionInput.toString(),
                                "amount" to amountInput.toString()
                            )
                        ).addOnSuccessListener {
                            Log.d(
                                TAG,
                                "DocumentSnapshot successfully updated!"
                            )
                        }
                            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }

        intentPlus()

    }
    fun intentPlus(){
        plusUn(mail, "MapsActivity")
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("key", mail)
        // start your next activity
        startActivity(intent)
    }

    private fun cancelButton() {

        db.collection("depots")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.getValue("name") == "null") {
                        db.collection("depots").document(document.id).delete()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot successfully deleted!"
                                )
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
                }
            }

        statRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == "compteur") {
                        var compteurTemporaire: Int =
                            document.data.getValue("compteur").toString().toInt()
                        compteurTemporaire -= 1
                        statRef.document("depots").update("compteur", compteurTemporaire)
                    }

                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        finish()



    }
    private fun plusUn(mail: String, name: String) {
        val db2 = db.collection("clients").document(mail)
        var newScore: Int
        db.runTransaction { transaction ->
            val snapshot = transaction.get(db2)
            newScore = (snapshot.getDouble("points")!! + Combien.combien(name)).toInt()
            transaction.update(db2, "points", newScore)
        }

    }

    private fun updateActionBar() {
        val actionBar = supportActionBar

        val docRef = db.collection("clients").document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                val points = document.data?.getValue("points")
                actionBar!!.title =
                    mail.replaceAfter("@", "").replace("@", "") + " : $points points "
            }
    }


}