package com.example.marcin.mqttclient;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.marcin.mqttclient.supp.HistoryAdapter;
import com.example.marcin.mqttclient.supp.MqttSupp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    MqttSupp mqttSupp;
    final private int minSpeed = 155;
    HistoryAdapter mHistoryAdapter;
    boolean isRobotConnected = false;

    @Extra
    String brokerAddress;

    @ViewById
    TextView speedValue;

    @ViewById
    RecyclerView recyclerView;

    @AfterViews
    void setupRecycleViewer(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHistoryAdapter = new HistoryAdapter(new ArrayList<String>());
        recyclerView.setAdapter(mHistoryAdapter);
    }

    @AfterViews
    protected void startMqtt() {
        mqttSupp = new MqttSupp(getApplicationContext(), brokerAddress, mHistoryAdapter);
        mqttSupp.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
mHistoryAdapter.add(0, "Połaczono z brokerem");
mqttSupp.sendMessage("c");
            }

            @Override
            public void connectionLost(Throwable cause) {
mHistoryAdapter.add(0, "Rozłączono");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if(!isRobotConnected && Objects.equals(message.toString(), " 0")){
                    mHistoryAdapter.add(0, "Połączono z robotem");
                    isRobotConnected = true;
                } else {
                    mHistoryAdapter.add(0, topic + ": " + message.toString());
                    recyclerView.smoothScrollToPosition(0);
                }
                Log.i("MQTT", message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

       // mqttSupp.sendMessage("c");
    }

    @SeekBarProgressChange(R.id.speedBar)
    void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        speedValue.setText("Prędkość:" + String.valueOf(progress));
    }

    @Touch(R.id.arrow_up)
    void onUp(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int speed = Integer.valueOf(speedValue.getText().toString().split(":")[1]) + minSpeed;
            mqttSupp.sendMessage("u5000," + String.valueOf(speed));
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            mqttSupp.sendMessage("s");
        }
    }

    @Touch(R.id.arrow_down)
    void onDown(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int speed = Integer.valueOf(speedValue.getText().toString().split(":")[1]) + minSpeed;
            mqttSupp.sendMessage("d5000," + String.valueOf(speed));
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            mqttSupp.sendMessage("s");
        }
    }

    @Touch(R.id.arrow_right)
    void onRight(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int speed = Integer.valueOf(speedValue.getText().toString().split(":")[1]) + minSpeed;
            mqttSupp.sendMessage("r360," + String.valueOf(speed));
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            mqttSupp.sendMessage("s");
        }
    }

    @Touch(R.id.arrow_left)
    void onLeft(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int speed = Integer.valueOf(speedValue.getText().toString().split(":")[1]) + minSpeed;
            mqttSupp.sendMessage("l360," + String.valueOf(speed));
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            mqttSupp.sendMessage("s");
        }
    }

    @Click(R.id.stop)
    void onStopClicked() {
        mqttSupp.sendMessage("s");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttSupp.disconnect();
    }
}
