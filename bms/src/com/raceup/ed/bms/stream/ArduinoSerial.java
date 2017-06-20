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

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;


/**
 * Manage arduino byte stream
 */
public class ArduinoSerial implements SerialPortEventListener {
    private static final int TIME_OUT = 2000;  // milliseconds to block while waiting for port open
    private static final String PORT_NAMES[] = {  // The port we're normally going to use.
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyUSB0", // Linux
            "COM3" // Windows
    };
    public int msSerialIntervalUpdate = 10;  // interval (in milliseconds) between 2 updates
    protected int BAUD_RATE;  // reading baud rate
    protected String serialData;  // last data from serial
    private long lastUpdateMs;  // ms of last update series
    private SerialPort serialPort;  // serial port reading raw data from arduino
    private CommPortIdentifier portId;  // id of serial port
    private BufferedReader serialInput;  // a BufferedReader which will be fed by a InputStreamReader converting the bytes into characters
    private OutputStream serialOutput;  // stream to write to serial port
    private OutputStream output;  // the output stream from the port

    /**
     * Create new arduino binding with default baud rate
     *
     * @param BAUD_RATE symbol read rate
     */
    protected ArduinoSerial(int BAUD_RATE) {
        this.BAUD_RATE = BAUD_RATE;  // baud rate to read data
        // TODO: test only initializeOrFail();
    }

    /**
     * Create new arduino binding with custom baud rate and redirection of output
     *
     * @param BAUD_RATE read rate of arduino serial
     * @param output    std output where to send serial data
     */
    protected ArduinoSerial(int BAUD_RATE, OutputStream output) {
        this(BAUD_RATE);  // set baud rate and initializeOrFail
        this.output = output;  // set output stream to get data from port
    }

    /**
     * Set new baud rate
     *
     * @param baudRate baud rate to read serial data
     */
    public void setBaudRate(int baudRate) {
        this.BAUD_RATE = baudRate;
        configureSerialPortOrFail(portId);  // re-set baud rate
    }

    /**
     * Write data to serial port
     *
     * @param data data to write
     * @throws IOException if cannot write to serial
     */
    protected synchronized void writeToSerial(String data) throws IOException {
        serialOutput.write(data.getBytes());
    }

    /**
     * This should be called when you stop using the port. This will prevent port locking on platforms like Linux.
     *
     * @throws IOException when streams cannot be found
     */
    protected synchronized void close() throws IOException {
        if (serialPort != null) {
            serialPort.removeEventListener();  // stop port
            serialPort.close();

            serialInput.close();  // stop streams
            output.close();
        }
    }

    /**
     * Finds arduino port, attach to it
     */
    private void initializeOrFail() {
        portId = findPort();
        configureSerialPortOrFail(portId);
    }

    /**
     * Search for arduino port
     *
     * @return port to read arduino from
     */
    private CommPortIdentifier findPort() throws NoSuchElementException {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }

        if (portId == null) {
            throw new NoSuchElementException("No Arduino board found.");
        } else {
            return portId;
        }
    }

    /**
     * Open serial port, use class name for the appName, and set parameters
     *
     * @param portId is of port to configure
     */
    private void configureSerialPortOrFail(CommPortIdentifier portId) {
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
            serialPort.setSerialPortParams(
                    BAUD_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
            );  // set port parameters

            serialInput = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));  // open the streams
            output = serialPort.getOutputStream();
            serialPort.addEventListener(this);  // add event listeners
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Read last update from serial and stores it
     *
     * @throws IOException if cannot read to serial
     */
    private synchronized void readFromSerial() throws IOException {
        long timeNowMs = System.currentTimeMillis();  // get ms when updated with new data
        if (timeNowMs - lastUpdateMs >= msSerialIntervalUpdate) {  // update series interval
            String newData = serialInput.readLine();  // get new data
            if (newData != null) {
                lastUpdateMs = timeNowMs;  // update last time of update
                serialData = newData;  // store new value
            }

            if (output != null) {
                output.write((timeNowMs + " - " + newData + "\n").getBytes());  // write new data to output
            }
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     *
     * @param oEvent any event that occurs in the serial
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                readFromSerial();
            } catch (Exception e) {
                System.err.println(e.toString());
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
            writeToSerial(data);
        } catch (Exception e) {
            System.err.println("ArduinoSerial cannot send data " + data + " because:\n" + e.toString());
        }
    }
}
