package com.example.login

import com.example.login.CollecteRepository.Singleton.databaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CollecteRepository {

    object Singleton{
        // se connecter à la référence collecte de la DB
        val databaseRef = FirebaseDatabase.getInstance().getReference("collectes")
        // créer liste avec les collectes
        val collecteList = arrayListOf<CollecteModel>()

}}