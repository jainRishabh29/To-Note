package com.example.tonote.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBarDrawerToggle
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.tonote.util.LocalKeyStorage


class MainFragment : Fragment(), NoteRVAdapter.INoteRVAdapter {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteRVAdapter: NoteRVAdapter
    var allNotes: ArrayList<Notes> = ArrayList()
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var localKeyStorage: LocalKeyStorage

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.rView.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            noteRVAdapter = NoteRVAdapter(requireContext(), this@MainFragment)
            adapter = noteRVAdapter
        }

        localKeyStorage = LocalKeyStorage(requireContext())

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNotes(localKeyStorage.getValue(LocalKeyStorage.sortNumber)!!).observe(viewLifecycleOwner, Observer {
            it?.let {
                allNotes = it as ArrayList
                noteRVAdapter.setNotes(allNotes)
            }
        })

        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_fabFragment)
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
        findNavController().navigate(R.id.action_mainFragment_to_openNoteFragment, bundle)
    }



    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fabButton).visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        when (localKeyStorage.getValue(LocalKeyStorage.sortNumber)) {
            0 -> {
                Log.d("batao", "1")
                menu.getItem(0).isChecked = true
            }
            1 -> {
                Log.d("batao", "2")
                menu.getItem(1).isChecked = true
            }
            2 -> {
                Log.d("batao", "3")
               menu.getItem(2).isChecked = true
            }
            3 -> {
                Log.d("batao", "3")
               menu.getItem(3).isChecked = true
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dateCreatedDesc -> {
                item.isChecked = true
                localKeyStorage.saveValue(LocalKeyStorage.sortNumber, 0)
                    findNavController().navigate(R.id.action_mainFragment_self)
                true
            }
            R.id.dateCreatedAsc -> {
                item.isChecked = true
                localKeyStorage.saveValue(LocalKeyStorage.sortNumber, 1)
                    findNavController().navigate(R.id.action_mainFragment_self)
                true
            }
            R.id.descend -> {
                item.isChecked = true
                localKeyStorage.saveValue(LocalKeyStorage.sortNumber, 2)
                    findNavController().navigate(R.id.action_mainFragment_self)
                true
            }
            R.id.ascend -> {
                item.isChecked = true
                localKeyStorage.saveValue(LocalKeyStorage.sortNumber, 3)
                    findNavController().navigate(R.id.action_mainFragment_self)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}
