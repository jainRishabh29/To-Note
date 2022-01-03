package com.example.tonote.repository

import androidx.lifecycle.LiveData
import com.example.tonote.database.Notes
import com.example.tonote.database.NotesDao


class MainRepo(private val notesDao: NotesDao) {

    val allNotes : LiveData<List<Notes>> = notesDao.getAllNotes()

    suspend fun insert(notes: Notes){
        notesDao.insert(notes)
    }
    suspend fun delete(notes: Notes){
        notesDao.delete(notes)
    }
    suspend fun updateNote(notes: Notes){
        notesDao.updateNote(notes)
    }

}