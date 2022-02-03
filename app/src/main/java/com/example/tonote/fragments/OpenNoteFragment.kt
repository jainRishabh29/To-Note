package com.example.tonote.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.system.Os.accept
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.tonote.R
import com.example.tonote.adapter.ColorListAdapter
import com.example.tonote.database.Notes
import com.example.tonote.databinding.FragmentOpenNoteBinding
import com.example.tonote.util.LocalKeyStorage
import com.example.tonote.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class OpenNoteFragment : Fragment(), ColorListAdapter.IColorListAdapter {

    private var _binding: FragmentOpenNoteBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel
    private lateinit var bottomSheet: BottomSheetDialog
    var idOfNote: Int = 0
    private lateinit var dateCreated: String
    var isNoteHidden: Boolean = false
    lateinit var localKeyStorage: LocalKeyStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOpenNoteBinding.inflate(inflater, container, false)
        val view = binding.root
        localKeyStorage = LocalKeyStorage(requireContext())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val contentOfNote = arguments?.getStringArrayList("Content")
        binding.toolbar.inflateMenu(R.menu.menu_open_fragment)
        binding.toolbar.overflowIcon = resources.getDrawable(R.drawable.ic_baseline_more_vert_24)

        if (!contentOfNote.isNullOrEmpty()) {
            binding.title.setText(contentOfNote[0])
            binding.desc.setText(contentOfNote[1])
            idOfNote = contentOfNote[3].toInt()
            binding.toolbar.setBackgroundColor(Color.parseColor(contentOfNote[2]))
            binding.parentView.setBackgroundColor(Color.parseColor(contentOfNote[2]))
            binding.dateEdited.text = contentOfNote[4]
            dateCreated = contentOfNote[5]
            isNoteHidden = contentOfNote[6].toBoolean()
            binding.toolbar.menu.getItem(2).isChecked = contentOfNote[6].toBoolean()

        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.isHidden -> {
                    if (localKeyStorage.getValue(LocalKeyStorage.passcode) != 0) {
                        it.isChecked = !it.isChecked
                        isNoteHidden = !isNoteHidden
                    } else {
                        // tell the user to set the password
                        Toast.makeText(context, "Coming Soon..", Toast.LENGTH_SHORT).show()
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.title))
                            .setMessage(resources.getString(R.string.supporting_text))
                            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                                // Respond to neutral button press
                            }
                            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->

                                localKeyStorage.saveValue(LocalKeyStorage.isFromOtherFragment, 1)
                                val contentArray: ArrayList<String> = ArrayList()
                                val colorOfNote = binding.parentView.background as Drawable
                                val color = (colorOfNote as ColorDrawable).color
                                val hexColorOfNote = String.format("#%06X", 0xFFFFFF and color)
                                val bundle = Bundle()
                                if (!contentOfNote.isNullOrEmpty()) {
                                    contentArray.add(0, binding.title.text.toString())
                                    contentArray.add(1, binding.desc.text.toString())
                                    contentArray.add(2, hexColorOfNote)
                                    contentArray.add(3, contentOfNote[3])
                                    contentArray.add(4, contentOfNote[4])
                                    contentArray.add(5, contentOfNote[5])
                                    contentArray.add(6, contentOfNote[6])
                                }
                                bundle.putStringArrayList("Content", contentArray)
                                findNavController().navigate(
                                    R.id.action_openNoteFragment_to_passwordFragment,
                                    bundle
                                )
                            }
                            .show()
                    }
                    true
                }
                R.id.details -> {
                    val dialog = MaterialDialog(requireContext())
                        .customView(R.layout.details_dialog)
                        .cornerRadius(12f).positiveButton(null,"OK",null)
                    dialog.findViewById<TextView>(R.id.dateCreated).text = (contentOfNote!![5])
                    dialog.findViewById<TextView>(R.id.dateEdited).text = (contentOfNote[4])
                    dialog.show()
                    true
                }
                R.id.delete -> {
                    // delete this note
                    if (!contentOfNote.isNullOrEmpty()) {
                        val note = Notes(
                            contentOfNote[3].toInt(),
                            contentOfNote[0],
                            contentOfNote[1],
                            contentOfNote[2],
                            contentOfNote[4],
                            contentOfNote[5],
                            contentOfNote[6].toBoolean(),
                        )
                        viewModel.deleteNote(note)
                        findNavController().navigate(R.id.action_openNoteFragment_to_mainFragment)
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
            val dateUpdated = SimpleDateFormat("yyyy MMM d, hh:mm a").format(Date())

            if ((binding.title.text != null || binding.desc.text != null) && (binding.title.text.toString() != "" || binding.desc.text.toString() != "")) {
                val note = Notes(
                    idOfNote,
                    binding.title.text.toString(),
                    binding.desc.text.toString(),
                    hexColorOfNote,
                    dateCreated,
                    dateUpdated,
                    isNoteHidden
                )
                Log.d("batao", note.title)
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

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).visibility =
            View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}