package com.example.ffc

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    lateinit var linearTop: LinearLayout
    lateinit var linearBottom: LinearLayout
    lateinit var btn_cek : Button
    lateinit var txt_display: TextView

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    private lateinit var input: Array<TextView>
    lateinit var scan_string: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pindai, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences("save", Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        txt_display = activity?.findViewById(R.id.tv_text)!!

        input = arrayOf(
            requireActivity().findViewById(R.id.input1),
            requireActivity().findViewById(R.id.input2),
            requireActivity().findViewById(R.id.input3),
            requireActivity().findViewById(R.id.input4),
            requireActivity().findViewById(R.id.input5),
            requireActivity().findViewById(R.id.input6),
            requireActivity().findViewById(R.id.input7),
            requireActivity().findViewById(R.id.input8),
            requireActivity().findViewById(R.id.input9),
            requireActivity().findViewById(R.id.input10),
            requireActivity().findViewById(R.id.input11),
            requireActivity().findViewById(R.id.input12),
            requireActivity().findViewById(R.id.input13),
            requireActivity().findViewById(R.id.input14),
            requireActivity().findViewById(R.id.input15),
            requireActivity().findViewById(R.id.input16),
            requireActivity().findViewById(R.id.input17),
            requireActivity().findViewById(R.id.input18),
            requireActivity().findViewById(R.id.input19),
            requireActivity().findViewById(R.id.input20),
            requireActivity().findViewById(R.id.input21),
            requireActivity().findViewById(R.id.input22),
            requireActivity().findViewById(R.id.input23),
            requireActivity().findViewById(R.id.input24),
            requireActivity().findViewById(R.id.input25),
            requireActivity().findViewById(R.id.input26)
        )

        input.forEach {
            val editFilters = it.filters
            val newFilters = arrayOfNulls<InputFilter>(editFilters.size + 1)
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.size)
            newFilters[editFilters.size] = InputFilter.AllCaps()
            it.filters = newFilters
        }

        setupPermissions()
        codeScanner()
        scan_string = ""

        linearTop = requireActivity().findViewById(R.id.linTop)
        linearBottom = requireActivity().findViewById(R.id.linBot)
        btn_cek = requireActivity().findViewById(R.id.cek)

        linearTop.visibility = View.INVISIBLE
        linearBottom.visibility = View.INVISIBLE
        btn_cek.visibility = View.INVISIBLE

        btn_cek.setOnClickListener {
            cekText()
        }
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this.requireContext(), scn)
        val fullData = FullData.data

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
                        if(it.id.equals(txtScan)){
                            txt_display.text = it.nama
                            txt_display.visibility = View.INVISIBLE
                            setText(it.tebak)
                            onSet()

                            scan_string = it.nama.toUpperCase()
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

    private fun onSet(){
        for (i in input.indices){
            input[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (i < input.size - 1) {
                        if (!s.toString().trim().isEmpty()) {
                            if (!input[i + 1].isEnabled) {
                                for (j in i + 1..input.size - 1) {
                                    if (input[j].isEnabled) {
                                        input[j].requestFocus()
                                        break
                                    }
                                }
                            } else
                                input[i + 1].requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            if(i > 0){
                input[i].setOnKeyListener(object : View.OnKeyListener {
                    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                        if (input[i].text.toString().trim().isEmpty() && keyCode == KeyEvent.KEYCODE_DEL) {
                            if (!input[i - 1].isEnabled) {
                                var j = i - 1
                                while (j >= 0){
                                    if (input[j].isEnabled) {
                                        input[j].requestFocus()
                                        break
                                    }
                                    j--
                                }
                            } else
                                input[i - 1].requestFocus()
                        }
                        return false
                    }
                })
            }
        }
    }

    private fun setText(string: String){
        input.forEach {
            it.isEnabled = true
            it.text = ""
            it.visibility = View.VISIBLE
        }

        linearTop.visibility = View.VISIBLE
        linearBottom.visibility = View.VISIBLE
        btn_cek.visibility = View.VISIBLE

        val str = string.toCharArray()
        var iTemp = 0
        str.forEach{
            if(it != '.' && it != '_') {
                input[iTemp].text = it.toString()
                input[iTemp].isEnabled = false
            }
            iTemp++

            if(it == '_') {
                for (i in iTemp..12)
                    input[i].visibility = View.GONE
                iTemp = 13
            }

            if(it == '#') {
                for (j in (iTemp - 1)..25)
                    input[j].visibility = View.GONE
            }
        }
    }

    private fun cekText(){
        var true_text = ""
        input.forEach {
            if(it.visibility != View.GONE) {
                var text = "-"
                if(!it.text.toString().isEmpty())
                    text = it.text.toString()
                true_text = true_text + text
            }
        }

        if(true_text.equals(scan_string)){
            editor.putString("scan", tv_text.text.toString())
            editor.apply()
            (activity as main_menu?)?.firstFragmentDisplay(R.id.nav_detail_pindai)
        }
        else
            Toast.makeText(
                this.context,
                "Upss! Salah",
                Toast.LENGTH_SHORT
            ).show()
    }
}