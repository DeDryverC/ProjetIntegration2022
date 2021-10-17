package com.example.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.login.CollecteModel
import com.example.login.EventActivity
import com.example.login.R
import com.example.login.adapter.CollecteAdapter
import com.example.login.adapter.CollecteItemDecoration

class ListingCollecteFragment(private val context: EventActivity) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_liste_collecte, container, false)

        // créer liste qui va stocker collectes
        val collecteList = arrayListOf<CollecteModel>()

        // enregistrer premiere collecte (BLC)
        collecteList.add(
            CollecteModel(
            "Collecte BLC",
            "Braine-le-Comte",
            "https://cdn.pixabay.com/photo/2016/11/21/15/42/disposal-1846033_960_720.jpg",
            "Arthur Schamroth",
            "Privé"
        ))

        // enregistrer deuxieme collecte (LLN)
        collecteList.add(
            CollecteModel(
                "Collecte LLN",
                "Louvain-la-Neuve",
                "https://cdn.pixabay.com/photo/2016/06/21/15/55/nuclear-waste-1471361_960_720.jpg",
                "Arthur Schamroth",
                "Public"
            ))

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