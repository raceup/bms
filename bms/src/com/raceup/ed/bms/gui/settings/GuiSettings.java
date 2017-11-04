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

package com.raceup.ed.bms.gui.settings;

import com.raceup.ed.bms.Bms;
import com.raceup.ed.bms.battery.Pack;
import com.raceup.ed.bms.stream.Logger;
import com.raceup.ed.bms.utils.gui.JPanelsUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Show dialog to create a new instance of a BmsGui
 * Let users set some settings that cannot be set after creating BmsGui
 */
public class GuiSettings {
    private static final String SETTINGS_TITLE = "Create new Bms monitor";
    private static final String ARDUINO_SETTINGS_TITLE = "Arduino serial " +
            "data rate";
    private static final String LOGGER_SETTINGS_TITLE = "Folder to store " +
            "logs";
    private static final String BATTERY_SETTINGS_TITLE = "Battery pack model";

    /**
     * Create new bms based on settings
     *
     * @return custom bms
     */
    public static Bms buildBms() {
        JPanel canvas = new JPanel();
        canvas.setLayout(new BoxLayout(canvas, BoxLayout.PAGE_AXIS));
        // add items vertically

        ArduinoSettings arduinoSettings = new ArduinoSettings();
        LoggerSettings loggerSettings = new LoggerSettings();
        BatterySettings batterySettings = new BatterySettings();

        canvas.add(createSettingsPanel(arduinoSettings,
                ARDUINO_SETTINGS_TITLE));
        canvas.add(createSettingsPanel(loggerSettings, LOGGER_SETTINGS_TITLE));
        canvas.add(createSettingsPanel(new JScrollPane(batterySettings),
                BATTERY_SETTINGS_TITLE));

        return createBms(canvas, arduinoSettings, loggerSettings,
                batterySettings);
    }

    /**
     * Creates new bms model with settings from GUI panels
     *
     * @param mainPanel       canvas to show dialog
     * @param arduinoSettings panel for arduino related settings
     * @param loggerSettings  panel for logging related settings
     * @param batterySettings panel for battery related settings
     * @return bms model with settings from GUI panels
     */
    private static Bms createBms(JPanel mainPanel, ArduinoSettings
            arduinoSettings, LoggerSettings loggerSettings, BatterySettings
                                         batterySettings) {
        int userInput = JOptionPane.showConfirmDialog(
                null,
                mainPanel, SETTINGS_TITLE, // question for the user
                JOptionPane.OK_CANCEL_OPTION
        );

        boolean userQuit = userInput == JOptionPane.CANCEL_OPTION ||
                userInput < 0;

        if (userQuit) {
            return null;
        }

        return new Bms(
                arduinoSettings.getBaudRate(),  // baud rate
                new Logger(loggerSettings.getPathChosen()),  // log folder
                new Pack(
                        batterySettings.getNumberOfBmsPerSegment(),
                        3
                )
        );
    }

    /**
     * Create panel with settings and title
     *
     * @param settingsPanel settings panel to attach
     * @param title         title of panel
     * @return panel with settings
     */
    private static JPanel createSettingsPanel(Component settingsPanel,
                                              String title) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        mainPanel.add(settingsPanel);  // add settings panel
        JPanelsUtils.addTitleBorderOnPanel(mainPanel, title);  // add title


        return mainPanel;
    }
}
