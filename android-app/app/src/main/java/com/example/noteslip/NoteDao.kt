package com.example.noteslip

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY createTime DESC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE `key` = :key")
    fun getNoteByKey(key: String): Note?

    @Query("SELECT * FROM notes WHERE `nfcId` = :nfcId")
    fun getNoteByNfcId(nfcId: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)
} 