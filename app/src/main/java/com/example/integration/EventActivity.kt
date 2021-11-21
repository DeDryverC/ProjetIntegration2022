package com.example.integration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.integration.fragments.AddCollecteFragment
import com.example.integration.fragments.CollectionFragment
import com.example.integration.fragments.ListingCollecteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        loadFragment(ListingCollecteFragment(this))

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
                    loadFragment(AddCollecteFragment(this))
                    return@setOnNavigationItemReselectedListener
                    true
                }
                else -> false
            }
        }
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
}