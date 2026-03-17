package com.example.imubridge.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imubridge.ui.cards.IMUCard
import com.example.imubridge.viewmodel.IMUViewModel

@Composable
fun IMUScreen(
    viewModel: IMUViewModel = viewModel()
) {

    val imu by viewModel.imuState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        IMUCard(
            title = "Gyroscope",
            values = listOf(
                "gx" to imu.gx,
                "gy" to imu.gy,
                "gz" to imu.gz
            )
        )

        IMUCard(
            title = "Accelerometer",
            values = listOf(
                "ax" to imu.ax,
                "ay" to imu.ay,
                "az" to imu.az
            )
        )

        IMUCard(
            title = "Orientation",
            values = listOf(
                "qx" to imu.qx,
                "qy" to imu.qy,
                "qz" to imu.qz,
                "qw" to imu.qw
            )
        )
    }
}