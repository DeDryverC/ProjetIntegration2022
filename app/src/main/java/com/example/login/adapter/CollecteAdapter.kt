package com.example.login.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.login.CollecteModel
import com.example.login.EventActivity
import com.example.login.R
import org.w3c.dom.Text

class CollecteAdapter(private val context: EventActivity,
                      private val collecteList: List<CollecteModel>,
                      private val layoutId: Int) : RecyclerView.Adapter<CollecteAdapter.ViewHolder>(){

    // boite pour ranger les composants à controler
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        // image de la collecte
        val collecteImage = view.findViewById<ImageView>(R.id.image_item)
        val collecteName:TextView? = view.findViewById(R.id.name_item)
        val collecteLocalisation:TextView? = view.findViewById(R.id.description_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // récupérer informations de la collecte
        val currentCollecte = collecteList[position]
        // utiliser glide pour utiliser l'url de l'image pour récupérer l'image depuis internet
        Glide.with(context).load(Uri.parse(currentCollecte.imageUrl)).into(holder.collecteImage)

        // mettre à jour le nom de la collecte
        holder.collecteName?.text = currentCollecte.nom

        // mettre à jjour la description de la collecte
        holder.collecteLocalisation?.text = currentCollecte.localisation
    }

    override fun getItemCount(): Int = collecteList.size

}