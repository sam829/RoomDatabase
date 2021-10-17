package com.example.roomdatabasezero

import android.app.Application
import com.example.roomdatabasezero.database.WordRepository
import com.example.roomdatabasezero.database.WordRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}