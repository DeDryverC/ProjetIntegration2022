package com.example.integration.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.ModeratorActivity
import com.example.integration.ModeratorRepository
import com.example.integration.ModeratorRepository.Singleton.databaseRef
import com.example.integration.R
import com.example.integration.model.ModeratorModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModeratorAdapter(
    private val context : ModeratorActivity,
    private val reportList : ArrayList<ModeratorModel>,
    private val layoutId: Int) : RecyclerView.Adapter<ModeratorAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ModDepot = view.findViewById<TextView>(R.id.mod_depot_name)
        val ModUser = view.findViewById<TextView>(R.id.mod_email_report)
        val ModAssignment = view.findViewById<TextView>(R.id.mod_moderator_assignment)
        val ModPinned = view.findViewById<ImageView>(R.id.pin_icon)
        val ModTrash = view.findViewById<ImageView>(R.id.trash_icon)
        val ModCheckout = view.findViewById<ImageView>(R.id.checkbox_icon)
    }


    val db = Firebase.firestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModeratorAdapter.ViewHolder, position: Int) {

        val currentReport = reportList[position]
        val repo = ModeratorRepository()

        holder.ModDepot.text = currentReport.nomDepot
        holder.ModUser.text = currentReport.email
        holder.ModAssignment?.text = currentReport.modAssignement


        holder.ModCheckout.setOnClickListener {
            databaseRef.child(currentReport.id).removeValue()
            repo.updateData {  }
        }
        holder.ModPinned.setOnClickListener{
            currentReport.pinned = !currentReport.pinned
            //currentReport.modAssignement =

            repo.updateReport(currentReport)
        }
        holder.ModTrash.setOnClickListener{
            db.collection("depot")
                .get()
                .addOnSuccessListener { result ->
                    for(document in result){
                        if(document.data.getValue("name") === currentReport.nomDepot){
                            db.collection("depot").document(document.id).delete()
                        }
                    }
                }
            databaseRef.child(currentReport.id).removeValue()
            repo.updateData {  }
        }


    }

    override fun getItemCount(): Int = reportList.size

}