package com.example.ffc

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.number


/**
 * A simple [Fragment] subclass.
 */
class fragment_home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPindai = activity?.findViewById<ImageButton>(R.id.btnPindai)
        val btnMarket = activity?.findViewById<ImageButton>(R.id.btnBuy)

        btnPindai?.setOnClickListener {
            (activity as main_menu?)?.SetNavState(R.id.nav_pindai)
        }

        btnMarket?.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.market_layout)
        val shopeBtn = dialog.findViewById(R.id.shopee) as ImageButton
        val tokoBtn = dialog.findViewById(R.id.tokopedia) as ImageButton
        val waBtn = dialog.findViewById(R.id.wa) as ImageButton
        val close = dialog.findViewById(R.id.close) as ImageButton

        close.setOnClickListener {
            dialog.dismiss()
        }

        shopeBtn.setOnClickListener {
            Toast.makeText(activity, "Belum Tersedia", Toast.LENGTH_SHORT).show()
        }

        tokoBtn.setOnClickListener {
            click("https://tokopedia.com/ffcid")
        }

        waBtn.setOnClickListener {
            click("https://api.whatsapp.com/send?phone=+6285807215821")
        }
        dialog.show()
    }

    fun click(url: String){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}