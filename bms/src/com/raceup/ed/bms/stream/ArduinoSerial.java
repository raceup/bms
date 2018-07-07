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


package com.raceup.ed.bms.stream;

import com.raceup.ed.bms.stream.serial.PortFinder;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.NoSuchElementException;


/**
 * Manage Arduino byte stream
 */
public class ArduinoSerial implements SerialPortEventListener {
    private static final String TAG = "ArduinoSerial";
    // between 2 updates
    protected final int BAUD_RATE;  // reading baud rate
    private String serialData;  // last data from serial
    private SerialPort serialPort;  // serial port reading raw data from
    // arduino

    /**
     * Create new arduino binding with default baud rate
     *
     * @param BAUD_RATE symbol read rate
     */
    public ArduinoSerial(int BAUD_RATE) {
        this.BAUD_RATE = BAUD_RATE;  // baud rate to read data
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

    public String getRawData() {
        return serialData;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                String receivedData = serialPort.readString(event.getEventValue());
                System.out.println("Received response: " + receivedData);
                serialData = receivedData;
            } catch (SerialPortException ex) {
            }
        }
    }

    /**
     * Sends data via serial to Arduino
     *
     * @param data data to send
     */
    protected void sendSerialDataOrFail(String data) {
        try {
            serialPort.writeBytes(data.getBytes());
        } catch (Exception e) {
            System.err.println("ArduinoSerial cannot send data " + data + " " +
                    "because:\n" + e.toString());
        }
    }

    @Override
    public void finalize() {
        System.out.println("finalizing....");
        try {
            super.finalize();
            close();
        } catch (Throwable t) {
            System.err.println(t.toString());
        }
    }
}
