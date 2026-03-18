package com.example.imubridge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.imubridge.imu.IMUCollector
import com.example.imubridge.imu.IMUState
import com.example.imubridge.packet.PacketFormatter
import com.example.imubridge.network.SocketSender
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

sealed class StreamingState {
    object Idle : StreamingState()
    object Connecting : StreamingState()
    object Streaming : StreamingState()
}

@OptIn(FlowPreview::class)
class IMUViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "IMUViewModel"
        private const val UI_SAMPLE_MS = 50L
        private const val NETWORK_SAMPLE_MS = 20L
    }

    private val collector = IMUCollector(application)
    private val sender = SocketSender()

    private var streamingJob: Job? = null
    private var started = false

    private val _streamingState = MutableStateFlow<StreamingState>(StreamingState.Idle)
    val streamingState: StateFlow<StreamingState> = _streamingState

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

    init {
        startSensor()
    }

    private fun startSensor() {
        if (!started) {
            collector.start()
            started = true
            Log.d(TAG, "Sensor started")
        }
    }

    fun startStreaming() {
        if (streamingJob?.isActive == true) {
            Log.d(TAG, "Streaming already active")
            return
        }

        streamingJob = viewModelScope.launch {

            _streamingState.value = StreamingState.Connecting
            Log.d(TAG, "Connecting...")

            // 🔥 suspend → pasti selesai
            val connected = sender.connect("127.0.0.1", 5000)

            if (!connected) {
                Log.e(TAG, "Connection failed")
                _streamingState.value = StreamingState.Idle
                return@launch
            }

            Log.d(TAG, "Connected SUCCESS")

            // 🔥 handshake
            sender.send("#HELLO")

            _streamingState.value = StreamingState.Streaming

            // 🔥 stream IMU
            collector.imuState
                .sample(NETWORK_SAMPLE_MS)
                .collect { state ->
                    val packet = PacketFormatter.format(state)
                    sender.send(packet)
                }
        }
    }

    fun stopStreaming() {
        if (_streamingState.value != StreamingState.Streaming) return

        Log.d(TAG, "Stopping streaming")

        streamingJob?.cancel()
        streamingJob = null

        sender.close()
        _streamingState.value = StreamingState.Idle
    }

    override fun onCleared() {
        super.onCleared()

        Log.d(TAG, "ViewModel cleared")

        streamingJob?.cancel()
        collector.stop()
        sender.close()
    }
}