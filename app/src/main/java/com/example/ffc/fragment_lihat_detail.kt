package com.example.ffc

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.ffc.db.FullData
import com.example.ffc.db.ItemFFC

/**
 * A simple [Fragment] subclass.
 */
class fragment_lihat_detail : Fragment() {

    lateinit var constraint : ConstraintLayout

    lateinit var title : TextView
    lateinit var video : VideoView
    lateinit var deskripsi : TextView
    lateinit var linear : LinearLayout
    lateinit var btnVideo : Button
    lateinit var btnSejarah : Button
    lateinit var btnPeraturan : Button
    lateinit var btnPertandingan : Button
    lateinit var backTo : ImageButton

    lateinit var dataSelect : ItemFFC

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    private val mConstraintSet = ConstraintSet()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lihat_detail, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences("save", Context.MODE_PRIVATE)!!
        val fullData = FullData.data

        constraint     = activity?.findViewById(R.id.constrain)!!

        title           = activity?.findViewById(R.id.txtTitle)!!
        video           = activity?.findViewById(R.id.viewVideo)!!
        deskripsi       = activity?.findViewById(R.id.txtDeskripsi)!!
        linear          = activity?.findViewById(R.id.linButton)!!
        btnVideo        = activity?.findViewById(R.id.btnvideo)!!
        btnSejarah      = activity?.findViewById(R.id.btnsejarah)!!
        btnPeraturan    = activity?.findViewById(R.id.btnperaturan)!!
        btnPertandingan = activity?.findViewById(R.id.btnpertandingan)!!
        backTo          = activity?.findViewById(R.id.backTo)!!

        title.text = sharedPreferences.getString("scan", "")
        fullData.forEach {
            if(it.nama.equals(title.text.toString()))
                dataSelect = it
        }

        val sv = sharedPreferences.getString("check", "")
        when (sv){
            "video"         -> videoSet()
            "sejarah"       -> sejarahSet()
            "peraturan"     -> peraturanSet()
            "pertandingan" -> pertandinganSet()
        }

        btnVideo.setOnClickListener {
            videoSet()
        }

        btnSejarah.setOnClickListener {
            sejarahSet()
        }

        btnPeraturan.setOnClickListener {
            peraturanSet()
        }

        btnPertandingan.setOnClickListener {
            pertandinganSet()
        }

        backTo.setOnClickListener{
            (activity as main_menu?)?.firstFragmentDisplay(R.id.nav_detail_pindai)
        }
    }

    fun videoSet(){
        title.text = "VIDEO"
        deskripsi.visibility = View.GONE
        video.visibility = View.VISIBLE
        btnVideo.visibility = View.GONE
        btnSejarah.visibility = View.VISIBLE
        btnPeraturan.visibility = View.VISIBLE
        btnPertandingan.visibility = View.VISIBLE

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(R.id.linButton, ConstraintSet.TOP, R.id.viewVideo, ConstraintSet.BOTTOM)
        mConstraintSet.applyTo(constraint)
    }

    fun sejarahSet(){
        title.text = "SEJARAH"
        deskripsi.visibility = View.VISIBLE
        deskripsi.text = dataSelect.sejarah
        video.visibility = View.GONE
        btnVideo.visibility = View.VISIBLE
        btnSejarah.visibility = View.GONE
        btnPeraturan.visibility = View.VISIBLE
        btnPertandingan.visibility = View.VISIBLE

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(R.id.linButton, ConstraintSet.TOP, R.id.txtDeskripsi, ConstraintSet.BOTTOM)
        mConstraintSet.applyTo(constraint)
    }

    fun peraturanSet(){
        title.text = "PERATURAN"
        deskripsi.visibility = View.VISIBLE
        deskripsi.text = dataSelect.peraturan
        video.visibility = View.GONE
        btnVideo.visibility = View.VISIBLE
        btnSejarah.visibility = View.VISIBLE
        btnPeraturan.visibility = View.GONE
        btnPertandingan.visibility = View.VISIBLE

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(R.id.linButton, ConstraintSet.TOP, R.id.txtDeskripsi, ConstraintSet.BOTTOM)
        mConstraintSet.applyTo(constraint)
    }

    fun pertandinganSet(){
        title.text = "PERTANDINGAN"
        deskripsi.visibility = View.GONE
        video.visibility = View.VISIBLE
        btnVideo.visibility = View.VISIBLE
        btnSejarah.visibility = View.VISIBLE
        btnPeraturan.visibility = View.VISIBLE
        btnPertandingan.visibility = View.GONE

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(R.id.linButton, ConstraintSet.TOP, R.id.viewVideo, ConstraintSet.BOTTOM)
        mConstraintSet.applyTo(constraint)
    }
}