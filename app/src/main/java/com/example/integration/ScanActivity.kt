package com.example.integration

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.budiyev.android.codescanner.*
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_scan_ticket.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception
import android.content.Intent as Intent


private const val CAMERA_REQUEST_CODE = 101;

class ScanActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001;
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector
    val db = FirebaseFirestore.getInstance();
    val tickets = db.collection("tickets");
    private var mail = ""

    var class_spinner : Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ticket);

        mail=intent.getStringExtra("key").toString()
        val spinner: Spinner = findViewById(R.id.spinner_tec_scan)
        class_spinner = spinner
        if(ContextCompat.checkSelfPermission(this@ScanActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            askCameraPermission();
        }
        else{
            setupControls();
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.tec_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

    }

    private fun setupControls(){
        detector = BarcodeDetector.Builder(this@ScanActivity).build();
        cameraSource = CameraSource.Builder(this@ScanActivity, detector).setAutoFocusEnabled(true).build();
        cameraSurfaceView.holder.addCallback(surfaceCallback);
        detector.setProcessor(processor);
    }

    private fun askCameraPermission(){
        ActivityCompat.requestPermissions(this@ScanActivity, arrayOf(Manifest.permission.CAMERA), requestCodeCameraPermission);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupControls();
            }
            else{
                Toast.makeText(applicationContext, "Permission refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val surfaceCallback = object : SurfaceHolder.Callback{
        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            try{
                if (ActivityCompat.checkSelfPermission(
                        this@ScanActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                cameraSource.start(surfaceHolder)
            }catch(exception: Exception){
                Toast.makeText(applicationContext, "Quelque chose s'est mal passé", Toast.LENGTH_SHORT).show();
            }
        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            cameraSource.stop()
        }

    }


    private fun switchActivityPointsReceived(){
        val intent = Intent(this, PointsReceivedActivity::class.java)
        intent.putExtra("key",mail)
        startActivity(intent)
    }

    private fun alreadyScanned(){
        val intent = Intent(this, TicketActivity::class.java)
        intent.putExtra("key",mail)
        startActivity(intent)
        Toast.makeText(this, "Ce ticket a déjà été scanné", Toast.LENGTH_SHORT).show()
    }
    private fun getSpinnerValue(){

    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {

        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if(detections != null && detections.detectedItems.isNotEmpty()){
                val qrCodes: SparseArray<Barcode> = detections.detectedItems;
                val code = qrCodes.valueAt(0);
                textScanResult.text = code.displayValue;
                val docRef=db.collection("tickets").document(code.displayValue)
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())

                val spinner_value = class_spinner?.selectedItem.toString()
                val tec = hashMapOf(
                    "action" to "ticket",
                    "tec" to spinner_value,
                    "user" to mail,
                    "date" to currentDate,
                    "points" to 1
                )

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (!document.exists()) {
                            detector.release();
                            tickets.document(code.displayValue).set(tec);
                            switchActivityPointsReceived();
                        } else {
                            detector.release();
                            alreadyScanned();
                        }
                    }

            }else{
                textScanResult.text=" ";
            }
        }

    }


}