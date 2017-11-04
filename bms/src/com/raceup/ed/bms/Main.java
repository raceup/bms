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

import com.raceup.ed.bms.gui.settings.GuiSettings;

import javax.swing.*;
import java.awt.*;

/**
 * Main driver program
 * Run BmsGUI or simple Bms monitor here
 */
class Main {
    private static final String APP_NAME_SETTINGS = "awtAppClassName";
    private static final String ERROR_MESSAGE_TITLE =
            "Failure when starting app";
    private static final String ERROR_MESSAGE = "Sorry the app encountered " +
            "an error when starting. More details in the following lines.\n\n";
    private static final String RXTX_ERROR_MESSAGE = "It seems that we " +
            "cannot load the Arduino " +
            "serial library RXTX. It is mandatory to install it " +
            "in order to run the app. You can find more information on how " +
            "to install in the following web page: http://rxtx" +
            ".qbang.org/wiki/index.php/Download";

    /**
     * Open a gui and start bms manager
     *
     * @param args default args
     */
    public static void main(String[] args) {
        setAppUiOrFail();
        Bms bms = GuiSettings.buildBms();
        startGui(bms);  // create app

    }

    /**
     * Set application name or do nothing
     */
    private static void setAppUiOrFail() {
        try {
            Toolkit xToolkit = Toolkit.getDefaultToolkit();
            java.lang.reflect.Field awtAppClassNameField =
                    xToolkit.getClass().getDeclaredField(APP_NAME_SETTINGS);
            awtAppClassNameField.setAccessible(true);
            awtAppClassNameField.set(xToolkit, Bms.appName + " " + Bms
                    .appVersion);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Build a GUI or return a null reference
     * @param bms BMS model to open
     * @return bms gui manager already setup
     */
    private static void startGui(Bms bms) {
        try {
            if (bms != null) {
                BmsGui bmsGui = new BmsGui(bms);
                startAppOrExit(bmsGui);  // start app
            }
        } catch (Throwable e) {
            System.out.print(e.toString());
            String errorMessage = ERROR_MESSAGE + e.toString() + "\n\n";

            if (e.toString().toLowerCase().contains("rxtx")) {  // cannot
                // load arduino serial library
                errorMessage += RXTX_ERROR_MESSAGE;
            }

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
            String errorMessage = "Sorry the app encountered an error when " +
                    "opening. More details in the following lines.\n\n";
            errorMessage += e.toString() + "\n\n";
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
