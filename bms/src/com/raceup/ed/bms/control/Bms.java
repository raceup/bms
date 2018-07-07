/*
 *  Copyright 2016-2018 Race Up Electric Division
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.raceup.ed.bms.control;

import com.raceup.ed.bms.models.battery.Pack;
import com.raceup.ed.bms.models.stream.bms.BmsData;
import com.raceup.ed.bms.models.stream.bms.BmsValue;
import com.raceup.ed.bms.models.stream.serial.ArduinoSerial;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Battery management system (with multithreading support)
 * Provides data from arduino serial port
 */
public class Bms {
    public static final String TAG = "Bms";  // app settings
    public static final HashMap<BmsOperatingMode.OperatingMode, BmsOperatingMode> OPERATING_MODE;
    private ArduinoSerial arduino;

    static {
        OPERATING_MODE = new HashMap<>();
        OPERATING_MODE.put(BmsOperatingMode.OperatingMode.NORMAL,
                new BmsOperatingMode("N", "Normal")
        );
        OPERATING_MODE.put(BmsOperatingMode.OperatingMode.BALANCE,
                new BmsOperatingMode("B", "Balance")
        );
        OPERATING_MODE.put(BmsOperatingMode.OperatingMode.SLEEP,
                new BmsOperatingMode("S", "Sleep")
        );
        OPERATING_MODE.put(BmsOperatingMode.OperatingMode.DEBUG,
                new BmsOperatingMode("D", "Debug")
        );
    }

    private final Pack batteryPack;  // battery pack settings

    /**
     * Create new arduino binding with default baud rate
     *
     * @param batteryPack virtual battery pock to monitor
     */
    public Bms(ArduinoSerial arduino, Pack batteryPack) {
        this.arduino = arduino;
        this.batteryPack = batteryPack;  // create battery pack model
    }

    /**
     * Start reading data from arduino serial, wrapping it in BmsValue or
     * BmsLog and updating battery pack
     */
    public void start() {
        setNormalMode();  // start logging
        updateOrFail();
    }

    /**
     * Read last value in serial, parse, and return it
     *
     * @return last value from serial
     */
    public BmsData getNewestData() {
        try {
            String data = arduino.getRawData();
            System.out.println("Requesting newest data: " + data);
            if (data == null) {
                return null;
            }

            return new BmsData(
                    new JSONObject(
                            data
                    )
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get new data, parse and updateOrFail battery pack
     */
    private void updateOrFail() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    BmsData newestData = getNewestData();
                    if (newestData != null) {
                        if (newestData.isValueType()) {
                            updateBatteryPack(new BmsValue(newestData));
                        } else {
                            // todo logger.logBmsDataOrFail(new BmsLog
                            // (newestData));
                        }
                    }
                } catch (Throwable t) {
                    System.err.println(TAG + " has encountered some errors while " +
                            "updateOrFail()");
                }
            }
        }, 0, 100);
    }

    /**
     * Update battery pack with new data
     *
     * @param data new data coming from arduino
     */
    private void updateBatteryPack(BmsValue data) {
        if (data.isTemperature()) {
            if (data.isTemperature1()) {
                batteryPack.setTemperature1(
                        data.getSegment(),  // cell position
                        data.getBms(),
                        data.getValue()  // value
                );
            } else if (data.isTemperature2()) {
                batteryPack.setTemperature2(
                        data.getSegment(),  // cell position
                        data.getBms(),
                        data.getValue()  // value
                );
            }
        } else if (data.isVoltage()) {
            batteryPack.setVoltage(
                    data.getSegment(),
                    data.getBms(),
                    data.getCell(),
                    data.getValue()  // value
            );
        }
    }

    private void setMode(BmsOperatingMode.OperatingMode mode) {
        BmsOperatingMode command = OPERATING_MODE.get(BmsOperatingMode
                .OperatingMode.NORMAL);
        arduino.sendSerialDataOrFail(command.getArduinoCommand());
    }

    public void setNormalMode() {
        setMode(BmsOperatingMode.OperatingMode.NORMAL);
    }

    public void setBalancingMode() {
        setMode(BmsOperatingMode.OperatingMode.BALANCE);
    }

    public void setSleepMode() {
        setMode(BmsOperatingMode.OperatingMode.SLEEP);
    }

    public void setDebugMode() {
        setMode(BmsOperatingMode.OperatingMode.DEBUG);
    }

    public void close() {
        arduino.close();
    }
}
