package com.example.imubridge.ui.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
    IMUCard ini nanti akan dipakai oleh 3 data yang diambil dari Android.
    1. gyroscope (gx gy gz)
    2. accelerometer (ax ay az)
    3. qx qy qz qw -> orientation (quaternion / attitude)

    IMUCard -> Reusable
 */

@Composable
fun IMUCard(
    title: String,
    values: List<Pair<String, Float>>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                fontSize = 20.sp
            )

            values.forEach { (axis, value) ->
                Text(text = "$axis: $value")
            }
        }
    }
}

/*
    Cara pakai nanti seperti ini:
    IMUCard(title = "Accelerometer",
    values = listOf(
        "ax" to ax,
        "ay" to ay,
        "az" to az
        )
    )
 */