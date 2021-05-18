package com.example.mqttframework

interface SubscriberListener {
   fun onMessage(topic: String, message: String)
   fun onSubscribeFail(topic: String)
   fun onSubscribeSucces(topic: String)
   fun onSubscribeTimeout(topic: String)
}