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

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ticket);

        if(ContextCompat.checkSelfPermission(this@ScanActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            askCameraPermission();
        }
        else{
            setupControls();
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


    private fun switchActivity(){
        val intent = Intent(this, PointsReceivedActivity::class.java)
        startActivity(intent)
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
                val tec = hashMapOf(
                    "tec" to "SNCB"
                )

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (!document.exists()) {
                            tickets.document(code.displayValue).set(tec);
                            switchActivity();
                        } else {
                            textScanResult.text = "Ce ticket a déjà été scanné";
                        }
                    }

            }else{
                textScanResult.text=" ";
            }
        }

    }
}