package com.example.imubridge.imu

/*
    gyro
    accel
    fusion (quaternion ada w)
 */

data class IMUState(
    val gx: Float,
    val gy: Float,
    val gz: Float,

    val ax: Float,
    val ay: Float,
    val az: Float,

    val qx: Float,
    val qy: Float,
    val qz: Float,
    val qw: Float
)