package com.example.integration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.CollecteRepository.Singleton.collecteList
import com.example.integration.EventActivity
import com.example.integration.R
import com.example.integration.adapter.CollecteAdapter
import com.example.integration.adapter.CollecteItemDecoration

class CollectionFragment(
    private val context: EventActivity
) : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        // récupérer recyclerview
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView.adapter = CollecteAdapter(context, collecteList.filter { it.liked }, R.layout.item_vertical_collecte)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(CollecteItemDecoration())

        return view
    }

}