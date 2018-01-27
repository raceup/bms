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


package com.raceup.ed.bms;

import com.raceup.ed.bms.battery.Pack;
import com.raceup.ed.bms.stream.ArduinoSerial;
import com.raceup.ed.bms.stream.Logger;
import com.raceup.ed.bms.stream.bms.data.BmsData;
import com.raceup.ed.bms.stream.bms.data.BmsLog;
import com.raceup.ed.bms.stream.bms.data.BmsValue;
import org.json.JSONObject;


/**
 * Battery management system (with multithreading support)
 * Provides data from arduino serial port
 */
public class Bms extends ArduinoSerial implements Runnable, StartAndStop {
    public static final String appName = "BmsManager";  // app settings
    static final String appVersion = "2.4";
    private static final String TAG = "BmsUtils";
    private static final String ARDUINO_START_LOGGING_MSG = "H";  // send
    // this message to ask Arduino to start logging
    private static final String ARDUINO_BALANCE_MSG = "B";  // send this
    // message to ask Arduino to balance segments
    final Pack batteryPack;  // battery pack settings
    private final Logger logger;  // log data to file
    int msLogIntervalUpdate = 1000;  // logging interval update
    private long nextLogIntervalUpdate = System.currentTimeMillis() + (long)
            msLogIntervalUpdate;  // time to log data
    private volatile boolean amIStarted = false;  // start and stop management
    private volatile boolean amIPaused = false;
    private volatile boolean amIStopped = false;

    /**
     * Create new arduino binding with default baud rate
     *
     * @param BAUD_RATE   symbol read rate
     * @param logger      logger to log new data
     * @param batteryPack virtual battery pock to monitor
     */
    public Bms(int BAUD_RATE, Logger logger, Pack batteryPack) {
        super(BAUD_RATE, System.out);

        this.logger = logger;  // logger
        this.batteryPack = batteryPack;  // create battery pack model
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
            close();  // close arduino serial
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
    BmsData getNewestData() {
        String data = getRawData();  // todo BmsUtils.getRandomData().trim();
        System.out.println("Requesting newest data: " + data);
        if (data == null) {
            return null;
        }

        return new BmsData(
                new JSONObject(
                        data
                )
        );
    }

    /**
     * Get new data, parse and updateOrFail battery pack
     */
    private void updateOrFail() {
        try {
            BmsData newestData = getNewestData();
            if (newestData != null) {
                if (newestData.isValueType()) {
                    updateBatteryPack(new BmsValue(newestData));
                } else {
                    updateLogs(new BmsLog(newestData));
                }
            }
            Thread.sleep(msSerialIntervalUpdate);
        } catch (NullPointerException | InterruptedException e) {
            System.err.println(TAG + " has encountered some errors while " +
                    "updateOrFail()");
        }
    }

    /**
     * Update logger with new data
     * @param log new log
     */
    private void updateLogs(BmsLog log) {
        if (System.currentTimeMillis() >= nextLogIntervalUpdate) {
            logger.logBmsDataOrFail(log);
            nextLogIntervalUpdate = System.currentTimeMillis() +
                    msLogIntervalUpdate;
        }
    }

    /**
     * Update battery pack with new data
     *
     * @param data new data coming from arduino
     */
    private void updateBatteryPack(BmsValue data) {
        if (data.isTemperature()) {  // retrieve coordinate of cells and set
            // value
            batteryPack.setTemperature(
                    data.getSegment(),  // cell position
                    data.getCell(),
                    data.getValue()  // value
            );
        } else if (data.isVoltage()) {  // retrieve coordinate of cells and
            // set value
            batteryPack.setVoltage(
                    data.getSegment(),  // cell position
                    data.getCell(),
                    data.getValue()  // value
            );
        }
    }

    /**
     * Asks Arduino to start logging data
     */
    private void askArduinoToStartLogging() {
        // todo sendSerialDataOrFail(ARDUINO_START_LOGGING_MSG);
    }

    /**
     * Asks Arduino to balance segments
     */
    void askArduinoToBalanceCells() {
        // todo sendSerialDataOrFail(ARDUINO_BALANCE_MSG);
    }
}
