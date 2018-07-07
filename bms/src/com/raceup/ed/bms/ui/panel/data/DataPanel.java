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


package com.raceup.ed.bms.ui.panel.data;

import com.raceup.ed.bms.models.stream.bms.BmsValue;

import javax.swing.*;
import java.awt.*;

/**
 * Frame containing raw data from BMS
 */
public class DataPanel extends JPanel {
    private Bms[] bmsDevices;

    public DataPanel(int numberOfSegments, int numberOfBmsPerSegment) {
        super();
        bmsDevices = new Bms[numberOfSegments * numberOfBmsPerSegment];
        setup(numberOfSegments, numberOfBmsPerSegment);
    }

    /**
     * Update value of cell
     *
     * @param data new data (coming from arduino)
     */
    public void update(BmsValue data) {
        int bms = data.getBms() - 1;
        double value = data.getValue();

        if (data.isTemperature()) {
            if (data.isTemperature1()) {
                bmsDevices[bms].setTemperature1(value);
            } else if (data.isTemperature2()) {
                bmsDevices[bms].setTemperature2(value);
            }
        } else if (data.isVoltage()) {
            bmsDevices[bms].setMinVoltage(value);
        }
    }

    /**
     * Setup ui and widgets
     *
     * @param numberOfBmsPerSegment list of number of bmsDevices per segment
     */
    private void setup(int numberOfSegments, int numberOfBmsPerSegment) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  // vertical
        for (int row = 0; row < numberOfSegments; row++) {
            JPanel segment = new JPanel();
            segment.setLayout(new BoxLayout(segment, BoxLayout.X_AXIS));

            for (int column = 0; column < numberOfBmsPerSegment; column++) {
                int bmsNumber = row * numberOfBmsPerSegment + column;
                bmsDevices[bmsNumber] = new Bms(bmsNumber);
                segment.add(bmsDevices[bmsNumber]);
                segment.add(Box.createRigidArea(new Dimension(50, 0)));
            }

            add(segment, BorderLayout.AFTER_LAST_LINE);
            add(Box.createRigidArea(new Dimension(0, 20)));  // add spacing
        }
    }
}
