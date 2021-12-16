package com.example.mqttframework

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.services.iot.client.AWSIotMessage
import com.example.mqtt.MQTTStreamer
import com.example.mqtt.listener.PublishListener
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
        val clientId = "MSTEST10"
        mqttStreamer = MQTTStreamer(
            PATH_TO_APP_DIR+"bootstrap-certificate.pem.crt",
            PATH_TO_APP_DIR+"bootstrap-private.pem.key",
            "a1tav0i47qrik6-ats.iot.ap-south-1.amazonaws.com",
            clientId,
            100000,
            true,
            getStreamListener()
        )
        Log.d(TAG,"connecting to $clientId")
        mqttStreamer.connect()
    }

    fun callPublish(view: View) {
            val payload = "{\n" +
                    "\t\"eventType\": \"EG01\",\n" +
                    "\t\"make\": \"HMDGlobal\",\n" +
                    "\t\"model\": \"Nokia 2.4\"\n" +
                    "}"
            Log.d(TAG,"Publishing $payload")
            mqttStreamer.publish("cloud/bootstrap",payload, getPublishListener())
    }

    fun callSubscribe(view: View) {
        //val topics = arrayOf("test/topic1", "test/topic2", "test/topic3")
        mqttStreamer.subscribe("gateway/MSTEST10/bootstrap", getSubscriberListener())

    }
    fun callDisconnect(view: View){
        mqttStreamer.disconnect()
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
                Log.d(TAG, "message received " + message.stringPayload)
            }

            override fun onSuccess(topic:String) {
                Log.d(TAG, "subscriber Success on $topic")
            }

            override fun onFailure(topic:String) {
                Log.d(TAG, "subscriber fail on $topic")
            }

            override fun onTimeout(topic:String) {
                Log.d(TAG, "subscriber timeout on $topic")
            }
        }
    }

    private fun getPublishListener() : PublishListener{
        return object : PublishListener{
            override fun onSuccess(topic:String) {
                Log.d(TAG,"publish success on $topic")
            }

            override fun onFailure(topic:String) {
                Log.d(TAG,"publish failure on $topic")
            }

            override fun onTimeout(topic:String) {
                Log.d(TAG,"publish timeout on $topic")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttStreamer.disconnect()
    }

    companion object {
        const val PATH_TO_APP_DIR = "/storage/emulated/0/Mqtt_Test/"
    }

}