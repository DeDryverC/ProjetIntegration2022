package com.example.integration

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.integration.adapter.MyPanierAdapter
import com.example.integration.eventbus.UpdateCartEvent
import com.example.integration.listener.ICartLoadListener
import com.example.integration.model.CartModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_boutique.*
import kotlinx.android.synthetic.main.activity_panier.*
import kotlinx.android.synthetic.main.activity_panier.boutique_before
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.NumberFormatException
import java.lang.StringBuilder
import java.util.function.LongToDoubleFunction

class PanierActivity : AppCompatActivity(), ICartLoadListener {

    var cartLoadListener:ICartLoadListener?=null

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
    fun onUpdateCartEvent(event: UpdateCartEvent) {
        loadCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panier)
        init()
        loadCartFromFirebase()

        val actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra("key").toString().replaceAfter("@", "").replace("@", "")
    }

    private fun loadCartFromFirebase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("panier-boutique")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapShot in snapshot.children) {
                        val cartModel = cartSnapShot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapShot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener!!.onCartLoadSuccess(cartModels)
                    btnPay.setOnClickListener { payCart(cartModels) }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onCartLoadFailed(error.message)
                }

            })
    }

    private fun init() {
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(this)
        recycler_panier!!.layoutManager = layoutManager
        recycler_panier!!.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        boutique_before!!.setOnClickListener{ finish()}

        // Appelle la fonction payCart pour le payement
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("panier-boutique")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapShot in snapshot.children) {
                        val cartModel = cartSnapShot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapShot.key
                        cartModels.add(cartModel)
                    }
                    btnPay.setOnClickListener { payCart(cartModels) }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onCartLoadFailed(error.message)
                }

            })
    }

    override fun onCartLoadSuccess(cartModelList: List<CartModel>) {
        var sum = 0.0
        for(cartModel in cartModelList!!){
            sum += cartModel!!.prixTotal
        }
        txtTotal.text = StringBuilder("Total : ").append(sum.toString().replaceAfter(".", "").replace(".", ""))
        val adapter = MyPanierAdapter(this,cartModelList)
        recycler_panier!!.adapter = adapter
    }

    override fun onCartLoadFailed(message: String?) {
        Snackbar.make(mainBoutique,message!!, Snackbar.LENGTH_LONG).show()
    }


    //Bouttton "dépenser mes points"
    private fun payCart(cartModelList: List<CartModel>) {
        val mail = intent.getStringExtra("key").toString()
        val intent = Intent(this,MapsActivity::class.java)
        intent.putExtra("key",mail)

        var sum = 0.0
        for(cartModel in cartModelList!!){
            sum += cartModel!!.prixTotal
        }

        var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (sum.toInt() == 0) {
            val dialog4 = AlertDialog.Builder(this)
                .setTitle("Votre Panier est vide !")
                .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                .create()
            dialog4.show()
        }
        else {
            val sumValidate = StringBuilder("Valider pour :\n").append(
                sum.toString()
                    .replaceAfter(".", "").replace(".", "").plus(" points")
            )
            val dialog = AlertDialog.Builder(this)
                .setTitle("Acheter cette contrepartie ?")
                .setMessage("Voulez vous vraiment valider votre achat ?")
                .setNegativeButton("Annuler") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(sumValidate) { _, _ ->


                    firestore.collection("clients").document(mail)
                        .get()
                        .addOnSuccessListener {
                            var points = it.data?.get("points").toString()
                            var x = points.toDouble()
                            if (x >= sum) {
                                firestore.collection("clients").document(mail).update(
                                    "points", (x - sum).toString()
                                        .replaceAfter(".", "").replace(".", "")
                                )

                                FirebaseDatabase.getInstance("https://projetintegration-83d97-default-rtdb.europe-west1.firebasedatabase.app")
                                    .getReference("panier-boutique")
                                    .child("UNIQUE_USER_ID")
                                    .removeValue()

                                val dialog2 = AlertDialog.Builder(this)
                                    .setTitle("Achat bien effectué !")
                                    .setPositiveButton("Retourner à la Carte") { _, _ ->
                                        startActivity(intent)
                                    }
                                    .create()
                                dialog2.show()
                            } else {
                                val dialog3 = AlertDialog.Builder(this)
                                    .setTitle("Solde insuffisant")
                                    .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                                    .create()
                                dialog3.show()

                            }
                        }
                        .addOnFailureListener {

                        }


                }
                .create()
            dialog.show()
        }
    }


}