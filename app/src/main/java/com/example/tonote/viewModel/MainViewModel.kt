package com.example.tonote.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tonote.database.Notes
import com.example.tonote.database.RoomDB
import com.example.tonote.repository.MainRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel constructor(application: Application) : AndroidViewModel(application){

    private val repository : MainRepo
//    lateinit var allNotes : LiveData<List<Notes>>

    init {
        val dao = RoomDB.getDatabase(application).getNotesDao()
        repository = MainRepo(dao)
    }
    fun insertNote(note:Notes) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }
    fun deleteNote(note:Notes) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }
    fun updateNote(note: Notes) = viewModelScope.launch(Dispatchers.IO)  {
        repository.updateNote(note)
    }
    fun getNotes(sortNumber:Int): LiveData<List<Notes>> {
        return repository.getAllNotes(sortNumber)
    }
    fun getHiddenNotes(): LiveData<List<Notes>> {
        return repository.getHiddenNotes()
    }
    fun searchNote(searchQuery:String) : LiveData<List<Notes>> {
        return repository.searchNote(searchQuery)
    }
}