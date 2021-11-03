package com.example.integration

import android.net.Uri
import android.util.Log
import com.example.integration.CollecteRepository.Singleton.collecteList
import com.example.integration.CollecteRepository.Singleton.databaseRef
import com.example.integration.CollecteRepository.Singleton.downloadUri
import com.example.integration.CollecteRepository.Singleton.storageReference
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.util.*

class CollecteRepository {

    object Singleton {
        // lien vers bucket storage
        private val BUCKET_URL: String = "gs://projetintegration-83d97.appspot.com"

        // se connecte à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la référence collecte de la DB
        val databaseRef = FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app").getReference("collectes")

        // créer liste avec les collectes
        val collecteList = arrayListOf<CollecteModel>()

        // contenir lien de l'image courante
        var downloadUri: Uri
            get() {
                TODO()
            }
            set(value) {}
    }
    fun updateData(callback: () -> Unit){
        //absorber les données depuis DB
        databaseRef.addValueEventListener(object:ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les ancciennes collectes pour mettre a jour la liste
                collecteList.clear()
                // récolter la liste
                for(ds in snapshot.children){
                    //construire un objet collecte
                    val collecte = ds.getValue(CollecteModel::class.java)

                    // vérifier que la collecte existe
                    if(collecte != null){
                        // ajout de la collecte à la liste
                        collecteList.add(collecte)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    // créer fonction pour envoyer fichier sur le storage
    fun uploadImage(file:Uri, callback: () -> Unit){
        // vérifier si fichier n'est pas null
        if(file != null){
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            // demarrer envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{task ->
                // si probleme lors de l'envoi
                if(!task.isSuccessful){
                    task.exception?.let { throw it }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                // vérifier si tout a fonctionné
                if(task.isSuccessful){
                    // récup l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    fun updateCollecte(collecte:CollecteModel){
        databaseRef.child(collecte.id).setValue(collecte)
    }

    // insérer nouvelle collecte
    fun insertCollecte(collecte:CollecteModel) = databaseRef.child(collecte.id).setValue(collecte)


    // supprimer collecte de la base de données
    fun deleteCollecte(collecte:CollecteModel) = databaseRef.child(collecte.id).removeValue()
}