package com.example.integration

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.*

class HistoryActivity : AppCompatActivity() {
    private var mail = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyArrayList: ArrayList<History>
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mail=intent.getStringExtra("key").toString()

        recyclerView = findViewById(R.id.history_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        historyArrayList = arrayListOf()
        historyAdapter = HistoryAdapter(historyArrayList)
        recyclerView.adapter = historyAdapter
        updateActionBar()
        EventChangeListener()


    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("tickets")
            .whereEqualTo("user", mail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "TEST2 : ${document.toObject(History::class.java)}")
                    historyArrayList.add(document.toObject(History::class.java));

                }
                Log.d(TAG, "TEST_inside2 : ${historyArrayList}")
                historyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "erreur requete base de donnée", Toast.LENGTH_SHORT).show()
            }
        Log.d(TAG, "TEST_outside2 : ${historyArrayList}")

    }

    private fun updateActionBar(){

        val actionBar = supportActionBar

        actionBar!!.title = mail.replaceAfter("@", "").replace("@", "")
    }

}