package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.login.fragments.ListingCollecteFragment

class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // injecter la liste des activités (seulement images pour l'instant)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_listeCollectes, ListingCollecteFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}