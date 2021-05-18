package com.example.mqttframework

interface StreamerListener {
    fun onConnection()
    fun onConnectionFail()
    fun onDisconnection()
   // fun onNotification(topic: String, message:String)
    fun onPublishFail(topic: String)
    fun onPublishSuccess(topic: String)
    fun onPublishTimeout(topic: String)



}