package com.example.integration

import android.util.Log
import com.example.integration.ModeratorRepository.Singleton.databaseRef
import com.example.integration.ModeratorRepository.Singleton.repList
import com.example.integration.model.ModeratorModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.security.auth.callback.Callback

class ModeratorRepository {

    object Singleton {
        val inst = "https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app"
        val databaseRef = FirebaseDatabase
            .getInstance(inst)
            .getReference("report")

        val repList = arrayListOf<ModeratorModel>()

    }


    fun updateData(callback: () -> Unit ) {
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                repList.clear()

                for(ds in snapshot.children) {

                    val report = ds.getValue(ModeratorModel::class.java)

                    Log.d("RETR", report.toString())
                    if(report != null){
                        repList.add(report)
                    }
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}