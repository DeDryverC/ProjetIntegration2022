package com.example.integration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
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
        db.collection("tickets").addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("firestore error", error.message.toString())
                    return
                }

                for(dc: DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        historyArrayList.add(dc.document.toObject(History::class.java))
                    }
                }

                historyAdapter.notifyDataSetChanged()
            }

        })
    }

    private fun updateActionBar(){

        val actionBar = supportActionBar

        actionBar!!.title = mail.replaceAfter("@", "").replace("@", "")
    }

}