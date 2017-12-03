package com.example.marcin.mqttclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_broker)
public class BrokerActivity extends AppCompatActivity {

    @ViewById
    EditText brokerIp;

    @ViewById
    EditText brokerPort;

    @Click(R.id.connectBrokerButton)
    void onClick(){
        String message = brokerIp.getText().toString() + ":" + brokerPort.getText().toString();
        MainActivity_.intent(BrokerActivity.this).brokerAddress(message).start();
    }
}
