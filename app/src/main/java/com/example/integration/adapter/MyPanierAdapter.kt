package com.example.integration.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.integration.R
import com.example.integration.eventbus.UpdateCartEvent
import com.example.integration.model.CartModel
import com.example.integration.unique
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder



class MyPanierAdapter(
    private val context: Context,
    private val cartModelList:List<CartModel>
): RecyclerView.Adapter<MyPanierAdapter.MyPanierViewHolder>() {

    class MyPanierViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

        var btnMoins:ImageView?=null
        var btnPlus:ImageView?=null
        var imageView:ImageView?=null
        var btnDelete:ImageView?=null
        var txtName:TextView?=null
        var txtPrice:TextView?=null
        var txtQuantity:TextView?=null

        init {
            btnMoins = itemView.findViewById(R.id.btnMoins) as ImageView
            btnPlus = itemView.findViewById(R.id.btnPlus) as ImageView
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            btnDelete = itemView.findViewById(R.id.btnDelete) as ImageView
            txtName = itemView.findViewById(R.id.txtName) as TextView
            txtPrice = itemView.findViewById(R.id.txtPrice) as TextView
            txtQuantity = itemView.findViewById(R.id.txtQuantity) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPanierViewHolder {
        return MyPanierViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_panier_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyPanierViewHolder, position: Int) {
        Glide.with(context)
            .load(cartModelList[position].imageUrl)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(cartModelList[position].nom)
        holder.txtPrice!!.text = StringBuilder("").append(cartModelList[position].prix)
        holder.txtQuantity!!.text = StringBuilder("").append(cartModelList[position].quantity)

        holder.btnMoins!!.setOnClickListener{_ -> minusCartItem(holder,cartModelList[position])}
        holder.btnPlus!!.setOnClickListener{_ -> plusCartItem(holder,cartModelList[position])}
        holder.btnDelete!!.setOnClickListener{_ ->
            val dialog = AlertDialog.Builder(context)
                .setTitle("Supprimer cet article")
                .setMessage("Voulez-vous vraiment supprimer cet article ?")
                .setNegativeButton("RETOUR") {dialog,_ -> dialog.dismiss() }
                .setPositiveButton("SUPPRIMER") {dialog,_ ->
                    notifyItemRemoved(position)
                    FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
                        .getReference("panier-boutique")
                        .child(unique())
                        .child(cartModelList[position].key!!)
                        .removeValue()
                        .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }
                }
                .create()
            dialog.show()
        }
    }

    private fun plusCartItem(holder: MyPanierAdapter.MyPanierViewHolder, cartModel: CartModel) {
        cartModel.quantity += 1
        cartModel.prixTotal = cartModel.quantity * cartModel.prix!!.toFloat()
        holder.txtQuantity!!.text = StringBuilder("").append(cartModel.quantity)
        updateFirebase(cartModel)
    }

    private fun minusCartItem(holder: MyPanierAdapter.MyPanierViewHolder, cartModel: CartModel) {
            if(cartModel.quantity > 1) {
                cartModel.quantity -= 1
                cartModel.prixTotal = cartModel.quantity * cartModel.prix!!.toFloat()
                holder.txtQuantity!!.text = StringBuilder("").append(cartModel.quantity)
                updateFirebase(cartModel)
            }
    }

    private fun updateFirebase(cartModel: CartModel) {
        FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("panier-boutique")
            .child(unique())
            .child(cartModel.key!!)
            .setValue(cartModel)
            .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }

}
