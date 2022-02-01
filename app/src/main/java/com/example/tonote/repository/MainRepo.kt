package com.example.tonote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.tonote.database.Notes
import com.example.tonote.database.NotesDao


class MainRepo(private val notesDao: NotesDao) {

    lateinit var allNotes : LiveData<List<Notes>>
    lateinit var allHiddenNotes : LiveData<List<Notes>>

    suspend fun insert(notes: Notes){
        notesDao.insert(notes)
    }
    suspend fun delete(notes: Notes){
        notesDao.delete(notes)
    }
    suspend fun updateNote(notes: Notes){
        notesDao.updateNote(notes)
    }

    fun getAllNotes(sortNumber:Int):LiveData<List<Notes>>{
        when(sortNumber){
            0 ->{
                allNotes = notesDao.getNotesByDCD()
            }
            1->{
                allNotes = notesDao.getNotesByDCA()
            }
            2->{
                allNotes = notesDao.getNotesByDED()
            }
            3->{
                allNotes = notesDao.getNotesByDEA()
            }
        }
        return allNotes
    }

    fun getHiddenNotes() : LiveData<List<Notes>>{
        allHiddenNotes = notesDao.getHiddenNotes()
        return allHiddenNotes
    }

}