package com.example.ffc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.android.youtube.player.YouTubePlayerSupportFragment


class main_menu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var toolbar: Toolbar? = null
    var drawer: DrawerLayout? = null
    var toggle: ActionBarDrawerToggle? = null
    var fragment: Fragment? = null
    var transaction: FragmentTransaction? = null
    private var doubleBackToExitPressedOnce = false
    lateinit var navigationView : NavigationView

    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("save", Context.MODE_PRIVATE)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navigationView = findViewById(R.id.nav_view)
        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle!!.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.getMenu().getItem(0).isChecked = true
        if(sharedPreferences.getString("isBackDetail", "") == "true")
            SetNavState(R.id.nav_detail_pindai)
        else
            firstFragmentDisplay(R.id.nav_home)
    }

    fun firstFragmentDisplay(itemId: Int) {
        fragment = null
        when (itemId) {
            R.id.nav_home -> fragment = fragment_home()
            R.id.nav_pindai -> fragment = fragment_pindai()
            R.id.nav_detail_pindai -> fragment = fragment_detail_pindai()
//            R.id.nav_lihat_detail -> fragment = fragment_lihat_detail()
        }
        if (fragment != null) {
            transaction = supportFragmentManager.beginTransaction()
            transaction!!.replace(R.id.fLayout, fragment!!)
            transaction!!.commit()
        }
        drawer!!.closeDrawers()

        if(itemId == R.id.logout){
            AlertDialog.Builder(this)
                .setMessage("Yakin Keluar Aplikasi ?")
                .setPositiveButton("Oke",
                    DialogInterface.OnClickListener { dialog, which -> finish() })
                .setNegativeButton("Batal",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                .show()
        }
    }

    fun SetNavState(id: Int) {
        firstFragmentDisplay(id)
        navigationView.setCheckedItem(R.id.nav_pindai)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        firstFragmentDisplay(item.itemId)
        return true
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawers()
        } else {
            if (doubleBackToExitPressedOnce) {
                AlertDialog.Builder(this)
                    .setMessage("Yakin Keluar Aplikasi ?")
                    .setPositiveButton("Oke",
                        DialogInterface.OnClickListener { dialog, which -> finish() })
                    .setNegativeButton("Batal",
                        DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                    .show()
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Tekan kembali untuk keluar", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}