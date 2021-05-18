package com.example.mqttframework

import com.amazonaws.services.iot.client.AWSIotMqttClient
import javax.net.ssl.SSLSocketFactory

class GatewayMQTTClient(url: String, clientId: String,socketFactory: SSLSocketFactory,listener: StreamerListener): AWSIotMqttClient(url,clientId,socketFactory) {
    var listener: StreamerListener = listener
    override fun onConnectionSuccess() {
        super.onConnectionSuccess()
        println("connection Success")
        listener.onConnection()
    }

    override fun onConnectionFailure() {
        super.onConnectionFailure()
        listener.onConnectionFail()
    }

    override fun disconnect() {
        super.disconnect()
        listener.onDisconnection()
    }

}