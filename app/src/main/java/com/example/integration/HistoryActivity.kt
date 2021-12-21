package com.example.integration

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.adapter.HistoryActionAdapter
import com.google.firebase.firestore.*

class HistoryActivity : AppCompatActivity() {
    private var mail = ""
    private lateinit var recyclerViewAction: RecyclerView

    private lateinit var historyActionArrayList: ArrayList<HistoryAction>
    private lateinit var historyActionAdapter: HistoryActionAdapter


    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mail=intent.getStringExtra("key").toString()

        recyclerViewAction = findViewById(R.id.history_recycler)

        recyclerViewAction.layoutManager = LinearLayoutManager(this)
        recyclerViewAction.setHasFixedSize(true)


        historyActionArrayList = arrayListOf()


        historyActionAdapter = HistoryActionAdapter(historyActionArrayList)
        recyclerViewAction.adapter = historyActionAdapter


        updateActionBar()
        EventChangeListener()
        if (historyActionArrayList.isEmpty()) {
            setContentView(R.layout.activity_history_empty);
        }


    }

    private fun EventChangeListener() {
        //Getting tickets from the user
        db = FirebaseFirestore.getInstance()
        db.collection("action")
            .whereEqualTo("user", mail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    historyActionArrayList.add(document.toObject(HistoryAction::class.java));
                }
                Log.d(TAG, "TEST_inside2 : ${historyActionArrayList}")
                historyActionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "erreur requete base de donnée", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateActionBar(){

        val actionBar = supportActionBar

        actionBar!!.title = mail.replaceAfter("@", "").replace("@", "")
    }

}