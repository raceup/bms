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
import com.raceup.ed.bms.stream.bms.data.BmsValue;
import org.json.JSONObject;


/**
 * Battery management system (with multithreading support)
 * Provides data from arduino serial port
 */
public class Bms extends ArduinoSerial implements Runnable, StartAndStop {
    public static final String appName = "BmsManager";  // app settings
    static final String appVersion = "2.4";
    private static final String TAG = "Bms";
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

    /*
     * Thread
     */

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

    /*
     * Recharge
     */

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

    /*
     * Data
     */

    /**
     * Read last value in serial, parse, and return it
     *
     * @return last value from serial
     */
    BmsData getNewestData() {
        return new BmsData(new JSONObject(getNewestRawData()));
    }

    /**
     * Getter of raw data
     *
     * @return raw data
     */
    private String getNewestRawData() {
        String type, value;
        double randTypeNum = Math.random();
        double minCell = 0, maxCell = 18;
        double minSegment = 0, maxSegment = 8;
        double randCell = minCell + Math.random() * (maxCell - minCell);
        double randSegment = minSegment + Math.random() * (maxSegment -
                minCell);

        String cell = Integer.toString((int) randCell);
        String segment = Integer.toString((int) randSegment);

        double rawValueProb = 0.75;

        if (randTypeNum < rawValueProb) {  // generate random value
            if (randTypeNum < rawValueProb / 2) {  // value is voltage
                type = "voltage";

                double minVolt = 3950, maxVolt = 4050;
                double randVolt = minVolt + Math.random() * (maxVolt -
                        minVolt);
                value = Integer.toString((int) randVolt);
            } else {  // value is temperature
                type = "temperature";

                double minTemp = 25, maxTemp = 45;
                double randTemp = minTemp + Math.random() * (maxTemp -
                        minTemp);
                value = Double.toString(randTemp);
            }
        } else {  // generate random status
            double randStatusNum = Math.random();
            if (randStatusNum < 1.0 / 3) {
                type = "Alert";
                value = "Possible warning in cell " + cell + ", segment " +
                        segment;
            } else if (randStatusNum < 2.0 / 3) {
                type = "Fault";
                value = "Fault in cell " + cell + ", segment " + segment;
            } else {
                type = "Log";
                value = "All right down here for as far as I can tell";
            }
        }

        String data = getRawDataFromValues(type, cell, segment, value);
        // TODO: debug only String data = serialData;
        return data.trim();
    }

    /**
     * Gets raw data from pre-defined values
     *
     * @param type    type of data
     * @param cell    number of cell broadcasting value
     * @param segment number of segment broadcasting value
     * @param value   value of cell of segment
     * @return raw data from pre-defined values
     */
    private String getRawDataFromValues(String type, String cell, String
            segment, String value) {
        BmsData data = new BmsData(type, cell, segment, value);
        return data.getJsonValue();
    }


    /*
     * Battery manager
     */

    /**
     * Get new data, parse and updateOrFail battery pack
     */
    private void updateOrFail() {
        try {
            BmsData newestData = getNewestData();  // retrieve newest data
            // from arduino
            if (newestData != null) {
                if (newestData.isValueType()) {  // updateOrFail series bms
                    // with new data
                    updateBatteryPack(new BmsValue(newestData));
                }

                updateLogs();
            }
            Thread.sleep(msSerialIntervalUpdate);  // wait for next
            // updateOrFail series
        } catch (NullPointerException | InterruptedException e) {
            System.err.println(TAG + " has encountered some errors while " +
                    "updateOrFail()");
        }
    }

    /**
     * Update logger with new data
     */
    private void updateLogs() {
        if (System.currentTimeMillis() >= nextLogIntervalUpdate) {  // time
            // to log
            logger.logBmsDataOrFail(getNewestData());
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
            batteryPack.setTemperatureOfCell(
                    data.getSegment(),  // cell position
                    data.getCell(),
                    data.getValue()  // value
            );
        } else if (data.isVoltage()) {  // retrieve coordinate of cells and
            // set value
            batteryPack.setVoltageOfCell(
                    data.getSegment(),  // cell position
                    data.getCell(),
                    data.getValue()  // value
            );
        }
    }

    /*
     * I/O with Arduino
     */

    /**
     * Asks Arduino to start logging data
     */
    private void askArduinoToStartLogging() {
        sendSerialDataOrFail(ARDUINO_START_LOGGING_MSG);
    }

    /**
     * Asks Arduino to balance segments
     */
    void askArduinoToBalanceCells() {
        sendSerialDataOrFail(ARDUINO_BALANCE_MSG);
    }
}
