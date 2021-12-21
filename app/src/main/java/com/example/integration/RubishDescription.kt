package com.example.integration

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.integration.ModeratorRepository.Singleton.repList
import com.example.integration.RubishDescription.Singleton.count
import com.example.integration.model.ModeratorModel
import com.google.firebase.database.*
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RubishDescription : AppCompatActivity() {
    //fun onLoad() {Toast.makeText(this, "msg", Toast.LENGTH_LONG).show()}
    val db = Firebase.firestore
    object Singleton {

        var count = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rubish_description_page)
        setupComponents()

    }

    private fun setupComponents() {

        //création des variable récupéré de MapsActtivity
        val markerTitle = intent.getStringExtra("MARKER_TITLE")
        val markerDescription = intent.getStringExtra("MARKER_DESCRIPTION")
        val userId = intent.getStringExtra("ID_USER")

        //configuration des TextView et du bouton delRubish
        findViewById<TextView>(R.id.rubish_title_content).text = markerTitle
        findViewById<TextView>(R.id.rubish_description_content).text = markerDescription
        findViewById<ImageView>(R.id.rubish_report_button).setOnClickListener {
            repRubish(markerTitle, userId)
        }
        findViewById<ImageView>(R.id.close_rubish_button).setOnClickListener {
            delRubish(markerTitle, userId)
        }
        getSize{}
    }


    private fun delRubish(markerTitle: String?, userId: String?) {
        // Appel de la méthode .delete() avec les résultat du get() si l'utilisateur est bien le créateur de la méthode
        // Sinon message d'erreur

        // requete a la DB
        db.collection("depots")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //trouve le bon depot et vérifie que l'utilisateur est bien le créateur
                    if ((document.data.getValue("name") == markerTitle) and (document.data.getValue(
                            "creator"
                        ) == userId)
                    ) {
                        //suppresion du dépot
                        db.collection("depots").document(document.id).delete()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot successfully deleted!"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(
                                    TAG,
                                    "Error deleting document",
                                    e
                                )
                            }
                        Toast.makeText(this, "Dépot supprimé !", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    if ((document.data.getValue("name") == markerTitle) and (document.data.getValue(
                            "creator"
                        ) != userId)
                    ) {
                        Toast.makeText(this, "Erreur : Vous n'êtes pas le créateur du dépot!", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun repRubish(markerTitle: String?, userId: String?){

        getSize {
            val strId = "report"+ String.format("%02d", count+1)
            val report = ModeratorModel(strId, markerTitle.toString(), userId.toString(), "none", false)
            val database = FirebaseDatabase
                .getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("report")
            database.child(strId).setValue(report)
            Toast.makeText(this, "Le dépôt a bien été signalé !", Toast.LENGTH_LONG).show()

            finish()
        }


    }

    private fun getSize(callback: () -> Unit){
        val database = FirebaseDatabase
            .getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("report")

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                count = 0
                for(ds in snapshot.children) {
                    count += 1
                    Log.d("COUNT", count.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        callback()
    }

}
