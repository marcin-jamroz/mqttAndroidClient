package com.example.marcin.mqttclient;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.marcin.mqttclient.supp.MqttSupp;
import com.example.marcin.mqttclient.supp.TimerMessage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    MqttSupp mqttSupp;
    Timer timer;
    String lastAction = "u";

    @ViewById
    TextView receivedMessage;

    @ViewById
    EditText sendMessage;

    @AfterViews
    protected void startMqtt() {
        mqttSupp = new MqttSupp(getApplicationContext());
        mqttSupp.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                receivedMessage.setText(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    @Click(R.id.sendButton)
    void sendMesage() {
        mqttSupp.sendMessage(sendMessage.getText().toString());
    }

    @Click(R.id.arrow_up)
    void onUp() {
        mqttSupp.sendMessage("u5000,255");
    }

    @Click(R.id.arrow_down)
    void onDown() {
        mqttSupp.sendMessage("d5000,255");
    }

    @Click(R.id.arrow_right)
    void onRight() {
        mqttSupp.sendMessage("r360");
    }

    @Click(R.id.arrow_left)
    void onLeft() {
        mqttSupp.sendMessage("l360");
    }

    @Click(R.id.stop)
    void onStopPressed() {
        mqttSupp.sendMessage("s");
    }

}
