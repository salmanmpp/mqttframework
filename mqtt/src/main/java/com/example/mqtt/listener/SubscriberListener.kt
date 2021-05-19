package com.example.mqtt.listener

import com.amazonaws.services.iot.client.AWSIotMessage

interface SubscriberListener {
    fun onMessage(message: AWSIotMessage)
    fun onSuccess()
    fun onFailure()
    fun onTimeout()
}