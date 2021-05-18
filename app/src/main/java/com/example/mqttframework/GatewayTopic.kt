package com.example.mqttframework


import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotTopic

class GatewayTopic(topic: String, listener: SubscriberListener) : AWSIotTopic(topic) {
    val listener : SubscriberListener = listener

    override fun onMessage(message: AWSIotMessage?) {
        super.onMessage(message)
        //listener.onNotification(topic, message!!)
        listener.onMessage(topic, message!!.stringPayload)
    }

    override fun onFailure() {
        super.onFailure()
        listener.onSubscribeFail(topic)
    }

    override fun onSuccess() {
        super.onSuccess()
        listener.onSubscribeSucces(topic)
    }

    override fun onTimeout() {
        super.onTimeout()
        listener.onSubscribeTimeout(topic)
    }

}