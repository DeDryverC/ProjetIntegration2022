package com.example.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.login.CollecteModel
import com.example.login.CollecteRepository.Singleton.collecteList
import com.example.login.EventActivity
import com.example.login.R
import com.example.login.adapter.CollecteAdapter
import com.example.login.adapter.CollecteItemDecoration

class ListingCollecteFragment(private val context: EventActivity) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_liste_collecte, container, false)

        // récupérer 1er recyclerView
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = CollecteAdapter(context, collecteList, R.layout.item_horizontal_collecte)

        // récupérer 2eme recyclerView
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = CollecteAdapter(context, collecteList, R.layout.item_vertical_collecte)
        verticalRecyclerView.addItemDecoration(CollecteItemDecoration())

        return view
    }

}