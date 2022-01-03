package com.example.tonote.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tonote.R
import com.example.tonote.adapter.NoteRVAdapter
import com.example.tonote.database.Notes
import com.example.tonote.databinding.FragmentMainBinding
import com.example.tonote.viewModel.MainViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainFragment : Fragment(), NoteRVAdapter.INoteRVAdapter {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteRVAdapter: NoteRVAdapter
    var allNotes: ArrayList<Notes> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.rView.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            noteRVAdapter = NoteRVAdapter(requireContext(), this@MainFragment)
            adapter = noteRVAdapter
        }
//        val contentOfNote : Array<String>? = arguments?.getStringArray("Content") as Array<String>
//        if (contentOfNote!=null && !contentOfNote.contentEquals(emptyArray())){
//            val note = Notes(contentOfNote[0],contentOfNote[1])
//            viewModel.insertNote(note)
//        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                allNotes = it as ArrayList
                noteRVAdapter.setNotes(allNotes)
            }
        })

        val fabButton = view.findViewById<ExtendedFloatingActionButton>(R.id.fabButton)
        fabButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_fabFragment)
        }
        return view
    }

    override fun onDeleteItemClicked(notes: Notes) {
        viewModel.deleteNote(notes)
    }

    override fun onItemClicked(notes: Notes) {
        Log.d("batao", "chlgya")
        val contentArray : ArrayList<String> = ArrayList()
        val bundle = Bundle()
        contentArray.add(0, notes.title)
        contentArray.add(1, notes.desc)
        contentArray.add(2, notes.colorOfNote)
        contentArray.add(3, notes.id.toString())
        bundle.putStringArrayList("Content", contentArray)
        findNavController().navigate(R.id.action_mainFragment_to_openNoteFragment , bundle)
    }
}