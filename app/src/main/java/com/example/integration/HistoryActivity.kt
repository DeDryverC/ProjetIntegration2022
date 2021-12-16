package com.example.integration

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.adapter.HistoryDepotAdapter
import com.example.integration.adapter.HistoryTicketAdapter
import com.google.firebase.firestore.*

class HistoryActivity : AppCompatActivity() {
    private var mail = ""
    private lateinit var recyclerViewTicket: RecyclerView
    private lateinit var recyclerViewDepot: RecyclerView

    private lateinit var historyTicketArrayList: ArrayList<HistoryTicket>
    private lateinit var historyTicketAdapter: HistoryTicketAdapter

    private lateinit var historyDepotArrayList: ArrayList<HistoryDepot>
    private lateinit var historyDepotAdapter: HistoryDepotAdapter

    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mail=intent.getStringExtra("key").toString()

        recyclerViewTicket = findViewById(R.id.history_recycler)

        recyclerViewTicket.layoutManager = LinearLayoutManager(this)
        recyclerViewTicket.setHasFixedSize(true)

        //recyclerViewDepot = findViewById(R.id.history_recycler_depot)

        //recyclerViewDepot.layoutManager = LinearLayoutManager(this)
        //recyclerViewDepot.setHasFixedSize(true)

        historyTicketArrayList = arrayListOf()
        //historyDepotArrayList = arrayListOf()


        historyTicketAdapter = HistoryTicketAdapter(historyTicketArrayList)
        recyclerViewTicket.adapter = historyTicketAdapter


        //historyDepotAdapter = HistoryDepotAdapter((historyDepotArrayList))
        //recyclerViewDepot.adapter = historyDepotAdapter


        updateActionBar()
        EventChangeListener()


    }

    private fun EventChangeListener() {
        //Getting tickets from the user
        db = FirebaseFirestore.getInstance()
        db.collection("tickets")
            .whereEqualTo("user", mail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    historyTicketArrayList.add(document.toObject(HistoryTicket::class.java));
                }
                Log.d(TAG, "TEST_inside2 : ${historyTicketArrayList}")
                historyTicketAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "erreur requete base de donnée", Toast.LENGTH_SHORT).show()
            }

        //getting depots from the user
        /*
        db.collection("depots")
            .whereEqualTo("creator", mail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    historyDepotArrayList.add(document.toObject(HistoryDepot::class.java));
                }
                Log.d(TAG, "TEST_inside2 : ${historyDepotArrayList}")
                historyDepotAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "erreur requete base de donnée", Toast.LENGTH_SHORT).show()
            }
        */
    }

    private fun updateActionBar(){

        val actionBar = supportActionBar

        actionBar!!.title = mail.replaceAfter("@", "").replace("@", "")
    }

}