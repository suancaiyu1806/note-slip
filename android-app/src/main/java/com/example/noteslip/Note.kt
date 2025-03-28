package com.example.noteslip

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val key: String,
    val nfcId: String? = null,
    val content: String = "",
    val createTime: Long = System.currentTimeMillis()
) 