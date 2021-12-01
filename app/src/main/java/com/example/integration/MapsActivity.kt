package com.example.integration


import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnInfoWindowCloseListener {

    private lateinit var mMap: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mail = ""


    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val extras = intent.extras
        mail=intent.getStringExtra("key").toString()

    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            val depot = hashMapOf(
                "name" to "Depots",
                "lat" to latLng.latitude,
                "long" to latLng.longitude,
                "long" to latLng.longitude,
                "description" to "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ullamcorper accumsan porta. Nulla facilisi."
            )
            db.collection("depots").document("" + latLng.latitude)
                .set(depot)
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Decharge")
                    .snippet(snippet)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            )
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Ephec and move the camera
        val ephec = LatLng(50.66586937988797, 4.61221029898094)
        val zoomLevel = 15f
        //mMap.addMarker(MarkerOptions().position(ephec).title("Ephec"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ephec, zoomLevel))
        setMapLongClick(mMap)
        enableMyLocation()

        addDepots(mMap)

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

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        R.id.action_collecte_listing -> {
            val intent = Intent(this, EventActivity::class.java)
            // start your next activity
            startActivity(intent)
            true
        }
        R.id.action_classement -> {
            val intent = Intent(this, ClassementActivity::class.java)
            // start your next activity
            startActivity(intent)

            true
        }
        R.id.action_login -> {
            val intent = Intent(this, LoginActivity::class.java)
            // start your next activity
            startActivity(intent)
            true
        }
        R.id.action_boutique -> {
            val intent = Intent(this, BoutiqueActivity::class.java)
            intent.putExtra("key",mail)
            // start your next activity
            startActivity(intent)
            true
        }
        R.id.action_ticket -> {
            val intent = Intent(this, TicketActivity::class.java)
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

    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun addDepots(googleMap: GoogleMap) {

        db.collection("depots")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val lat = document.data.getValue("lat")
                    val long = document.data.getValue("long")
                    val name = document.data.getValue("name")
                    val pos = LatLng(lat as Double, long as Double)
                    val desc = document.data.getValue("description")

                    mMap.addMarker(
                        MarkerOptions()

                            .position(pos)
                            .title(name as String)
                            .snippet(desc as String)
                            .draggable(true)
                    )

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    override fun onInfoWindowLongClick(marker: Marker) {
        val intent = Intent(this, RubishDescription::class.java)
        intent.putExtra("MARKER_TITLE", marker.title)
        intent.putExtra("MARKER_DESCRIPTION", marker.snippet)
        startActivity(intent)
    }

    override fun onInfoWindowClose(marker: Marker) {
        msgShow("Close Info Window")
    }
}


