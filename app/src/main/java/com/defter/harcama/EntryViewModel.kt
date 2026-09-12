package com.defter.harcama

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.defter.harcama.data.AppDatabase
import com.defter.harcama.data.Entry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).entryDao()

    val entries: StateFlow<List<Entry>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEntry(entry: Entry) {
        viewModelScope.launch { dao.insert(entry) }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch { dao.delete(entry) }
    }

    fun resetAll() {
        viewModelScope.launch { dao.deleteAll() }
    }
}
