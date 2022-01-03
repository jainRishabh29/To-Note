package com.example.tonote.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note:Notes)

    @Delete
    suspend fun delete(note:Notes)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Notes)

    @Query("Select * from notes_table order by id ASC")
    fun getAllNotes() : LiveData<List<Notes>>
}