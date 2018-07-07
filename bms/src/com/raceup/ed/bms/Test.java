package com.raceup.ed.bms;

import com.raceup.ed.bms.stream.ArduinoSerial;

public class Test {
    public static void main(String[] args) {
        ArduinoSerial serial = new ArduinoSerial(115200);
        serial.setup();
    }

}