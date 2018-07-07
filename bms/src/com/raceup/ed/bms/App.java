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
import com.raceup.ed.bms.ui.Gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * App driver program
 * Run BmsGUI or simple BmsUtils monitor here
 */
class App {
    private Pack battery;
    private ArduinoSerial arduino;
    private Bms bms;
    private Gui ui;

    public App() {
        setup();
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    private void setup() {
        try {
            battery = new Pack(8, 3);
        } catch (Exception e) {
            System.err.println("Error while creating battery model");
        }

        try {
            arduino = new ArduinoSerial(115200);
        } catch (Exception e) {
            System.err.println("Error while opening Arduino serial");
        }

        try {
            bms = new Bms(arduino, battery);
        } catch (Exception e) {
            System.err.println("Error while creating control");
        }

        try {
            ui = new Gui(bms);
            ui.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    ui.close();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            System.err.println("Error while creating UI");
        }
    }

    // todo measure lag between when serial receives data and when updates
    // model and screen
    public void start() {
        try {
            ui.open();  // start frontend
            Thread thread = new Thread(ui);  // start thread
            thread.start();
        } catch (Exception e) {
            System.err.println("Error while opening UI");
        }
    }
}
