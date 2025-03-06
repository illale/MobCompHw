package com.mobile.computing.locationnotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MarkerNote::class], version = 1)
abstract class MarkerDatabase: RoomDatabase() {
    abstract fun markerDao(): MarkerDao

    companion object {
        @Volatile
        private var Instance: MarkerDatabase? = null

        fun getDatabase(context: Context): MarkerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MarkerDatabase::class.java, "marker_database")
                    .allowMainThreadQueries()
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}