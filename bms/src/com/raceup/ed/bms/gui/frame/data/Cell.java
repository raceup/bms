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

import com.raceup.ed.bms.gui.frame.chart.ChartFrame;

import javax.swing.*;
import java.awt.*;

/**
 * GUI frame that contains battery cell info
 */
public class Cell extends NumAlerter {
    public static final double[] VOLTAGE_BOUNDS = new double[]{4000.0,
            4200.0};
    // labels
    protected final JLabel voltageLabel = new JLabel("0000.00");
    protected double voltage;
    private int indexInBms;

    Cell(int indexInBms) {
        this.indexInBms = indexInBms;
        setLayout(new GridLayout(2, 1));  // rows, columns
        setup();
    }

    /*
     * Getters & setters
     */

    /**
     * Retrieve voltage value
     *
     * @return getter method for voltage
     */
    public double getVoltage() {
        return voltage;
    }

    /**
     * Show new value in label and bar
     *
     * @param value new voltage to set
     */
    public void setVoltage(double value) {
        voltage = value;  // update value
        update(voltageLabel, VOLTAGE_BOUNDS, value);
    }

    /**
     * Sets temperature bounds
     *
     * @param min minimum value; below this value background will color
     *            VALUE_TOO_LOW_COLOR
     * @param max maximum value; over this value background will color
     *            VALUE_TOO_HIGH_COLOR
     */
    void setVoltageBounds(double min, double max) {
        VOLTAGE_BOUNDS[0] = min;
        VOLTAGE_BOUNDS[1] = max;

        setVoltage(voltage);  // update colors
    }

    /**
     * Get voltage bounds
     *
     * @return [min, max] array
     */
    double[] getVoltageBounds() {
        return VOLTAGE_BOUNDS;
    }

    /**
     * Show dialog with more info about cell
     *
     * @param title title of dialog
     */
    void showDialog(String title) {
        ChartFrame dialog = new ChartFrame(title, new String[]{"Voltage (mV)" +
                "", "Temperature (K)"});
        dialog.setLocationRelativeTo(null);  // center in screen

        Timer updater = new Timer(10, e -> {
            dialog.updateOrFail(0, getVoltage());
        });  // timer to update dialog values
        updater.start();
    }

    /*
     * Setup and update
     */

    /**
     * Add to panel all widgets
     */
    private void setup() {
        voltageLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(new JLabel(Integer.toString(indexInBms)));
        add(voltageLabel);

        setVisible(true);  // open
    }
}
