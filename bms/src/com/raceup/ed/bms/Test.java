package com.raceup.ed.bms;

import com.raceup.ed.bms.stream.ArduinoSerial;

import java.util.Timer;
import java.util.TimerTask;

public class Test {
    public static void main(String[] args) {
        ArduinoSerial serial = new ArduinoSerial(115200);
        serial.setup();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println(System.currentTimeMillis());
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
        }, 0, 100);
    }
}