package com.example.mqtt


import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotTopic
import com.example.mqtt.listener.SubscriberListener

class GatewayTopic(topic: String, private val listener: SubscriberListener) : AWSIotTopic(topic) {

    override fun onMessage(message: AWSIotMessage) {
        super.onMessage(message)
        listener.onMessage(message)
    }

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