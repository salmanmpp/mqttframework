package com.example.mqtt

import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.example.mqtt.listener.StreamerListener
import javax.net.ssl.SSLSocketFactory

class GatewayMQTTClient(
    url: String,
    clientId: String,
    socketFactory: SSLSocketFactory,
    private var listener: StreamerListener
) : AWSIotMqttClient(url, clientId, socketFactory) {

    override fun onConnectionSuccess() {
        super.onConnectionSuccess()
        listener.onConnectionSuccess()
    }

    override fun onConnectionFailure() {
        super.onConnectionFailure()
        listener.onConnectionFailure()
    }

    override fun onConnectionClosed() {
        super.onConnectionClosed()
        listener.onConnectionClosed()
    }

}