package com.example.tonote.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import android.widget.Toolbar
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
import com.example.tonote.util.LocalKeyStorage


class MainFragment : Fragment(), NoteRVAdapter.INoteRVAdapter {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteRVAdapter: NoteRVAdapter
    var allNotes: ArrayList<Notes> = ArrayList()
    var sortNumber : Int = 1
    lateinit var localKeyStorage: LocalKeyStorage

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        createToolbarSortIcon()

        binding.rView.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            noteRVAdapter = NoteRVAdapter(requireContext(), this@MainFragment)
            adapter = noteRVAdapter
        }

        localKeyStorage = LocalKeyStorage(requireContext())
        when(localKeyStorage.getValue(LocalKeyStorage.sortNumber)){
            0 ->{
                Log.d("batao", "1")
                binding.toolbar.menu.getItem(0).isChecked = true
            }
            1->{
                Log.d("batao", "2")
                binding.toolbar.menu.getItem(1).isChecked = true
            }
            2->{
                Log.d("batao", "3")
                binding.toolbar.menu.getItem(2).isChecked = true
            }
            3->{
                Log.d("batao", "3")
                binding.toolbar.menu.getItem(3).isChecked = true
            }
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNotes(localKeyStorage.getValue(LocalKeyStorage.sortNumber)!!).observe(viewLifecycleOwner, Observer {
            it?.let {
                allNotes = it as ArrayList
                noteRVAdapter.setNotes(allNotes)
            }
        })

        binding.fabButton.setOnClickListener {
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
        bundle.putStringArrayList("Content", contentArray)
        findNavController().navigate(R.id.action_mainFragment_to_openNoteFragment, bundle)
    }

    private fun createToolbarSortIcon() {
        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.dateCreatedAsc -> {
                    it.isChecked = true
                    localKeyStorage.saveValue(LocalKeyStorage.sortNumber,0)
                    findNavController().navigate(R.id.action_mainFragment_self)
                    true
                }
                R.id.dateCreatedDesc -> {
                    it.isChecked = true
                    localKeyStorage.saveValue(LocalKeyStorage.sortNumber,1)
                    findNavController().navigate(R.id.action_mainFragment_self)
                    true
                }
                R.id.descend -> {
                    it.isChecked = true
                    localKeyStorage.saveValue(LocalKeyStorage.sortNumber,2)
                    findNavController().navigate(R.id.action_mainFragment_self)
                    true
                }
                R.id.ascend -> {
                    it.isChecked = true
                    localKeyStorage.saveValue(LocalKeyStorage.sortNumber,3)
                    findNavController().navigate(R.id.action_mainFragment_self)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.dateCreated, R.id.descend, R.id.Ascend -> {
//                item.isChecked = !item.isChecked
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
}
