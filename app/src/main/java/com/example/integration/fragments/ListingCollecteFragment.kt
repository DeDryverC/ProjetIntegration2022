package com.example.integration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.CollecteRepository.Singleton.collecteList
import com.example.integration.EventActivity
import com.example.integration.R
import com.example.integration.adapter.CollecteAdapter
import com.example.integration.adapter.CollecteItemDecoration

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