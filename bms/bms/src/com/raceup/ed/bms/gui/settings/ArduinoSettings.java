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

import javax.swing.*;
import java.awt.*;

/**
 * Panel to set arduino options
 */
public class ArduinoSettings extends JPanel {
    public static final String[] SERIAL_BAUD_RATE = new String[]{"300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "28800", "31250", "38400", "57600", "115200"};
    private static int BAUD_RATE = 115200;  // setup bms

    ArduinoSettings() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));  // add items vertically

        add(createSerialPanel(), BorderLayout.NORTH);
    }

    /**
     * Get selected baud rate
     *
     * @return baud rate
     */
    public int getBaudRate() {
        return BAUD_RATE;
    }

    /**
     * Create a combo box with available data rate (baud rate)
     *
     * @return panel with serial date settings
     */
    private JPanel createSerialPanel() {
        JPanel panel = new JPanel();
        JComboBox<String> dataRateComboBox = new JComboBox<>(SERIAL_BAUD_RATE);
        dataRateComboBox.setSelectedIndex(SERIAL_BAUD_RATE.length - 1);  // set fastest speed available
        dataRateComboBox.addActionListener(e -> {
            String dataRateChosen = (String) dataRateComboBox.getSelectedItem();
            BAUD_RATE = Integer.parseInt(dataRateChosen);
        });  // update baud rate on chosen item

        panel.add(new JLabel(("Baud Rate")));
        panel.add(dataRateComboBox);

        return panel;
    }
}
