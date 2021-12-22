package com.example.integration


import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.Thread.sleep
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnInfoWindowCloseListener {

    private lateinit var account: Auth0
    private lateinit var mMap: GoogleMap
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null
    private var rubishCount: Int = 0
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mail = ""
    private val db = Firebase.firestore
    private val repo = ModeratorRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mail = intent.getStringExtra("key").toString()
        updateActionBar()
        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

    }


    override fun onResume() {
        super.onResume()
        updateActionBar()
    }


    private fun updateActionBar() {
        sleep(1000)
        val actionBar = supportActionBar

        val docRef = db.collection("clients").document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                val points = document.data?.getValue("points")
                actionBar!!.title =
                    mail.replaceAfter("@", "").replace("@", "") + " : $points points "
            }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Ephec and move the camera
        val ephec = LatLng(50.66586937988797, 4.61221029898094)
        val zoomLevel = 15f
        //mMap.addMarker(MarkerOptions().position(ephec).title("Ephec"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ephec, zoomLevel))
        enableMyLocation()
        addDepots()
        setMapLongClick(mMap)

        mMap.setOnInfoWindowLongClickListener(this@MapsActivity)
        mMap.setOnInfoWindowCloseListener(this@MapsActivity)
        // fenetre d'info personnalisée
        mMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
    private fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {

                }

                override fun onSuccess(payload: Void?) {
                    cachedCredentials = null
                    cachedUserProfile = null

                }

            })
    }
    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        R.id.action_collecte_listing -> {
            val intent = Intent(this, EventActivity::class.java)
            intent.putExtra("key", mail)
            // start your next activity
            startActivity(intent)
            true
        }

        R.id.action_login -> {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            logout()
            true
        }
        R.id.action_detect_trash -> {
            val intent = Intent(this, DetectActivity::class.java)
            // start your next activity
            startActivity(intent)
            true
        }
        R.id.action_profil -> {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("key", mail)
            startActivity(intent)
            true
        }

        R.id.action_boutique -> {
            val intent = Intent(this, BoutiqueActivity::class.java)
            intent.putExtra("key", mail)
            // start your next activity
            startActivity(intent)
            true
        }
        R.id.action_ticket -> {
            val intent = Intent(this, TicketActivity::class.java)
            intent.putExtra("key", mail)
            // start your next activity
            startActivity(intent)
            true
        }


        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



    private fun addDepots() {

        db.collection("depots")
            .get()
            .addOnSuccessListener { result ->
                var compteur = 0
                for (document in result) {
                    var name = "vide"
                    var desc = "vide"
                    var amount = "vide"
                    val lat = document.data.getValue("lat")
                    val long = document.data.getValue("long")
                    if (document.data.containsKey("name")) {
                        name = document.data.getValue("name").toString()
                    }
                    if (document.data.containsKey("description")) {
                        desc = document.data.getValue("description").toString()
                    }
                    if (document.data.containsKey("amount")) {
                        amount = document.data.getValue("amount").toString()
                    }
                    val latitudeAddDepot = (if (lat is String) lat.toDouble() else lat as Double)
                    val longitudeAddDepot =
                        (if (long is String) long.toDouble() else long as Double)
                    val pos = LatLng(latitudeAddDepot, longitudeAddDepot)

                    compteur += 1
                    mMap.addMarker(
                        MarkerOptions()

                            .position(pos)
                            .title(name)
                            .snippet(desc)
                            .draggable(false)
                    )
                }
                db.collection("statistique").document("depots").update("compteur", compteur.plus(1))

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }

    var depotId: String = " Depot n°$rubishCount"


    private fun setMapLongClick(map: GoogleMap) {

        map.setOnMapLongClickListener(fun(latLng: LatLng) {

            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())




            val depot = hashMapOf(
                "date" to currentDate,
                "name" to "null",
                "lat" to latLng.latitude,
                "long" to latLng.longitude,
                "creator" to mail
            )



            val action = hashMapOf(
                "action" to "depot",
                "date" to currentDate,
                "location" to "/",
                "points" to 20,
                "user" to mail
            )
            updateActionBar()

            //création du dépots dans la DB avant de lancer le formulaire
            db.collection("depots").document(latLng.latitude.toString())
                .set(depot)
            db.collection("action").document().set(action)
            //lancement de l'activité contentant le formulaire
            val intent = Intent(this, RubishCreationForm::class.java)
            intent.putExtra("key", mail)
            startActivity(intent)
        })
    }

    override fun onInfoWindowLongClick(marker: Marker) {
        val titre = marker.title.toString()
        val description = marker.snippet.toString()
        val intent = Intent(this, RubishDescription::class.java)
        intent.putExtra("MARKER_TITLE", titre)
        intent.putExtra("MARKER_DESCRIPTION", description)
        intent.putExtra("ID_USER", mail)
        startActivity(intent)
    }

    override fun onInfoWindowClose(marker: Marker) {
        //msgShow("Close Info Window")
    }

}

