package com.example.integration.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.integration.ModeratorActivity
import com.example.integration.R
import com.example.integration.model.ModeratorModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModeratorAdapter(
    private val context : ModeratorActivity,
    private val reportList : List<ModeratorModel>,
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
        holder.ModDepot.text = currentReport.nomDepot
        holder.ModUser.text = currentReport.email
        holder.ModAssignment?.text = currentReport.modAssignement
        holder.ModCheckout.setOnClickListener {
            db.collection("reports")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data.getValue("name") === currentReport.nomDepot) {
                            db.collection("reports").document(document.id).delete()
                        }
                    }

                }
        }
        holder.ModPinned.setOnClickListener{
            db.collection("reports")
                .get()
                .addOnSuccessListener { result ->
                    for(document in result) {
                        if(document.data.getValue("name") === currentReport.nomDepot){
                            val data = hashMapOf(
                                "name" to currentReport.nomDepot,
                                "user" to currentReport.email,
                                "mod" to "dedryver.cedric@gmail.com",
                                "pinned" to true
                            )
                        }
                    }
                }
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
        }


    }

    override fun getItemCount(): Int = reportList.size

}