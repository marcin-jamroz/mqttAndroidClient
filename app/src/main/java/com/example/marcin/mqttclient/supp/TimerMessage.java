package com.example.marcin.mqttclient.supp;

import java.util.Objects;
import java.util.TimerTask;

/**
 * Created by marcin on 30.11.17.
 */

public class TimerMessage implements Runnable {

    MqttSupp mqttSupp;

    String moveType;
    String moveTime;
    String moveSpeed;

    public TimerMessage(String move, MqttSupp mqttS) {
        if (Objects.equals(move, "u") || Objects.equals(move, "d")) {
            moveType = move;
            moveSpeed = ",255";
            moveTime = "5000";
        } else if (Objects.equals(move, "r") || Objects.equals(move, "l")) {
            moveType = move;
            moveTime = "360";
            moveSpeed = "";
        } else if (Objects.equals(move, "s")) {
            moveType = move;
        }

        mqttSupp = mqttS;
    }

    @Override
    public void run() {
        mqttSupp.sendMessage(moveType + moveTime + moveSpeed);
    }
}
