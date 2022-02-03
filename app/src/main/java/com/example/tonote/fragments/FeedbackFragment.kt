package com.example.tonote.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tonote.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class FeedbackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)

        view.findViewById<ImageView>(R.id.backbtn1).setOnClickListener {
            findNavController().navigate(R.id.action_feedbackFragment_to_mainFragment)
        }
        view.findViewById<Button>(R.id.send).setOnClickListener {
            Toast.makeText(context,"Response submitted",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_feedbackFragment_to_mainFragment)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).visibility =
            View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

}