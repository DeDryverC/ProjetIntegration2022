package com.example.integration

import android.app.Dialog
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.integration.adapter.CollecteAdapter

class CollectePopup(private val adapter: CollecteAdapter,
                    private val currentCollecte: CollecteModel)
    : Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_collecte_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()
    }

    private fun updateStar(button: ImageView){
        if(currentCollecte.liked){
            button.setImageResource(R.drawable.ic_start)
        }
        else{
            button.setImageResource(R.drawable.ic_unstar)
        }
    }

    private fun setupStarButton() {
        // récupérer
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)

        // interaction
        starButton.setOnClickListener{
            currentCollecte.liked = !currentCollecte.liked
            val repo = CollecteRepository()
            repo.updateCollecte(currentCollecte)
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener{
            // supprimer collecte
            val repo = CollecteRepository()
            repo.deleteCollecte(currentCollecte)
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener{
            // fermer la fenetre popup
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualiser l'image de la collecte
        val collecteImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentCollecte.imageUrl)).into(collecteImage)

        // actualiser nom
        findViewById<TextView>(R.id.popup_collecte_name).text = currentCollecte.nom

        // description
        findViewById<TextView>(R.id.popup_collecte_description_subtitle).text = currentCollecte.description

        // localité
        findViewById<TextView>(R.id.popup_collecte_localite).text = currentCollecte.localisation

        // organisateur
        findViewById<TextView>(R.id.popup_collecte_organisateur_subtitle).text = currentCollecte.organisateur

        // date
        findViewById<TextView>(R.id.popup_collecte_date_subtitle).text = currentCollecte.date

        // heure
        findViewById<TextView>(R.id.popup_collecte_heure_subtitle).text = currentCollecte.heure
    }

}