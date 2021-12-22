package com.example.integration

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioDeviceCallback
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.integration.R.layout.activity_detect
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class DetectActivity : AppCompatActivity() {
    val REQUEST_CODE = 2000
    private lateinit var imageView: ImageView
    private lateinit var btnEnvoi: Button
    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

    override fun onCreate(savedInstance: Bundle?){
        super.onCreate(savedInstance)
        setContentView(activity_detect)
        val bouton_retour = findViewById<ImageView>(R.id.collecte_before)
        bouton_retour.setOnClickListener { finish() }
        val bouton_galerie = findViewById<Button>(R.id.button_galerie)
        bouton_galerie.setOnClickListener { pickupImage() }
        uploadedImage = findViewById<ImageView>(R.id.imageView2)

        imageView = findViewById<ImageView>(R.id.imageView2)

        btnEnvoi = findViewById<Button>(R.id.button_envoi)
        btnEnvoi.setOnClickListener {
            uploadImage(file!!)
        }

    }

    private fun uploadImage(file:Uri) {
        // vérifier si fichier n'est pas null
        if(file != null){
            val fileName = "data.jpg"
            val ref = CollecteRepository.Singleton.storageReferencesignal.child(fileName)
            val uploadTask = ref.putFile(file)

            // demarrer envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                // si probleme lors de l'envoi
                if(!task.isSuccessful){
                    task.exception?.let { throw it }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                // vérifier si tout a fonctionné
                if(task.isSuccessful){
                    // récup l'image
                    CollecteRepository.Singleton.downloadUri = task.result
                    val dialog3 = AlertDialog.Builder(this)
                        .setTitle("Photo Envoyée !")
                        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                        .create()
                    dialog3.show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            imageView.setImageBitmap(data.extras!!.get("data") as Bitmap)
        }
        if (requestCode == 47 && resultCode == Activity.RESULT_OK) {
            // vérifier si données sont nulles
            if (data == null || data.data == null) return

            // récupérer image sélectionnée
            file = data.data

            // mettre à jour l'aperçu de l'image
            uploadedImage?.setImageURI(file)
        }
    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }
}