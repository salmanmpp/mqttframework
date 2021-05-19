package com.example.mqtt

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotQos
import com.example.mqtt.listener.SubscriberListener

class GatewayMessage(
    topic: String,
    qos: AWSIotQos,
    payload: ByteArray,
    private val listener: SubscriberListener
) : AWSIotMessage(topic, qos, payload) {

    override fun onSuccess() {
        super.onSuccess()
        listener.onSuccess()
    }

    override fun onFailure() {
        super.onFailure()
        listener.onFailure()
    }

    override fun onTimeout() {
        super.onTimeout()
        listener.onTimeout()
    }

}