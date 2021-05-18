package com.example.mqttframework


import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.example.mqttframework.util.SSLUtil
import javax.net.ssl.SSLSocketFactory

class MQTTStreamer : Streamer {
    private lateinit var certificate: String
    private lateinit var privateKey: String
    private  lateinit var url: String
    private lateinit var clientId: String
    private  var timeout: Long = 0
    private  var autoConnect: Boolean= true
    private lateinit var listener: StreamerListener
    private var client: AWSIotMqttClient?=null
    private lateinit var rootCA:String


    override fun init(
            certificate: String,
            privateKey: String,
            rootCA:String,
            url: String,
            clientId: String,
            timeout: Long,
            autoConnect: Boolean,

            listener: StreamerListener
    ) {

        this.certificate = certificate
        this.privateKey = privateKey
        this.url = url
        this.clientId = clientId
        this.timeout = timeout
        this.autoConnect = autoConnect
        this.rootCA=rootCA
        this.listener = listener
    }

    override fun connect() {
        val socketfactory: SSLSocketFactory = SSLUtil.getSocketFactory(rootCA,certificate,privateKey,"")

        //Instantiates a new client using TLS 1.2 mutual authentication.
        client = GatewayMQTTClient(url,clientId,socketfactory,listener)
        client?.connect(timeout,false)
        client?.maxConnectionRetries = 10
        client?.keepAliveInterval = 60



    }

    override fun subscribe(topics: Array<String>,subscriberListener: SubscriberListener) {
        for(topic in topics)
        {
            val iotTopic = GatewayTopic(topic,subscriberListener)
            client?.subscribe(iotTopic)
        }
    }


    override fun publish(topic: String, payload: String) {
        client?.publish(GatewayMessage(topic,AWSIotQos.QOS0,payload.toByteArray(),listener))
    }

    override fun publish(topic: String, payload: ByteArray) {
        client?.publish(GatewayMessage(topic,AWSIotQos.QOS0,payload,listener))
    }

    override fun disconnect() {
        client?.disconnect()
    }
}