package com.example.tonote.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tonote.R
import com.example.tonote.adapter.NoteRVAdapter
import com.example.tonote.database.Notes
import com.example.tonote.databinding.FragmentHiddenNoteBinding
import com.example.tonote.databinding.FragmentPasswordBinding
import com.example.tonote.util.LocalKeyStorage
import com.example.tonote.viewModel.MainViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class HiddenNoteFragment : Fragment(), NoteRVAdapter.INoteRVAdapter {

    private var _binding: FragmentHiddenNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteRVAdapter: NoteRVAdapter
    lateinit var viewModel: MainViewModel
    lateinit var localKeyStorage: LocalKeyStorage
    var allHiddenNotes: ArrayList<Notes> = ArrayList()

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHiddenNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.hiddenNoteRV.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            noteRVAdapter = NoteRVAdapter(requireContext(), this@HiddenNoteFragment)
            adapter = noteRVAdapter
        }

        localKeyStorage = LocalKeyStorage(requireContext())

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getHiddenNotes().observe(viewLifecycleOwner, {
            it?.let {
                allHiddenNotes = it as ArrayList
                noteRVAdapter.setNotes(allHiddenNotes)
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_hiddenNoteFragment_to_mainFragment)
        }

        return view
    }

    override fun onDeleteItemClicked(notes: Notes) {
        viewModel.deleteNote(notes)
    }

    override fun onItemClicked(notes: Notes) {
        Log.d("batao", "chlgya")
        val contentArray: ArrayList<String> = ArrayList()
        val bundle = Bundle()
        contentArray.add(0, notes.title)
        contentArray.add(1, notes.desc)
        contentArray.add(2, notes.colorOfNote)
        contentArray.add(3, notes.id.toString())
        contentArray.add(4, notes.dateCreated)
        contentArray.add(5, notes.dateEdited)
        contentArray.add(6, notes.isHidden.toString())
        bundle.putStringArrayList("Content", contentArray)
        findNavController().navigate(R.id.action_hiddenNoteFragment_to_openNoteFragment, bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}