package com.example.integration


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


object Combien {


    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore
    private var nombre = 0

    fun combien(name: String): Int {
        db.collection("recompences").document(name)
            .get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "DocumentSnapshot data: ${document.data?.get("nombre")}")
                nombre = document.data?.get("nombre").toString().toInt()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        return nombre
    }
}