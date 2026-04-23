package com.example.aura_style.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura_style.data.local.entity.OutfitLog
import com.example.aura_style.data.repository.OutfitLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OutfitLogViewModel @Inject constructor(
    private val outfitLogRepository: OutfitLogRepository
) : ViewModel() {
    private val _logs = MutableStateFlow<List<OutfitLog>>(emptyList())
    val logs: StateFlow<List<OutfitLog>> = _logs.asStateFlow()

    fun insertLog(log: OutfitLog) {
        viewModelScope.launch {
            outfitLogRepository.insertLog(log)
            getAllLogs()
        }
    }

    fun getAllLogs() {
        viewModelScope.launch {
            _logs.value = outfitLogRepository.getAllLogs()
        }
    }

    fun getLogsByDate(date: String) {
        viewModelScope.launch {
            _logs.value = outfitLogRepository.getLogsByDate(date)
        }
    }
}

