package com.example.anizeno

import android.app.Application
import androidx.room.Room
import com.example.anizeno.data.local.room_db.AppDatabase



class App : Application() {

    companion object {
        // Referencia global a la base de datos
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "anime_database" // nombre del archivo .db
        )
            .fallbackToDestructiveMigration(false) // elimina la BD si cambia el esquema
            .build()
    }
}