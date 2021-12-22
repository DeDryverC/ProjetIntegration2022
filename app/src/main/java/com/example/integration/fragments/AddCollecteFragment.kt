package com.example.integration.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.integration.*
import com.example.integration.CollecteRepository.Singleton.downloadUri
import com.google.type.Date
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.fragment_add_collecte.*
import java.util.*

class AddCollecteFragment(
    private val context: EventActivity
) : Fragment(){
    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

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
        pickupImageButton.setOnClickListener {
            pickupImage()
        }

        // récupérer boutton confirmer
        val confirmationButton = view.findViewById<Button>(R.id.confirm_button)
        confirmationButton.setOnClickListener {
            sendForm(view)
            }

        return view
    }

    private fun sendForm(view: View) {
        val repo = CollecteRepository()
        repo.uploadImage(file!!) {
            val collecteName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val collecteDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val collecteLocalisation = view.findViewById<EditText>(R.id.adresse_input).text.toString()
            val collecteOrganisateur = view.findViewById<EditText>(R.id.organisateur_input).text.toString()
            val downloadImageUrl = downloadUri
            val collecteDate = view.findViewById<EditText>(R.id.jour_input).text.toString()
            val collecteHeure = view.findViewById<EditText>(R.id.heure_input).text.toString()
            // créer nouvel objet collecteModel
            val collect = CollecteModel(
                    collecteDescription,
                    UUID.randomUUID().toString(),
                    downloadImageUrl.toString(),
                    false,
                    collecteLocalisation,
                    collecteName,
                    collecteOrganisateur,
                    collecteDate,
                    collecteHeure
            )
            when {
                collecteName.isEmpty() -> {
                    name_input.error = "Nom requis !"
                    return@uploadImage
                }
                collecteDescription.isEmpty() -> {
                    description_input.error = "Description requise !"
                    return@uploadImage
                }
                collecteLocalisation.isEmpty() -> {
                    adresse_input.error = "Localisation requise !"
                    return@uploadImage
                }
                collecteOrganisateur.isEmpty() -> {
                    organisateur_input.error = "Organisateur requis !"
                    return@uploadImage
                }
                collecteDate.isEmpty() -> {
                    jour_input.error = "Date requise !"
                    return@uploadImage
                }
                collecteHeure.isEmpty() -> {
                    heure_input.error = "Heure requise !"
                    return@uploadImage
                }
                collecteDate.split("/")[0].toInt() !in 1..31 -> {
                    jour_input.error = "Jour incorrect ! (JJ/MM) "
                    return@uploadImage
                }
                collecteDate.split("/")[1].toInt() !in 1..12 -> {
                    jour_input.error = "Jour incorrect ! (JJ/MM) "
                    return@uploadImage
                }
                collecteHeure.split(":")[0].toInt() !in 1..24 -> {
                    heure_input.error = "Heure incorrecte ! (HH:MM) "
                    return@uploadImage
                }
                collecteHeure.split(":")[1].toInt() !in 1..60 -> {
                    heure_input.error = "Heure incorrecte ! (HH:MM) "
                    return@uploadImage
                }
                else -> {
                    // envoyer en DB
                    val dialog3 = AlertDialog.Builder(context)
                        .setTitle("Votre collecte a correctement été ajoutée !")
                        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        .create()
                    dialog3.show()
                    repo.insertCollecte(collect)


                }
            }
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
        if (requestCode == 47 && resultCode == Activity.RESULT_OK) {
            // vérifier si données sont nulles
            if (data == null || data.data == null) return

            // récupérer image sélectionnée
            file = data.data

            // mettre à jour l'aperçu de l'image
            uploadedImage?.setImageURI(file)

        }
    }
}