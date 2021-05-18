package com.example.mqttframework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connect()
    }

    fun connect(){
        println("hello world")
        val directory: String = filesDir.absolutePath
        val mqttStreamer: MQTTStreamer = MQTTStreamer()
        val listener:StreamerListener = object:StreamerListener{
            override fun onConnection() {
                println("successfully connected to AWS IOT")
            }

            override fun onConnectionFail() {
                println("connection failed")
            }

            override fun onDisconnection() {
                println("disconnected from aws iot")
            }




            override fun onPublishFail(topic: String) {
                println("publish failed on "+topic)
            }

            override fun onPublishSuccess(topic: String) {
                println("publish success on topic "+topic)
            }

            override fun onPublishTimeout(topic: String) {
                println("publish timeout on topic "+topic)
            }


        }
        println(directory)
        mqttStreamer?.init(directory+"/certificate.pem.crt",directory+"/private.pem.key",directory+"/rootCA.pem","a3e4k5wgvqo4ep-ats.iot.us-east-2.amazonaws.com","test",100000,true,listener)
        mqttStreamer?.connect()
        val subscriberListener = object :SubscriberListener{
            override fun onMessage(topic: String, message: String) {
                println("message received "+message)
            }

            override fun onSubscribeFail(topic: String) {
                println("subscriber fail on "+topic)
            }

            override fun onSubscribeSucces(topic: String) {
                println("subscriber Success on "+topic)
            }

            override fun onSubscribeTimeout(topic: String) {
               println("subscriber timeout on "+topic)
            }

        }
        mqttStreamer?.subscribe(arrayOf("test/topic1"),subscriberListener)
        //Thread.sleep(1000)
        mqttStreamer?.publish("test/topic1","helloworld")

    }
}