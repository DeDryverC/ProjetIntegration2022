package com.example.integration

import android.util.Log
import com.example.integration.CollecteRepository.Singleton.collecteList
import com.example.integration.CollecteRepository.Singleton.databaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CollecteRepository {

    object Singleton {
        // se connecter à la référence collecte de la DB
        val databaseRef = FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app").getReference("collectes")

        // créer liste avec les collectes
        val collecteList = arrayListOf<CollecteModel>()
    }
    fun updateData(callback: () -> Unit){
        println("bonjour")
        //absorber les données depuis DB
        databaseRef.addValueEventListener(object:ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les ancciennes collectes pour mettre a jour la liste
                collecteList.clear()
                // récolter la liste
                for(ds in snapshot.children){
                    //construire un objet collecte
                    val collecte = ds.getValue(CollecteModel::class.java)

                    // vérifier que la collecte existe
                    if(collecte != null){
                        // ajout de la collecte à la liste
                        collecteList.add(collecte)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun updateCollecte(collecte:CollecteModel){
        databaseRef.child(collecte.id).setValue(collecte)
    }
}