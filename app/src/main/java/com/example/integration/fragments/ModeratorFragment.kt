package com.example.integration.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.ModeratorActivity
import com.example.integration.ModeratorRepository.Singleton.repList
import com.example.integration.R
import com.example.integration.adapter.ModeratorAdapter
import com.example.integration.model.ModeratorModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModeratorFragment(private val context: ModeratorActivity, private val name : String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moderator, container, false)

        Log.d("REPLIST", repList.toString())
        val modRecyclerView = view.findViewById<RecyclerView>(R.id.mod_recycle_view)
        modRecyclerView.adapter = ModeratorAdapter(context, repList, name,  R.layout.layout_moderator_report)

        return view
    }
}