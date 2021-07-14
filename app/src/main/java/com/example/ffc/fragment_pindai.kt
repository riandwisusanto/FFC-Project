package com.example.ffc

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.example.ffc.db.FullData
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_pindai.*


/**
 * A simple [Fragment] subclass.
 */
class fragment_pindai : Fragment() {
    private lateinit var codeScanner: CodeScanner
    var multi = MultiFormatWriter()
    lateinit var btn_selengkapnya : Button

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pindai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences("save", Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        btn_selengkapnya = activity?.findViewById(R.id.btnSelengkapnya)!!
        btn_selengkapnya.visibility = View.INVISIBLE

        btn_selengkapnya.setOnClickListener {
            editor.putString("scan", tv_text.text.toString())
            editor.apply()
            (activity as main_menu?)?.firstFragmentDisplay(R.id.nav_detail_pindai)
        }

        setupPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this.requireContext(), scn)
        val fullData = FullData.data
        val imgPreview = activity?.findViewById<ImageView>(R.id.imgPreview)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                activity?.runOnUiThread {
                    val txtScan = it.text

                    fullData.forEach {
                        if(it.nama.equals(txtScan)){
                            tv_text.text = txtScan
                            btn_selengkapnya.visibility = View.VISIBLE
                            try {
                                val bitMatrix: BitMatrix = multi.encode(
                                    txtScan,
                                    BarcodeFormat.QR_CODE,
                                    150,
                                    150
                                )
                                val barcodeEncoder = BarcodeEncoder()
                                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                                imgPreview?.setImageBitmap(bitmap)
                            } catch (e: WriterException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

            errorCallback = ErrorCallback {
                activity?.runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this.requireContext(),
            android.Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this.requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this.context,
                        "Memerlukan izin kamera untuk menggunakan aplikasi ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}