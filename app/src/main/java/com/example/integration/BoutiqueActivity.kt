package com.example.integration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.integration.adapter.MyArticleAdapter
import com.example.integration.eventbus.UpdateCartEvent
import com.example.integration.listener.IArticleLoadListener
import com.example.integration.listener.ICartLoadListener
import com.example.integration.model.ArticleModel
import com.example.integration.model.CartModel
import com.example.integration.utils.SpaceItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_boutique.*
import kotlinx.android.synthetic.main.activity_boutique.boutique_before
import kotlinx.android.synthetic.main.activity_panier.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.auth0.android.result.UserProfile
import com.google.firebase.firestore.auth.User


class BoutiqueActivity : AppCompatActivity(), IArticleLoadListener, ICartLoadListener {

    lateinit var articleLoadListener: IArticleLoadListener
    lateinit var cartLoadListener: ICartLoadListener

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java)) {
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onUpdateCartEvent(event:UpdateCartEvent) {
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boutique)
        init()
        loadArticleFromFirebase()
        countCartFromFirebase()

    }

    private fun countCartFromFirebase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("panier-boutique")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapShot in snapshot.children) {
                        val cartModel = cartSnapShot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapShot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onCartLoadSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onCartLoadFailed(error.message)
                }

            })
    }

    private fun loadArticleFromFirebase() {
        var articleModels: MutableList<ArticleModel> = ArrayList()
        FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("articles-boutique")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(articlesSnapshot in snapshot.children) {
                            val articleModel = articlesSnapshot.getValue(ArticleModel::class.java)
                            articleModel!!.key = articlesSnapshot.key
                            articleModels.add(articleModel)
                        }
                    }
                    else {
                        articleLoadListener.onArticleLoadFailed("Cet Article n'existe pas")
                    }
                    articleLoadListener.onArticleLoadSuccess(articleModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    articleLoadListener.onArticleLoadFailed(error.message)
                }

            })
    }

    private fun init() {
        articleLoadListener = this
        cartLoadListener = this

        val gridLayoutManager = GridLayoutManager(this,2)
        recycler_articles.layoutManager = gridLayoutManager
        recycler_articles.addItemDecoration(SpaceItemDecoration())

        btnCart.setOnClickListener { startActivity(Intent(this,PanierActivity::class.java)) }
        boutique_before!!.setOnClickListener{ finish()}
    }

    override fun onArticleLoadSuccess(articleModelList: List<ArticleModel>?) {
        val adapter = MyArticleAdapter(this,articleModelList!!, cartLoadListener)
        recycler_articles.adapter = adapter

        // Affichage des points de la personne connectée
        val x = 1
        var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("clients").document("louiscarlier123@gmail.com")
            .get()
            .addOnSuccessListener {
                val points = it.data?.get("points")
                boutiquePoints.text = StringBuilder("Boutique (").append(points).append(StringBuilder(")"))}
            .addOnFailureListener{

            }
    }

    override fun onArticleLoadFailed(message: String?) {
        Snackbar.make(mainBoutique,message!!,Snackbar.LENGTH_LONG).show()
    }

    override fun onCartLoadSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for(cartModel in cartModelList!!) cartSum+= cartModel.quantity
        badge!!.setNumber(cartSum)
    }

    override fun onCartLoadFailed(message: String?) {
        Snackbar.make(mainBoutique,message!!,Snackbar.LENGTH_LONG).show()
    }

}