package com.example.imubridge.ui.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.imubridge.viewmodel.StreamingState

@Composable
fun StreamingCard(
    state: StreamingState,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = "Streaming Control", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "State: ${state::class.simpleName}")

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(
                    onClick = onStart,
                    enabled = state == StreamingState.Idle
                ) {
                    Text("Start")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onStop,
                    enabled = state == StreamingState.Streaming
                ) {
                    Text("Stop")
                }
            }
        }
    }
}