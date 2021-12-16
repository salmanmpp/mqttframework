package com.example.mqtt

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotQos
import com.example.mqtt.listener.PublishListener

class GatewayMessage(
    topic: String,
    qos: AWSIotQos,
    payload: ByteArray,
    private val listener: PublishListener
) : AWSIotMessage(topic, qos, payload) {

    override fun onSuccess() {
        super.onSuccess()
        listener.onSuccess(topic)
    }

    override fun onFailure() {
        super.onFailure()
        listener.onFailure(topic)
    }

    override fun onTimeout() {
        super.onTimeout()
        listener.onTimeout(topic)
    }

}