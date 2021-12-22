package com.example.integration

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.integration.fragments.AddCollecteFragment
import com.example.integration.fragments.CollectionFragment
import com.example.integration.fragments.ListingCollecteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class EventActivity : AppCompatActivity() {
    private var mail = ""
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        mail = intent.getStringExtra("key").toString()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        loadFragment(ListingCollecteFragment(this))
        val bouton_retour = findViewById<ImageView>(R.id.collecte_before)
        bouton_retour.setOnClickListener { finish() }
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.collecte_page-> {
                    loadFragment(ListingCollecteFragment(this))
                    return@setOnNavigationItemReselectedListener
                    true
                }
                R.id.collection_page -> {
                    loadFragment(CollectionFragment(this))
                    return@setOnNavigationItemReselectedListener
                    true
                }
                R.id.add_collecte_page -> {
                    plusUn(mail,"EventActivity")
                    loadFragment(AddCollecteFragment(this))
                    return@setOnNavigationItemReselectedListener
                }
                else -> false
            }
        }
        val actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra("key").toString().replaceAfter("@", "").replace("@", "")
    }

    private fun loadFragment(fragment: Fragment) {
        val repo = CollecteRepository()

        // mettre à jour la liste de collectes
        repo.updateData{
            // injecter la liste des activités (seulement images pour l'instant)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_listeCollectes, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    private fun plusUn(mail: String, name: String) {
        val db2 = db.collection("clients").document(mail)
        var newScore: Int
        db.runTransaction { transaction ->
            val snapshot = transaction.get(db2)
            newScore = (snapshot.getDouble("points")!! + Combien.combien(name)).toInt()
            transaction.update(db2, "points", newScore)
        }

    }
}