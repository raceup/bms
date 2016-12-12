/*
 *  Copyright 2016-2017 RaceUp ED
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
import com.raceup.ed.bms.stream.bms.data.Data;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Battery management system (with multithreading support)
 * Provides data from arduino serial port
 */
public class Bms extends ArduinoSerial implements Runnable, StartAndStop {
    public static final String appName = "Bms manager";  // app settings
    public static final String appVersion = "2.1";

    final Pack batteryPack;  // battery pack settings
    private final Logger logger;  // log data to file
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
     * Parse raw data
     *
     * @param rawData raw data string from arduino
     * @return parsed data
     */
    private static BmsData parseData(String rawData) {
        JSONTokener tokener = new JSONTokener(rawData);
        JSONObject root = new JSONObject(tokener);
        return new BmsData(
                root.getString("type"),
                root.getString("cell"),
                root.getString("segment"),
                root.getString("value")
        );
    }

    /*
     * Thread
     */

    /**
     * Start reading data from arduino serial, wrapping it in BmsValue or BmsLog and updating battery pack
     */
    public void start() {
        if (!amIStopped && !amIStarted) {  // only when i'm not stopped
            amIStarted = true;  // start updating

            if (!amIPaused) {  // firs time
                new Thread(this, this.getClass().getName()).start();  // start in new thread
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
    public BmsData getNewestData() {
        return parseData(getNewestRawData());
    }

    /**
     * Getter of raw data
     *
     * @return raw data
     */
    private String getNewestRawData() {
        String typeData;
        int randomSegment = (int) (Math.random() * batteryPack.getNumberOfCellsPerSegment().length);
        int randomCell = (int) (Math.random() * batteryPack.getNumberOfCellsPerSegment()[randomSegment]);
        String randomValue;

        if (Math.random() > 0.5) {  // half of the time generate a value
            typeData = Data.values()[(int) (4 + Math.random() * 2)].toString();  // temperature or voltage
            randomValue = Double.toString(Math.random() * 4400);
        } else {  // the other time generate a log
            typeData = Data.values()[(int) (4 * Math.random())].toString();  // log
            randomValue = "Oh no! We received a " + typeData + "!";
        }
//        return "{" +
//                "\"type\":\"" + typeData + "\"," +
//                "\"cell\":\"" + Integer.toString(randomCell) + "\"," +
//                "\"segment\":\"" + Integer.toString(randomSegment) + "\"," +
//                "\"value\":\"" + randomValue + "\"}";

        // TODO: remove this for real data coming from arduino
        return serialData;
    }

    /*
     * Battery manager
     */

    /**
     * Get new data, parse and updateOrFail battery pack
     */
    private void updateOrFail() {
        try {
            BmsData newestData = getNewestData();  // retrieve newest data from arduino
            logger.logBmsDataOrFail(newestData);  // log new data

            if (newestData != null) {
                if (newestData.isValue()) {  // updateOrFail series bms with new data
                    updateBatteryPack(new BmsValue(newestData));
                }
                Thread.sleep(msSerialIntervalUpdate);  // wait for next updateOrFail series
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Update battery pack with new data
     *
     * @param data new data coming from arduino
     */
    private void updateBatteryPack(BmsValue data) {
        if (data.isTemperature()) {  // retrieve coordinate of cells and set value
            batteryPack.setTemperatureOfCell(
                    data.getSegment(),  // cell position
                    data.getCell(),
                    data.getValue()  // value
            );
        } else if (data.isVoltage()) {  // retrieve coordinate of cells and set value
            batteryPack.setVoltageOfCell(
                    data.getSegment(),  // cell position
                    data.getCell(),
                    data.getValue()  // value
            );
        }
    }

    /**
     * Balances cell of segment or fail
     *
     * @param cell          number of cell to balance
     * @param segmentOfCell number of segment of cell to balance
     */
    public void balanceCellOrFail(int cell, int segmentOfCell) {
        try {
            writeToSerial("BALANCE");  // TODO: send arduino message to balance cell in segment
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
