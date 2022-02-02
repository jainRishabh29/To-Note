package com.example.tonote.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tonote.R
import com.example.tonote.adapter.NoteRVAdapter
import com.example.tonote.database.Notes
import com.example.tonote.databinding.FragmentSearchBinding
import com.example.tonote.viewModel.MainViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class SearchFragment : Fragment(), NoteRVAdapter.INoteRVAdapter {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var LviewModel: MainViewModel
    private lateinit var noteRVAdapter: NoteRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        LviewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lViewModel2 = LviewModel
        binding.lifecycleOwner = this
        binding.lottie.visibility = View.GONE
        binding.searchNoteRView.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            noteRVAdapter = NoteRVAdapter(requireContext(), this@SearchFragment)
            adapter = noteRVAdapter
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_mainFragment)
        }
        binding.searchNote.addTextChangedListener(textWatcher)
        return binding.root
    }


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { str ->
                if (str.isNotEmpty()) {
                    binding.searchBtn.setImageResource(R.drawable.ic_baseline_close_24)
                    binding.searchBtn.setOnClickListener {
                        binding.searchNote.setText("")
                    }
                    LviewModel.searchNote("%$s%").observe(viewLifecycleOwner, {
                        if(!it.isNullOrEmpty()){
                            binding.lottie.visibility = View.GONE
                            noteRVAdapter.setNotes(it as ArrayList<Notes>)
                            binding.searchNoteRView.visibility = View.VISIBLE
                        }
                        else{
                            binding.lottie.visibility = View.VISIBLE
                            binding.searchNoteRView.visibility = View.GONE
                        }
                    })
                }
                else{
                    binding.lottie.visibility = View.GONE
                    binding.searchNoteRView.visibility = View.GONE
                    binding.searchBtn.setImageResource(R.drawable.ic_baseline_search_24)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDeleteItemClicked(notes: Notes) {
        LviewModel.deleteNote(notes)
    }

    override fun onItemClicked(notes: Notes) {
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
        findNavController().navigate(R.id.action_searchFragment_to_openNoteFragment, bundle)
    }

}