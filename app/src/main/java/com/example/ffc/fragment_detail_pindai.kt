package com.example.ffc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.ffc.db.FullData
import kotlinx.android.synthetic.main.fragment_detail_pindai.*

/**
 * A simple [Fragment] subclass.
 */
class fragment_detail_pindai : Fragment() {

    lateinit var btnVideo : ImageButton
    lateinit var btnSejarah : ImageButton
    lateinit var btnPeraturan : ImageButton
    lateinit var btnPertandingan : ImageButton

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_pindai, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences("save", Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        titleDetail.text = sharedPreferences.getString("scan", "")
        val fullData = FullData.data
        var vid = toString()
        fullData.forEach {
            if(it.nama.equals(titleDetail.text.toString())) {
                vid = it.video
            }
        }

        btnVideo        = activity?.findViewById(R.id.btnVideo)!!
        btnSejarah      = activity?.findViewById(R.id.btnSejarah)!!
        btnPeraturan    = activity?.findViewById(R.id.btnPeraturan)!!
        btnPertandingan = activity?.findViewById(R.id.btnPertandingan)!!

        btnVideo.setOnClickListener {
            if(vid == ""){
                Toast.makeText(activity, "Video Belum Tersedia", Toast.LENGTH_SHORT).show()
            }
            else{
                editor.putString("check", "video")
                editor.apply()
                startActivity(Intent(activity, lihat_detail::class.java))
            }
        }
//
        btnSejarah.setOnClickListener {
            editor.putString("check", "sejarah")
            editor.apply()
            startActivity(Intent(activity, lihat_detail::class.java))
        }

        btnPeraturan.setOnClickListener {
            editor.putString("check", "peraturan")
            editor.apply()
            startActivity(Intent(activity, lihat_detail::class.java))
        }

        btnPertandingan.setOnClickListener {
            editor.putString("check", "pertandingan")
            editor.apply()
            startActivity(Intent(activity, lihat_detail::class.java))
        }
    }
}