package com.example.mqtt


import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.example.mqtt.listener.StreamerListener
import com.example.mqtt.listener.SubscriberListener
import com.example.mqtt.util.SSLUtil
import javax.net.ssl.SSLSocketFactory

class MQTTStreamer(
    var certificate: String,
    var privateKey: String,
    var rootCA: String,
    var url: String,
    var clientId: String,
    var timeout: Long,
    var autoConnect: Boolean,
    var listener: StreamerListener
) {

    private lateinit var client: AWSIotMqttClient

    init {
        connect()
    }

    fun connect() {
        val socketfactory: SSLSocketFactory =
            SSLUtil.getSocketFactory(rootCA, certificate, privateKey, "")

        //Instantiates a new client using TLS 1.2 mutual authentication.
        client = GatewayMQTTClient(url, clientId, socketfactory, listener)
        client.connect(timeout, false)
        client.maxConnectionRetries = 10
        client.keepAliveInterval = 60
    }

    fun disconnect() {
        client.disconnect()
    }

    fun subscribe(topic: String, listener: SubscriberListener) {
        client.subscribe(GatewayTopic(topic, listener))
    }

    fun publish(topic: String, payload: String, listener: SubscriberListener) {
        client.publish(GatewayMessage(topic, AWSIotQos.QOS0, payload.toByteArray(), listener))
    }

    fun publish(topic: String, payload: ByteArray, listener: SubscriberListener) {
        client.publish(GatewayMessage(topic, AWSIotQos.QOS0, payload, listener))
    }

}