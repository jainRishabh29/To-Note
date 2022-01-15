package com.example.tonote.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tonote.R
import com.example.tonote.adapter.ColorListAdapter
import com.example.tonote.database.Notes
import com.example.tonote.databinding.FragmentFabBinding
import com.example.tonote.databinding.FragmentMainBinding
import com.example.tonote.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class FabFragment : Fragment() , ColorListAdapter.IColorListAdapter {

    private var _binding: FragmentFabBinding? = null
    private val binding get() = _binding!!
    lateinit var contentArray: Array<String>
    lateinit var viewModel: MainViewModel
    private lateinit var bottomSheet : BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFabBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.saveButton.setOnClickListener {

            val colorOfNote = binding.parentView.background as Drawable
            val color = (colorOfNote as ColorDrawable).color
            var hexColorOfNote = String.format("#%06X", 0xFFFFFF and color)
            val backgroundColor = String.format("#%06X", 0xFFFFFF and resources.getColor(R.color.backgroundColor))
            val defaultCardColor = String.format("#%06X", 0xFFFFFF and resources.getColor(R.color.cardColor))
            if (hexColorOfNote == backgroundColor){
                hexColorOfNote = defaultCardColor
            }
            Log.d("batao" , hexColorOfNote)
            val dateCreated = SimpleDateFormat("yyyy MMM d, hh:mm a").format(Date())
            val dateEdited = dateCreated

            if ((binding.title.text != null && binding.desc.text != null) && (binding.title.text.toString() != "" && binding.desc.text.toString() != "")) {
                val note = Notes(0,binding.title.text.toString(), binding.desc.text.toString(), hexColorOfNote, dateCreated, dateEdited)
                viewModel.insertNote(note)
//                val bundle = Bundle()
//                contentArray[0] = binding.title.text.toString()
//                contentArray[1] = binding.desc.text.toString()
//                bundle.putStringArray("Content",contentArray)
                findNavController().navigate(R.id.action_fabFragment_to_mainFragment)

            } else {
                Toast.makeText(context, "Please enter Title and Note", Toast.LENGTH_LONG).show()
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_fabFragment_to_mainFragment)
        }
        binding.colorPaletteIcon.setOnClickListener {
            bottomSheet = BottomSheetDialog(requireContext())
            bottomSheet.setContentView(R.layout.fragment_bottom_sheet)
            val recyclerView = bottomSheet.findViewById<RecyclerView>(R.id.colorRecyclerView)
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ColorListAdapter(requireContext(), this@FabFragment, -1)
            }
            bottomSheet.show()
        }

        return view
    }

    override fun onItemClicked(colorName: String) {
        val cardView = bottomSheet.findViewById<CardView>(R.id.cardView)
        binding.toolbar.setBackgroundColor(Color.parseColor(colorName))
        binding.parentView.setBackgroundColor(Color.parseColor(colorName))
        cardView!!.setCardBackgroundColor(Color.parseColor(colorName))

    }

}