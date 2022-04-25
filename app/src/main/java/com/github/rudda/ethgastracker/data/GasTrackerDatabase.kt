package com.github.ethgastracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.ethgastracker.data.dao.GasDao

@Database( entities = [Gas::class, Gas.Gwei::class], version = 1)
abstract class GasTrackerDatabase : RoomDatabase() {
    abstract fun GasDao() : GasDao

    companion object {
        private var DATABASE_NAME = "GasTrackerDatabase"
        private var INSTANCE : GasTrackerDatabase? = null

        fun getDatabase(context : Context) : GasTrackerDatabase {

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GasTrackerDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }

    }
}