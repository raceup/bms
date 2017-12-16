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


package com.raceup.ed.bms.gui.frame.data;

import com.raceup.ed.bms.stream.bms.data.BmsValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Frame containing raw data from BMS
 */
public class DataFrame extends JPanel {
    private BmsDevice[] bmsDevices;  // battery segments

    public DataFrame(int[] numberOfCellsPerSegment) {
        super();
        setup(numberOfCellsPerSegment);
    }

    /**
     * Update value of cell
     *
     * @param data new data (coming from arduino)
     */
    public void updateCellValue(BmsValue data) {
        if (data.isTemperature()) {
            bmsDevices[data.getSegment()].setTemperature1(data.getValue());
            bmsDevices[data.getSegment()].setTemperature2(data.getValue());
        } else if (data.isVoltage()) {
            bmsDevices[data.getSegment()].setVoltage(data.getCell(),
                    data.getValue());
        }
    }

    /**
     * Setup gui and widgets
     *
     * @param numberOfCellsPerSegment list of number of bmsDevices per segment
     */
    private void setup(int[] numberOfCellsPerSegment) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));  // add
        // components vertically
        bmsDevices = new BmsDevice[numberOfCellsPerSegment.length];
        for (int i = 0; i < numberOfCellsPerSegment.length; i++) {  // open
            // all segments
            bmsDevices[i] = new BmsDevice(i, numberOfCellsPerSegment[i]);
            add(bmsDevices[i], BorderLayout.AFTER_LAST_LINE);
            add(Box.createRigidArea(new Dimension(0, 10)));  // add spacing
        }

        setClickListeners();  // on mouse click -> open panel dialog
    }

    /**
     * Set listeners on mouse clicks
     */
    private void setClickListeners() {
        for (int s = 0; s < bmsDevices.length; s++) {
            for (int c = 0; c < bmsDevices[s].cells.length; c++) {
                final int cellNumber = c;
                final int segmentNumber = s;
                bmsDevices[s].cells[c].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            // left button
                            bmsDevices[segmentNumber].cells[cellNumber]
                                    .showDialog(
                                            "Cell " + Integer.toString
                                                    (cellNumber +
                                                            1) + " of bms " +
                                                    Integer
                                                    .toString(segmentNumber
                                                            + 1)
                            );
                        } else if (SwingUtilities.isRightMouseButton
                                (mouseEvent)) {  // right button
                            bmsDevices[segmentNumber].showDialog(
                                    "segment " + Integer.toString
                                            (segmentNumber + 1)
                            );
                        }
                    }
                });  // set onclick mouse event
            }
        }
    }
}
