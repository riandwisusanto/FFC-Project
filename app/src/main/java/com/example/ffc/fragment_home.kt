package com.example.ffc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

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
        requireActivity().title = "Home"

        val btnPindai = activity?.findViewById<ImageButton>(R.id.btnPindai)

        btnPindai?.setOnClickListener {
            (activity as main_menu?)?.SetNavState(R.id.nav_pindai)
        }
    }
}