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


package com.raceup.ed.bms.models.stream.serial;

import com.raceup.ed.bms.logging.Debugger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.NoSuchElementException;


/**
 * Manage Arduino byte stream
 */
public class ArduinoSerial extends Debugger implements SerialPortEventListener {
    private static final String TAG = "ArduinoSerial";
    // between 2 updates
    private static final int WAIT_TIME = 100;
    protected final int BAUD_RATE;  // reading baud rate
    private volatile String rawBuffer = "";
    private SerialPort serialPort;  // serial port reading raw data from
    // arduino

    /**
     * Create new arduino binding with default baud rate
     *
     * @param BAUD_RATE symbol read rate
     */
    public ArduinoSerial(int BAUD_RATE) {
        super("ARDUINO SERIAL", true);
        this.BAUD_RATE = BAUD_RATE;  // baud rate to read data

        try {
            setup();
        } catch (Exception e) {
            logException(e);
        }
    }

    public void close() {
        System.out.println("close port");
        try {
            serialPort.closePort();
        } catch (Throwable t) {
            System.err.println(t.toString());
        }
    }

    /**
     * Finds arduino port, attach to it
     */
    public void setup() {
        String port = findPort();
        configureSerialPortOrFail(port);
    }

    /**
     * Search for arduino port
     *
     * @return port to read arduino from
     */
    private String findPort() throws NoSuchElementException {
        try {
            PortFinder finder = new PortFinder();
            String[] availablePorts = finder.getAvailablePorts();
            return availablePorts[0];
        } catch (Throwable t) {
            throw new NoSuchElementException(t.toString());
        }
    }

    /**
     * Open serial port, use class name for the appName, and set parameters
     *
     * @param port is name of port to configure
     */
    private void configureSerialPortOrFail(String port) {
        try {
            System.out.println("[" + TAG + "]: connecting to " + port);
            serialPort = new SerialPort(port);
            System.out.println("[" + TAG + "]: port opened " +
                    serialPort.openPort());
            System.out.println("[" + TAG + "]: params set " + serialPort
                    .setParams(BAUD_RATE, 8, 1, 0));
            serialPort.addEventListener(this);  // add event listeners
        } catch (Exception e) {
            System.err.println(TAG + " has encountered some errors in " +
                    "configuring serial port");
            System.err.println(e.toString());
        }
    }

    public String[] getRawData() {
        String[] lines = rawBuffer.split("\n");
        rawBuffer = "";
        return lines;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                String receivedData = serialPort.readString(event.getEventValue());
                if (receivedData != null) {
                    rawBuffer += receivedData;
                }
            } catch (SerialPortException ex) {
            }
        }
    }

    /**
     * Sends data via serial to Arduino
     *
     * @param data data to send
     */
    public void sendSerialData(String data) throws SerialPortException {
        serialPort.writeBytes(data.getBytes());
    }

    public void sendSerialData(String data, int maxTimes) {
        int attemptNumber = 0;
        while (attemptNumber < maxTimes) {
            try {
                sendSerialData(data);
                Thread.sleep(WAIT_TIME);
            } catch (Exception e) {
            }
            attemptNumber += 1;
        }

        if (attemptNumber == maxTimes) {
            logError("Max # of attempts reached! Giving up sending" +
                    " Arduino command");
        } else {
            logAction("Attempt # " + attemptNumber + " successful! " +
                    "Sent Arduino command!");
        }
    }

    @Override
    public void finalize() {
        try {
            super.finalize();
            close();
        } catch (Throwable t) {
            System.err.println(t.toString());
        }
    }
}
