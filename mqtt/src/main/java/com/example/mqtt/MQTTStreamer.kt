package com.example.mqtt


import android.util.Log
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.example.mqtt.listener.PublishListener
import com.example.mqtt.listener.StreamerListener
import com.example.mqtt.listener.SubscriberListener
import com.example.mqtt.util.SSLUtil
import javax.net.ssl.SSLSocketFactory

class MQTTStreamer(
    var certificate: String,
    var privateKey: String,
    var url: String,
    var clientId: String,
    var timeout: Long,
    var autoConnect: Boolean,
    var listener: StreamerListener
) {

    private lateinit var client: AWSIotMqttClient
    val TAG = "MQTTStreamer"


    fun connect() {
        try {
            val socketfactory: SSLSocketFactory =
                SSLUtil.getSocketFactory(certificate, privateKey, "")

            //Instantiates a new client using TLS 1.2 mutual authentication.
            client = GatewayMQTTClient(url, clientId, socketfactory, listener)
            client.maxConnectionRetries = 100
            client.connect(timeout, false)
            if(!autoConnect){
                client.maxConnectionRetries = 0
            }
        }catch (exception:Exception){
            Log.e(TAG,exception.localizedMessage)
        }

    }

    fun disconnect() {
        try{
            client.disconnect()
        }catch (exception:Exception){
            Log.e(TAG,exception.localizedMessage)
        }

    }

    fun subscribe(topic: String, listener: SubscriberListener) {

            client.subscribe(GatewayTopic(topic, listener))

    }

    fun subscribe(topics:Array<String>,listener: SubscriberListener){

            for (topic in topics) {
                client.subscribe(GatewayTopic(topic, listener))
            }

    }

    fun publish(topic: String, payload: String, listener: PublishListener) {

            client.publish(GatewayMessage(topic, AWSIotQos.QOS1, payload.toByteArray(), listener))

    }

    fun publish(topic: String, payload: ByteArray, listener: PublishListener) {

            client.publish(GatewayMessage(topic, AWSIotQos.QOS1, payload, listener))

    }

}