package com.example.imubridge.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class SocketSender {

    companion object {
        private const val TAG = "SocketSender"
        private const val CONNECT_TIMEOUT_MS = 3000
    }

    private var socket: Socket? = null
    private var output: OutputStream? = null

    @Volatile
    var isConnected: Boolean = false
        private set

    suspend fun connect(host: String, port: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Connecting to $host:$port...")

                val newSocket = Socket()
                newSocket.connect(
                    InetSocketAddress(host, port),
                    CONNECT_TIMEOUT_MS
                )

                socket = newSocket
                output = newSocket.getOutputStream()
                isConnected = true

                Log.d(TAG, "Connected SUCCESS")

                true
            } catch (e: Exception) {
                Log.e(TAG, "Connection failed: ${e.message}")
                isConnected = false
                false
            }
        }
    }

    suspend fun send(data: String) {
        withContext(Dispatchers.IO) {

            if (!isConnected || output == null) {
                Log.d(TAG, "Not connected, skip send")
                return@withContext
            }

            try {
                val packet = data + "\n"

                Log.d(TAG, "SEND: $data")

                output?.write(packet.toByteArray())
                output?.flush()

            } catch (e: Exception) {
                Log.e(TAG, "Send failed: ${e.message}")
                isConnected = false
                closeInternal()
            }
        }
    }

    fun close() {
        Log.d(TAG, "Closing connection")

        isConnected = false
        closeInternal()
    }

    private fun closeInternal() {
        try {
            output?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Output close error: ${e.message}")
        }

        try {
            socket?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Socket close error: ${e.message}")
        }

        output = null
        socket = null
    }
}