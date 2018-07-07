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

import com.raceup.ed.bms.StartAndStop;
import com.raceup.ed.bms.models.battery.Pack;
import com.raceup.ed.bms.models.stream.Logger;
import com.raceup.ed.bms.models.stream.bms.BmsData;
import com.raceup.ed.bms.models.stream.bms.BmsValue;
import com.raceup.ed.bms.models.stream.serial.ArduinoSerial;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Battery management system (with multithreading support)
 * Provides data from arduino serial port
 */
public class Bms implements Runnable, StartAndStop {
    public static final String appName = "BmsManager";  // app settings
    static final String appVersion = "3.0";
    private static final String TAG = "BmsUtils";
    private static final String ARDUINO_START_LOGGING_MSG = "H";  // send
    // this message to ask Arduino to start logging
    private static final String ARDUINO_BALANCE_MSG = "B";  // send this
    // message to ask Arduino to balance segments
    final Pack batteryPack;  // battery pack settings
    private final Logger logger;  // log data to file
    private ArduinoSerial arduino;
    int msLogIntervalUpdate = 1000;  // logging interval update
    private volatile boolean amIStarted = false;  // start and stop management
    private volatile boolean amIPaused = false;
    private volatile boolean amIStopped = false;

    /**
     * Create new arduino binding with default baud rate
     *
     * @param batteryPack virtual battery pock to monitor
     */
    public Bms(ArduinoSerial arduino, Pack batteryPack) {
        this.arduino = arduino;
        this.batteryPack = batteryPack;  // create battery pack model
        this.logger = null;  // logger
    }

    /**
     * Start reading data from arduino serial, wrapping it in BmsValue or
     * BmsLog and updating battery pack
     */
    public void start() {
        if (!amIStopped && !amIStarted) {  // only when i'm not stopped
            amIStarted = true;  // start updating
            askArduinoToStartLogging();  // start logging

            if (!amIPaused) {  // first time
                new Thread(this, this.getClass().getName()).start();  //
                // start in new thread
            }
        }
    }

    /**
     * Pause reading data from serial
     */
    public void pause() {
        amIStarted = false;  // stop updating
        amIPaused = true;
    }

    /**
     * Close serial port and never re-open it
     */
    public void stop() {
        amIStarted = false;
        amIPaused = false;
        amIStopped = true;

        try {
            arduino.close();  // close arduino serial
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Monitoring BMS
     * 1) read newest value from serial
     * 2) updateOrFail series values in BMS model
     * 3) loop
     */
    public void run() {  // exit thread only when stopped
        while (!amIStopped) {
            while (amIStarted) {
                updateOrFail();
            }
        }
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

    /**
     * Asks Arduino to start logging data
     */
    private void askArduinoToStartLogging() {
        arduino.sendSerialDataOrFail(ARDUINO_START_LOGGING_MSG);
    }

    /**
     * Asks Arduino to balance segments
     */
    void askArduinoToBalanceCells() {
        arduino.sendSerialDataOrFail(ARDUINO_BALANCE_MSG);
    }

    public void close() {
        arduino.close();
    }
}
