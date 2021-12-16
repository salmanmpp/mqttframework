package com.example.mqtt.listener

import com.amazonaws.services.iot.client.AWSIotMessage

interface SubscriberListener {
    fun onMessage(message: AWSIotMessage)
    fun onSuccess(topic:String)
    fun onFailure(topic:String)
    fun onTimeout(topic:String)
}