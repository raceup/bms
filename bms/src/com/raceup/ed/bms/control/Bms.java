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

import com.raceup.ed.bms.logging.Debugger;
import com.raceup.ed.bms.models.battery.BmsStatus;
import com.raceup.ed.bms.models.battery.Pack;
import com.raceup.ed.bms.models.stream.bms.BmsData;
import com.raceup.ed.bms.models.stream.bms.BmsLog;
import com.raceup.ed.bms.models.stream.bms.BmsValue;
import com.raceup.ed.bms.models.stream.serial.ArduinoSerial;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Battery management system (with multithreading support)
 * Provides data from arduino serial port
 */
public class Bms extends Debugger implements Runnable {
    private ArduinoSerial arduino;
    private boolean stopRequest = false;
    private static final int WAIT_LOOP = 250;
    private static final int MAX_RETRIES = 10;
    private BmsStatus status = new BmsStatus(null);

    public static final HashMap<BmsOperatingMode.OperatingMode, BmsOperatingMode> OPERATING_MODE;

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

    public final Pack getBatteryPack() {
        return batteryPack;
    }

    /**
     * Create new arduino binding with default baud rate
     *
     * @param batteryPack virtual battery pock to monitor
     */
    public Bms(ArduinoSerial arduino, Pack batteryPack) {
        super("BMS", true);
        this.arduino = arduino;
        this.batteryPack = batteryPack;  // create battery pack model

        setup();
    }

    public void setup() {
        setNormalMode();  // start logging
    }

    public ArrayList<BmsData> getNewestData() {
        String[] buffer = arduino.getRawData();
        ArrayList<BmsData> parsed = new ArrayList<>();
        for (String data : buffer) {
            try {
                parsed.add(new BmsData(new JSONObject(data)));
            } catch (Exception e) {
            }
        }
        return parsed;
    }

    /**
     * Update battery pack with new data
     *
     * @param data new data coming from arduino
     */
    private void updateBatteryPack(BmsValue data) {
        int bms = data.getBms() - 1;
        double value = data.getValue();

        if (data.isTemperature()) {
            if (data.isTemperature1()) {
                batteryPack.setTemperature1(bms, value);
            } else if (data.isTemperature2()) {
                batteryPack.setTemperature2(bms, value);
            }
        } else if (data.isVoltage()) {
            batteryPack.setVoltage(bms, data.getCell() - 1, value);
        }
    }

    private void updateStatus(BmsLog log) {
        status.update(log);
    }

    public void setMode(BmsOperatingMode.OperatingMode mode) {
        BmsOperatingMode command = OPERATING_MODE.get(mode);
        arduino.sendSerialData(command.getArduinoCommand(), MAX_RETRIES);
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
        stopRequest = true;
        arduino.close();
    }

    @Override
    public void run() {
        while (!stopRequest) {
            try {
                loop();
                Thread.sleep(WAIT_LOOP);
            } catch (Exception e) {
                System.err.println(TAG + e.toString());
            }
        }
    }

    public void loop() {
        ArrayList<BmsData> newestData = getNewestData();
        for (BmsData data : newestData) {
            if (data != null)
                if (data.isValueType()) {
                    updateBatteryPack(new BmsValue(data));
                } else if (data.isStatusType()) {
                    updateStatus(new BmsLog(data));
                }
        }
    }

    public String getCurrentStatus() {
        return status.getStatus();
    }
}
