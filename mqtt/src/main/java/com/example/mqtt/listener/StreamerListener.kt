package com.example.mqtt.listener

interface StreamerListener {
    fun onConnectionSuccess()
    fun onConnectionFailure()
    fun onConnectionClosed()
}