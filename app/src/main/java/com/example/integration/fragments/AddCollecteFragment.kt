package com.example.integration.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.integration.CollecteModel
import com.example.integration.CollecteRepository
import com.example.integration.CollecteRepository.Singleton.downloadUri
import com.example.integration.EventActivity
import com.example.integration.R
import java.util.*

class AddCollecteFragment(
    private val context: EventActivity
) : Fragment(){
    private var file: Uri? = null
    private var uploadedImage:ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_add_collecte, container, false)

        // récupérer uploadedImage pour associer composant
        uploadedImage = view.findViewById(R.id.preview_image)

        // récupérer le boutton pour charger les images
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // lorsqu'on clique dessus ca ouvre la galerie du téléphone
        pickupImageButton.setOnClickListener{
            pickupImage()
        }

        // récupérer boutton confirmer
        val confirmationButton = view.findViewById<Button>(R.id.confirm_button)
        confirmationButton.setOnClickListener { sendForm(view) }

        return view
    }

    private fun sendForm(view:View) {
        val repo = CollecteRepository()
        repo.uploadImage(file!!){
            val collecteName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val collecteDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val collecteLocalisation = view.findViewById<EditText>(R.id.adresse_input).text.toString()
            val collecteOrganisateur= view.findViewById<EditText>(R.id.organisateur_input).text.toString()
            val downloadImageUrl = downloadUri
            // créer nouvel objet collecteModel
            val collect = CollecteModel(
                collecteDescription,
                UUID.randomUUID().toString(),
                downloadImageUrl.toString(),
                false,
                collecteLocalisation,
                collecteName,
                collecteOrganisateur
            )
            // envoyer en DB
            repo.insertCollecte(collect)
        }
    }
    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 47 && resultCode == Activity.RESULT_OK){
            // vérifier si données sont nulles
            if(data == null || data.data == null) return

            // récupérer image sélectionnée
            file = data.data

            // mettre à jour l'aperçu de l'image
            uploadedImage?.setImageURI(file)

        }
    }

}