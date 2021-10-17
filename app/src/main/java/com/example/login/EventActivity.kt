package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.login.fragments.ListingCollecteFragment

class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // charger collecteRepository
        val repo = CollecteRepository()

        // mettre à jour la liste de collectes
        repo.updateData{
            // injecter la liste des activités (seulement images pour l'instant)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_listeCollectes, ListingCollecteFragment(this))
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}