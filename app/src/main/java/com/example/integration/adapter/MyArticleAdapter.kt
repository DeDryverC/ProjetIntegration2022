package com.example.integration.adapter

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
import com.example.integration.listener.ICartLoadListener
import com.example.integration.listener.IRecyclerClickListener
import com.example.integration.model.ArticleModel
import com.example.integration.model.CartModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder

class MyArticleAdapter(
    private val context: Context,
    private val list:List<ArticleModel>,
    private val cartListener: ICartLoadListener
): RecyclerView.Adapter<MyArticleAdapter.MyArticleViewHolder>() {

    class MyArticleViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView:ImageView?=null
        var txtName:TextView?=null
        var txtPrice:TextView?=null

        private var clickListener: IRecyclerClickListener? = null

        fun setClickListener(clickListener: IRecyclerClickListener)
        {
            this.clickListener = clickListener;
        }

        init {
                imageView = itemView.findViewById(R.id.imageView) as ImageView;
                txtName = itemView.findViewById(R.id.txtName) as TextView;
                txtPrice = itemView.findViewById(R.id.txtPrice) as TextView;

                itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyArticleViewHolder {
        return MyArticleViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_article_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: MyArticleViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position].imageUrl)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(list[position].nom)
        holder.txtPrice!!.text = StringBuilder("$").append(list[position].prix)

        holder.setClickListener(object:IRecyclerClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                    addToCart(list[position])
            }

        })
    }

    private fun addToCart(articleModel: ArticleModel) {
        val userCart = FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("panier-boutique")
            .child("UNIQUE_USER_ID")

        userCart.child(articleModel.key!!)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) { // Si un article est déjà dans le panier, met juste à jour
                        val cartModel =  snapshot.getValue(CartModel::class.java)
                        val updateData: MutableMap<String,Any> = HashMap()
                        cartModel!!.quantity = cartModel!!.quantity+1
                        updateData["quantity"] = cartModel!!.quantity
                        updateData["Prix Total"] = cartModel!!.quantity * cartModel.prix!!.toFloat()

                         userCart.child(articleModel.key!!)
                             .updateChildren(updateData)
                             .addOnSuccessListener {
                                 EventBus.getDefault().postSticky(UpdateCartEvent())
                                 cartListener.onCartLoadFailed("Article ajouté au panier")
                             }
                             .addOnFailureListener{ e->cartListener.onCartLoadFailed(e.message) }
                    }
                    else{ // Si il n'y a pas d'article dans le panier, en ajoute un nouveau
                        val cartModel = CartModel()
                        cartModel.key = articleModel.key
                        cartModel.nom = articleModel.nom
                        cartModel.imageUrl = articleModel.imageUrl
                        cartModel.prix = articleModel.prix
                        cartModel.quantity = 1
                        cartModel.prixTotal = articleModel.prix!!.toFloat()

                        userCart.child(articleModel.key!!)
                            .setValue(cartModel)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListener.onCartLoadFailed("Article ajouté au panier")
                            }
                            .addOnFailureListener{ e->cartListener.onCartLoadFailed(e.message) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartListener.onCartLoadFailed(error.message)
                }

            })
    }

    override fun getItemCount(): Int {
        return list.size
    }

}