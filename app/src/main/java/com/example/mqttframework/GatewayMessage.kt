package com.example.mqttframework

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotQos

class GatewayMessage(topic:String,qos: AWSIotQos,payload:ByteArray,listener: StreamerListener):AWSIotMessage(topic,qos, payload) {
    val listener = listener
    override fun onFailure() {
        super.onFailure()
        listener.onPublishFail(topic)
    }

    override fun onSuccess() {
        super.onSuccess()
        listener.onPublishSuccess(topic)
    }

    override fun onTimeout() {
        super.onTimeout()
        listener.onPublishTimeout(topic)
    }
}