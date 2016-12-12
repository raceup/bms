/*
 * Copyright 2016-2017 RaceUp Team ED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    /**
     * Create new bms based on settings
     *
     * @return custom bms
     */
    public static Bms buildBms() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));  // add items vertically

        ArduinoSettings arduinoSettings = new ArduinoSettings();  // create panels
        BatterySettings batterySettings = new BatterySettings();
        LoggerSettings loggerSettings = new LoggerSettings();

        mainPanel.add(createSettingsPanel(arduinoSettings, "Arduino serial data rate"));  // setup layout
        mainPanel.add(createSettingsPanel(loggerSettings, "Folder to store logs"));
        mainPanel.add(createSettingsPanel(new JScrollPane(batterySettings), "Battery pack model"));

        int userInput = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Settings to create new Bms monitor",  // question for the user
                JOptionPane.CANCEL_OPTION
        );

        if (userInput == JOptionPane.CANCEL_OPTION || userInput < 0) {  // user has clicked "Cancel" button or exited panel -> exit
            System.exit(0);
            return null;
        } else {  // user has decided options and not cancelled operations
            return new Bms(
                    arduinoSettings.getBaudRate(),  // baud rate
                    new Logger(loggerSettings.getPathChosen()),  // log folder
                    new Pack(batterySettings.getNumberOfCellsPerSegment())  // new battery pack
            );
        }
    }

    /**
     * Create panel with settings and title
     *
     * @param settingsPanel settings panel to attach
     * @param title         title of panel
     * @return panel with settings
     */
    private static JPanel createSettingsPanel(Component settingsPanel, String title) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // add spacing
        mainPanel.add(settingsPanel);  // add settings panel
        JPanelsUtils.addTitleBorderOnPanel(mainPanel, title);  // add title and border

        return mainPanel;
    }
}
