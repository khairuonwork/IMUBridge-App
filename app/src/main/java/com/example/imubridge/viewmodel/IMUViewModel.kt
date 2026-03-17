package com.example.imubridge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.imubridge.imu.IMUCollector
import com.example.imubridge.imu.IMUState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

@OptIn(FlowPreview::class)
class IMUViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val UI_SAMPLE_MS = 50L
    }

    private val collector = IMUCollector(application)

    val imuState: StateFlow<IMUState> =
        collector.imuState
            .sample(UI_SAMPLE_MS)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = IMUState(
                    gx = 0f, gy = 0f, gz = 0f,
                    ax = 0f, ay = 0f, az = 0f,
                    qx = 0f, qy = 0f, qz = 0f, qw = 1f
                )
            )

    private var started = false

    init {
        start()
    }

    fun start() {
        if (!started) {
            collector.start()
            started = true
        }
    }

    fun stop() {
        collector.stop()
        started = false
    }

    override fun onCleared() {
        super.onCleared()
        collector.stop()
    }
}