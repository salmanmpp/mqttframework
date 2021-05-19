package com.example.mqttframework

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.services.iot.client.AWSIotMessage
import com.example.mqtt.MQTTStreamer
import com.example.mqtt.listener.StreamerListener
import com.example.mqtt.listener.SubscriberListener

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var mqttStreamer: MQTTStreamer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connect()
    }

    private fun connect() {
        val directory: String = filesDir.absolutePath
        mqttStreamer = MQTTStreamer(
            "$directory/certificate.pem.crt",
            "$directory/private.pem.key",
            "$directory/rootCA.pem",
            "a3e4k5wgvqo4ep-ats.iot.us-east-2.amazonaws.com",
            "test",
            100000,
            true,
            getStreamListener()
        )
        //mqttStreamer.connect()
    }

    fun callPublish(view: View) {
        mqttStreamer.publish("test/topic1", "helloworld", getSubscriberListener())
    }

    fun callSubscribe(view: View) {
        //val topics = arrayOf("test/topic1", "test/topic2", "test/topic3")
        mqttStreamer.subscribe("test/topic1", getSubscriberListener())
    }

    private fun getStreamListener(): StreamerListener {
        return object : StreamerListener {

            override fun onConnectionSuccess() {
                Log.d(TAG, "successfully connected to AWS IOT")
            }

            override fun onConnectionFailure() {
                Log.d(TAG, "connection failed")
            }

            override fun onConnectionClosed() {
                Log.d(TAG, "connection closed from aws iot")
            }

        }
    }

    private fun getSubscriberListener(): SubscriberListener {
        return object : SubscriberListener {
            override fun onMessage(message: AWSIotMessage) {
                Log.d(TAG, "message received " + message.payload)
            }

            override fun onSuccess() {
                Log.d(TAG, "subscriber Success on ")
            }

            override fun onFailure() {
                Log.d(TAG, "subscriber fail on ")
            }

            override fun onTimeout() {
                Log.d(TAG, "subscriber timeout on ")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttStreamer.disconnect()
    }

}