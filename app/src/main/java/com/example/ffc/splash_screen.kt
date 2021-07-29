package com.example.ffc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.ffc.db.ItemFFC

class splash_screen : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences("save", Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()
        editor.putString("isBackDetail", "false")
        editor.apply()

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            startActivity(Intent(this, main_menu::class.java))
            finish()
        }, 3000)
    }

}