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

package com.raceup.ed.bms.stream;

import com.raceup.ed.bms.Bms;
import com.raceup.ed.bms.stream.bms.data.BmsData;
import com.raceup.ed.bms.stream.bms.data.BmsLog;
import com.raceup.ed.bms.stream.bms.data.BmsValue;
import com.raceup.ed.bms.utils.Streams;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Log bms data to local file
 */
public class Logger {
    private static final String logFolderPath = Paths.get(Bms.appName, "log", Long.toString(System.currentTimeMillis())).toString();  // name of folder that stores logs
    private static final String[] nameOfLogFiles = new String[]{"status.csv", "voltage.csv", "temperature.csv"};
    private static final File[] logFiles = new File[]{null, null, null};

    /**
     * New logger
     *
     * @param pathToFolder path to folder chosen to place logs there
     */
    public Logger(String pathToFolder) {
        File logFolder = new File(pathToFolder, logFolderPath);
        for (int i = 0; i < nameOfLogFiles.length; i++) {
            logFiles[i] = new File(logFolder, nameOfLogFiles[i]);
        }

        createLogFilesOrFail();  // create files if they don't exist
    }

    /*
     * Update
     */

    /**
     * Appends to log files new data
     *
     * @param data new data to append
     */
    public void logBmsDataOrFail(BmsData data) {
        try {
            if (data.isLog()) {  // if it's a log, update log frame
                logBmsLog(new BmsLog(data));
            } else if (data.isValue()) {  // if it's a value update data and chart frames
                logBmsValue(new BmsValue(data));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Appends to values log file new value
     *
     * @param data new data to append
     * @throws IOException when it cannot append to file
     */
    private void logBmsValue(BmsValue data) throws IOException {
        String out = "\"" + Long.toString(System.currentTimeMillis()) + "\"" + ","  // time
                + "\"" + data.getType().toString() + "\"" + ","  // type of value
                + "\"" + Integer.toString(data.getSegment()) + "\"" + ","  // segment
                + "\"" + Integer.toString(data.getCell()) + "\"" + ","  // cell
                + "\"" + Double.toString(data.getValue()) + "\"" + "\n";  // value

        if (data.isVoltage()) {
            Files.write(Paths.get(logFiles[1].toString()), out.getBytes(), StandardOpenOption.APPEND);
        } else if (data.isTemperature()) {
            Files.write(Paths.get(logFiles[2].toString()), out.getBytes(), StandardOpenOption.APPEND);
        }
    }

    /**
     * Appends to values log file new log data
     *
     * @param data new data to append
     * @throws IOException when it cannot append to file
     */
    private void logBmsLog(BmsLog data) throws IOException {
        String out = "\"" + Long.toString(System.currentTimeMillis()) + "\"" + ","  // time
                + "\"" + data.getType().toString() + "\"" + ","  // type of value
                + "\"" + Integer.toString(data.getSegment()) + "\"" + "," // segment
                + "\"" + Integer.toString(data.getCell()) + "\"" + ","  // cell
                + "\"" + data.getValue() + "\"" + "\n";  // value

        Files.write(Paths.get(logFiles[0].toString()), out.getBytes(), StandardOpenOption.APPEND);
    }

    /*
     * Files
     */

    private void createLogFilesOrFail() {
        try {
            for (File logFile : logFiles) {
                logFile.getParentFile().mkdirs();  // create file
                logFile.createNewFile();  // empty file
                Streams.emptyFileOrFail(logFile);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}