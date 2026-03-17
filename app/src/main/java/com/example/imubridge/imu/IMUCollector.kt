package com.example.imubridge.imu

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IMUCollector(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val gyroSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val accelSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val rotationSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)


    // Observable Stream
    private val _imuState = MutableStateFlow(
        IMUState(
            gx = 0f, gy = 0f, gz = 0f,
            ax = 0f, ay = 0f, az = 0f,
            qx = 0f, qy = 0f, qz = 0f, qw = 1f
        )
    )

    val imuState: StateFlow<IMUState> = _imuState

    fun start() {

        gyroSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        accelSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        rotationSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }


    fun stop() {
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent) {

        when (event.sensor.type) {

            Sensor.TYPE_GYROSCOPE -> {

                _imuState.value = _imuState.value.copy(
                    gx = event.values[0],
                    gy = event.values[1],
                    gz = event.values[2]
                )

            }

            Sensor.TYPE_ACCELEROMETER -> {

                _imuState.value = imuState.value.copy(
                    ax = event.values[0],
                    ay = event.values[1],
                    az = event.values[2]
                )

            }

            Sensor.TYPE_GAME_ROTATION_VECTOR -> {

                _imuState.value = _imuState.value.copy(
                    qx = event.values[0],
                    qy = event.values[1],
                    qz = event.values[2],
                    qw = event.values.getOrElse(3) { 1f }
                )

            }

        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // not used, but part of the interface.
    }
}