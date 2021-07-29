package com.example.ffc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.ffc.db.FullData
import com.example.ffc.db.ItemFFC
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView


class lihat_detail : YouTubeBaseActivity() {

    lateinit var constraint : ConstraintLayout

    lateinit var title : TextView
    lateinit var video : YouTubePlayerView
    lateinit var deskripsi : TextView
    lateinit var linear : LinearLayout
    lateinit var btnVideo : Button
    lateinit var btnSejarah : Button
    lateinit var btnPeraturan : Button
    lateinit var btnPertandingan : Button
    lateinit var backTo : ImageButton
    var youTubePlayerMain: YouTubePlayer? = null

    lateinit var dataSelect : ItemFFC

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    private val mConstraintSet = ConstraintSet()

    val api_key = "AIzaSyATcXj3MMmC9L2UrUL2EZtUANeRL7iYlEM"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_detail)

        sharedPreferences = getSharedPreferences("save", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val fullData = FullData.data

        constraint     = findViewById(R.id.constrain2)!!

        title           = findViewById(R.id.txtTitle)!!
        video           = findViewById(R.id.viewVideo)!!
        deskripsi       = findViewById(R.id.txtDeskripsi)!!
        linear          = findViewById(R.id.linButton)!!
        btnVideo        = findViewById(R.id.btnvideo)!!
        btnSejarah      = findViewById(R.id.btnsejarah)!!
        btnPeraturan    = findViewById(R.id.btnperaturan)!!
        btnPertandingan = findViewById(R.id.btnpertandingan)!!
        backTo          = findViewById(R.id.backTo)!!

        title.text = sharedPreferences.getString("scan", "")
        fullData.forEach {
            if(it.nama.equals(title.text.toString()))
                dataSelect = it
        }

        val sv = sharedPreferences.getString("check", "")
        when (sv){
            "video" -> videoSet()
            "sejarah" -> sejarahSet()
            "peraturan" -> peraturanSet()
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
            editor.putString("isBackDetail", "true")
            editor.apply()
            startActivity(Intent(this, main_menu::class.java))
            finish()
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

        init(dataSelect.video)

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(
            R.id.linButton,
            ConstraintSet.TOP,
            R.id.viewVideo,
            ConstraintSet.BOTTOM
        )
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

        if (youTubePlayerMain != null)
            youTubePlayerMain!!.pause()

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(
            R.id.linButton,
            ConstraintSet.TOP,
            R.id.txtDeskripsi,
            ConstraintSet.BOTTOM
        )
        mConstraintSet.applyTo(constraint)

        if(dataSelect.video == "")
            btnVideo.visibility = View.GONE
    }

    fun peraturanSet(){
        title.text = "PERATURAN"
        deskripsi.visibility = View.VISIBLE
        deskripsi.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(dataSelect.peraturan, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(dataSelect.peraturan)
        }
        video.visibility = View.GONE
        btnVideo.visibility = View.VISIBLE
        btnSejarah.visibility = View.VISIBLE
        btnPeraturan.visibility = View.GONE
        btnPertandingan.visibility = View.VISIBLE

        if (youTubePlayerMain != null)
            youTubePlayerMain!!.pause()

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(
            R.id.linButton,
            ConstraintSet.TOP,
            R.id.txtDeskripsi,
            ConstraintSet.BOTTOM
        )
        mConstraintSet.applyTo(constraint)

        if(dataSelect.video == "")
            btnVideo.visibility = View.GONE
    }

    fun pertandinganSet(){
        title.text = "PERTANDINGAN"
        deskripsi.visibility = View.GONE
        video.visibility = View.VISIBLE
        btnVideo.visibility = View.VISIBLE
        btnSejarah.visibility = View.VISIBLE
        btnPeraturan.visibility = View.VISIBLE
        btnPertandingan.visibility = View.GONE

        init(dataSelect.pertandingan)

        mConstraintSet.clone(constraint)
        mConstraintSet.connect(
            R.id.linButton,
            ConstraintSet.TOP,
            R.id.viewVideo,
            ConstraintSet.BOTTOM
        )
        mConstraintSet.applyTo(constraint)

        if(dataSelect.video == "")
            btnVideo.visibility = View.GONE
    }

    private fun init(VIdeoId: String) {
        if (youTubePlayerMain != null) {
            try {
                youTubePlayerMain!!.pause()
                youTubePlayerMain!!.loadVideo(VIdeoId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            video.initialize(api_key,
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider,
                        youTubePlayer: YouTubePlayer,
                        b: Boolean
                    ) {
                        youTubePlayerMain = youTubePlayer
                        if (!b) {
                            try {
                                youTubePlayer.loadVideo(VIdeoId) // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
                            } catch (e: Exception) {
                                Toast.makeText(
                                    applicationContext,
                                    "Somrthing Went Wrong with this video ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        youTubeInitializationResult: YouTubeInitializationResult
                    ) {
                    }
                })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        editor.putString("isBackDetail", "true")
        editor.apply()
        startActivity(Intent(this, main_menu::class.java))
        finish()
    }
}