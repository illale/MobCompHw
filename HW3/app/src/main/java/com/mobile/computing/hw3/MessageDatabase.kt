package com.mobile.computing.hw3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var Instance: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MessageDatabase::class.java, "message_database")
                    .allowMainThreadQueries()
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}