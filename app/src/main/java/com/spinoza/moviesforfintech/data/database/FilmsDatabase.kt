package com.spinoza.moviesforfintech.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.spinoza.moviesforfintech.data.database.DbConstants.Companion.DATABASE_NAME
import com.spinoza.moviesforfintech.data.database.model.FilmDbModel

@Database(entities = [FilmDbModel::class], version = 1, exportSchema = false)
abstract class FilmsDatabase : RoomDatabase() {
    companion object {
        private var db: FilmsDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): FilmsDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(context, FilmsDatabase::class.java, DATABASE_NAME).build()
                db = instance
                return instance
            }
        }
    }

    abstract fun movieDao(): FilmsDao
}