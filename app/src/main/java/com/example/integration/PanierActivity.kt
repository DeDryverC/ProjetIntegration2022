package com.example.integration

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
import kotlinx.android.synthetic.main.activity_boutique.*
import kotlinx.android.synthetic.main.activity_panier.*
import kotlinx.android.synthetic.main.activity_panier.boutique_before
import kotlinx.android.synthetic.main.layout_panier_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder

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
    }

    override fun onCartLoadSuccess(cartModelList: List<CartModel>) {
        var sum = 0.0
        for(cartModel in cartModelList!!){
            sum += cartModel!!.prixTotal
        }
        txtTotal.text = StringBuilder("Total : ").append(sum.toString())
        val adapter = MyPanierAdapter(this,cartModelList)
        recycler_panier!!.adapter = adapter
    }

    override fun onCartLoadFailed(message: String?) {
        Snackbar.make(mainBoutique,message!!, Snackbar.LENGTH_LONG).show()
    }
}