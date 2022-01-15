package com.example.tonote.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Notes)

    @Delete
    suspend fun delete(note: Notes)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Notes)

    // DCA = date created ascend
    // DCD = date created descend
    // DEA = date edited ascend
    // DED = date edited descend

    @Query("Select * from notes_table order by dateCreated ASC")
    fun getNotesByDCA() : LiveData<List<Notes>>

    @Query("Select * from notes_table order by dateCreated DESC")
    fun getNotesByDCD() : LiveData<List<Notes>>

    @Query("Select * from notes_table order by dateEdited ASC")
    fun getNotesByDEA() : LiveData<List<Notes>>

    @Query("Select * from notes_table order by dateEdited DESC")
    fun getNotesByDED() : LiveData<List<Notes>>
}