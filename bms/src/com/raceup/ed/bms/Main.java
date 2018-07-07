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

import com.raceup.ed.bms.control.Bms;
import com.raceup.ed.bms.models.battery.Pack;
import com.raceup.ed.bms.models.stream.serial.ArduinoSerial;

import javax.swing.*;

/**
 * Main driver program
 * Run BmsGUI or simple BmsUtils monitor here
 */
class Main {
    private static final String APP_NAME_SETTINGS = "awtAppClassName";
    private static final String ERROR_MESSAGE_TITLE =
            "Failure when starting app";
    private static final String ERROR_MESSAGE = "Sorry the app encountered " +
            "an error when starting.";

    /**
     * Open a ui and start bms manager
     *
     * @param args default args
     */
    public static void main(String[] args) {
        Pack battery = new Pack(8, 3);
        ArduinoSerial arduino = new ArduinoSerial(115200);
        Bms bms = new Bms(arduino, battery);
        startGui(bms);
    }

    /**
     * Build a GUI or return a null reference
     * @param bms BMS model to open
     */
    private static void startGui(Bms bms) {
        try {
            if (bms != null) {
                BmsGui bmsGui = new BmsGui();
                startAppOrExit(bmsGui);  // start app
            }
        } catch (Throwable e) {
            System.out.print(e.toString());
            String errorMessage = ERROR_MESSAGE + e.toString();

            JOptionPane.showMessageDialog(  // alert dialog with exception
                    null,
                    errorMessage,
                    ERROR_MESSAGE_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Try to start the app; if there are some errors it exits the program
     *
     * @param bmsGui app tp start
     */
    private static void startAppOrExit(BmsGui bmsGui) {
        if (bmsGui == null) {
            System.exit(1);
        }

        try {
            bmsGui.open();  // open in screen
        } catch (Throwable e) {
            String errorMessage = ERROR_MESSAGE + "\n\n ";
            errorMessage += e.toString();
            JOptionPane.showMessageDialog(  // alert dialog with exception
                    null,
                    errorMessage,
                    ERROR_MESSAGE_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );

            System.exit(1);
        }
    }
}
