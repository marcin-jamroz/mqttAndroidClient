package com.example.marcin.mqttclient.supp;

import android.content.Context;
import android.util.Log;

import com.example.marcin.mqttclient.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by marcin on 29.11.17.
 */

public class MqttSupp {
    public MqttAndroidClient mqttAndroidClient;
    HistoryAdapter mHistoryAdapter;

    String brokerUri;
    String clientId;
    String answerTopic;
    String sendTopic;
    String errorTopic;


    public MqttSupp(Context context, String brokerUri, HistoryAdapter historyAdapter) {
        this.brokerUri = "tcp://" + brokerUri;
        mHistoryAdapter = historyAdapter;
        clientId = context.getString(R.string.client_id);
        answerTopic = context.getString(R.string.answer_topic);
        sendTopic = context.getString(R.string.send_topic);
        errorTopic = context.getString(R.string.error_topic);
        mqttAndroidClient = new MqttAndroidClient(context, this.brokerUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("MQTT", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("MQTT", message.toString());
                mHistoryAdapter.add(0, message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        connect();
    }

    private void connect() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic(answerTopic);
                    subscribeToTopic(errorTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("MQTT", "Connection error to: " + brokerUri + exception.toString());
                    mHistoryAdapter.add(0, "Problem z połączeniem z brokerem");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            mqttAndroidClient.disconnect();
            mHistoryAdapter.add(0, "Rozłączono z brokerem");
        } catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void subscribeToTopic(final String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("MQTT", "Subscription success");
                    mHistoryAdapter.add(0, "Zasubskrybowano do " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("MQTT", "Subscription fail: " + exception.toString());
                    mHistoryAdapter.add(0, "Problem z subskrypcją do " + topic);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void sendMessage(String message) {
        try {
            mqttAndroidClient.publish(sendTopic, new MqttMessage(message.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
