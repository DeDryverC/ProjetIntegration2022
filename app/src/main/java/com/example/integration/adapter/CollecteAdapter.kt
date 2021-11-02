package com.example.integration.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.integration.CollecteModel
import com.example.integration.CollecteRepository
import com.example.integration.EventActivity
import com.example.integration.R

class CollecteAdapter(private val context: EventActivity,
                      private val collecteList: List<CollecteModel>,
                      private val layoutId: Int) : RecyclerView.Adapter<CollecteAdapter.ViewHolder>(){

    // boite pour ranger les composants à controler
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        // image de la collecte
        val collecteImage = view.findViewById<ImageView>(R.id.image_item)
        val collecteName:TextView? = view.findViewById(R.id.name_item)
        val collecteLocalisation:TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // récupérer informations de la collecte
        val currentCollecte = collecteList[position]

        // récupérer repository
        val repo = CollecteRepository()

        // utiliser glide pour utiliser l'url de l'image pour récupérer l'image depuis internet
        Glide.with(context).load(Uri.parse(currentCollecte.imageUrl)).into(holder.collecteImage)

        // mettre à jour le nom de la collecte
        holder.collecteName?.text = currentCollecte.nom

        // mettre à jour la description de la collecte
        holder.collecteLocalisation?.text = currentCollecte.localisation

        // vérifier si la collecte est likée ou pas
        if(currentCollecte.liked){
            holder.starIcon.setImageResource(R.drawable.ic_start)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }

        // rajouter interaction avec étoile
        holder.starIcon.setOnClickListener{
            //inverse si le bouton est liké ou non
            currentCollecte.liked = !currentCollecte.liked
            // mettre à jour la collecte
            repo.updateCollecte(currentCollecte)
        }
    }

    override fun getItemCount(): Int = collecteList.size

}