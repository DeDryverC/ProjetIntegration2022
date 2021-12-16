package com.example.integration

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.adapter.ModeratorAdapter
import com.example.integration.fragments.ModeratorFragment
import com.example.integration.model.ModeratorModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModeratorActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private val reportList = arrayListOf<ModeratorModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moderator)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, ModeratorFragment(this))
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
