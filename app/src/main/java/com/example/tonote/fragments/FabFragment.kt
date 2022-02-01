package com.example.tonote.fragments

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
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
import com.example.tonote.util.LocalKeyStorage
import com.example.tonote.viewModel.MainViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class FabFragment : Fragment(), ColorListAdapter.IColorListAdapter {

    private var _binding: FragmentFabBinding? = null
    private val binding get() = _binding!!
    lateinit var contentArray: Array<String>
    lateinit var viewModel: MainViewModel
    private lateinit var bottomSheet: BottomSheetDialog
    var isNoteHidden: Boolean = false
    lateinit var localKeyStorage: LocalKeyStorage

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFabBinding.inflate(inflater, container, false)
        val view = binding.root
        localKeyStorage = LocalKeyStorage(requireContext())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.toolbar.inflateMenu(R.menu.menu2)
        binding.toolbar.overflowIcon = resources.getDrawable(R.drawable.ic_baseline_more_vert_24)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.isHidden -> {
                    if(localKeyStorage.getValue(LocalKeyStorage.passcode)!=0) {
                        it.isChecked = !it.isChecked
                        isNoteHidden = !isNoteHidden
                    }else{
                        // tell the user to set the password
                        Toast.makeText(context,"Coming Soon..",Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> true
            }
        }
        binding.saveButton.setOnClickListener {

            val colorOfNote = binding.parentView.background as Drawable
            val color = (colorOfNote as ColorDrawable).color
            var hexColorOfNote = String.format("#%06X", 0xFFFFFF and color)
            val backgroundColor =
                String.format("#%06X", 0xFFFFFF and resources.getColor(R.color.backgroundColor))
            val defaultCardColor =
                String.format("#%06X", 0xFFFFFF and resources.getColor(R.color.cardColor))
            if (hexColorOfNote == backgroundColor) {
                hexColorOfNote = defaultCardColor
            }
            Log.d("batao", hexColorOfNote)
            val dateCreated = SimpleDateFormat("yyyy MMM d, hh:mm a").format(Date())
            val dateEdited = dateCreated

            if ((binding.title.text != null || binding.desc.text != null) && (binding.title.text.toString() != "" || binding.desc.text.toString() != "")) {
                val note = Notes(
                    0,
                    binding.title.text.toString(),
                    binding.desc.text.toString(),
                    hexColorOfNote,
                    dateCreated,
                    dateEdited,
                    isNoteHidden
                )
                viewModel.insertNote(note)
//                val bundle = Bundle()
//                contentArray[0] = binding.title.text.toString()
//                contentArray[1] = binding.desc.text.toString()
//                bundle.putStringArray("Content",contentArray)
                findNavController().navigate(R.id.action_fabFragment_to_mainFragment)

            } else {
                Toast.makeText(context, "Please enter Title or Note", Toast.LENGTH_LONG).show()
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

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).visibility =
            View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}