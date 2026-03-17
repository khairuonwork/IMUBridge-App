package com.example.imubridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.imubridge.ui.pages.IMUScreen
import com.example.imubridge.ui.theme.IMUBridgeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            IMUBridgeTheme {

                IMUScreen()

            }

        }
    }
}