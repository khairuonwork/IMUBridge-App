package com.example.imubridge.packet

import com.example.imubridge.imu.IMUState

object PacketFormatter {
    fun format(state: IMUState): String {
        return buildString {
            append("#IMU,")
            append(state.gx).append(",")
            append(state.gy).append(",")
            append(state.gz).append(",")
            append(state.ax).append(",")
            append(state.ay).append(",")
            append(state.az).append(",")
            append(state.qx).append(",")
            append(state.qy).append(",")
            append(state.qz).append(",")
            append(state.qw)
        }
    }
}