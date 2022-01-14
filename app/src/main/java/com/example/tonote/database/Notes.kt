package com.example.tonote.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Notes(
    @PrimaryKey(autoGenerate = true) val id: Int ,
    @ColumnInfo(name = "Title") val title: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "colorOfNote") val colorOfNote: String,
    @ColumnInfo(name = "dateCreated") val dateCreated: String
)