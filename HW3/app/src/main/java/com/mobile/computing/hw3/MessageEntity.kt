package com.mobile.computing.hw3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "path") val path: String
)

data class MessageContents(
    val author: String,
    val content: String,
    val path: String
)