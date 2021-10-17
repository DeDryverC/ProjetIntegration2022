package com.example.login.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R

class CollecteAdapter(private val layoutId: Int) : RecyclerView.Adapter<CollecteAdapter.ViewHolder>(){

    // boite pour ranger les composants à controler
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        // image de la collecte
        val collecteImage = view.findViewById<ImageView>(R.id.image_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int = 5

}