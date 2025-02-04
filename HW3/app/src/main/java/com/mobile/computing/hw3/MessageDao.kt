package com.mobile.computing.hw3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {

    @Query("SELECT * FROM message")
    fun getAll(): List<Message>

    @Insert(entity = Message::class)
    fun insertMessage(msgContents: MessageContents)

}