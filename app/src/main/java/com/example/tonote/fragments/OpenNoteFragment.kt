package com.example.tonote.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.example.tonote.databinding.FragmentOpenNoteBinding
import com.example.tonote.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class OpenNoteFragment : Fragment() , ColorListAdapter.IColorListAdapter{

    private var _binding: FragmentOpenNoteBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel
    private lateinit var bottomSheet : BottomSheetDialog
    var idOfNote: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOpenNoteBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val contentOfNote = arguments?.getStringArrayList("Content")
        if(!contentOfNote.isNullOrEmpty()) {
            binding.title.setText(contentOfNote[0])
            binding.desc.setText(contentOfNote[1])
            idOfNote = contentOfNote[3].toInt()
            binding.toolbar.setBackgroundColor(Color.parseColor(contentOfNote[2]))
            binding.parentView.setBackgroundColor(Color.parseColor(contentOfNote[2]))
        }
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

            if ((binding.title.text != null && binding.desc.text != null) && (binding.title.text.toString() != "" && binding.desc.text.toString() != "")) {
                val note = Notes(idOfNote, binding.title.text.toString(), binding.desc.text.toString(),hexColorOfNote)
                Log.d("batao" , note.title)
                viewModel.updateNote(note)
                findNavController().navigate(R.id.action_openNoteFragment_to_mainFragment)

            } else {
                Toast.makeText(context, "Please enter Title and Note", Toast.LENGTH_LONG).show()
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_openNoteFragment_to_mainFragment)
        }

        binding.colorPaletteIcon.setOnClickListener {
            bottomSheet = BottomSheetDialog(requireContext())
            bottomSheet.setContentView(R.layout.fragment_bottom_sheet)
            val recyclerView = bottomSheet.findViewById<RecyclerView>(R.id.colorRecyclerView)
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ColorListAdapter(requireContext(), this@OpenNoteFragment, -1)
            }
            bottomSheet.show()
        }
        return view
    }

    override fun onItemClicked(colorName: String) {
        val cardView = bottomSheet.findViewById<CardView>(R.id.cardView)
        val parentViewColor = binding.parentView
        binding.toolbar.setBackgroundColor(Color.parseColor(colorName))
        parentViewColor.setBackgroundColor(Color.parseColor(colorName))
        cardView!!.setCardBackgroundColor(Color.parseColor(colorName))

    }
}