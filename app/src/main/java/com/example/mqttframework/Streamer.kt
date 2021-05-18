package com.example.mqttframework

interface Streamer {
    fun init(certificate: String, privateKey: String,rootCA:String, url: String, clientId: String, timeout: Long,autoConnect: Boolean,listener: StreamerListener)
    fun connect()
    fun subscribe(topics: Array<String>,listener: SubscriberListener)
    fun publish(topic: String, payload: String)
    fun publish(topic: String, payload: ByteArray)
    fun disconnect()

}