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
import java.text.DecimalFormat;

/**
 * GUI frame that contains battery cell info
 */
class Cell extends JPanel {
    private static final DecimalFormat SHORT_DEC_FORMAT = new DecimalFormat("0000.00");  // decimal places
    private static final Color VALUE_TOO_HIGH_COLOR = Color.RED;
    private static final Color VALUE_NORMAL_COLOR = Color.GREEN;
    private static final Color VALUE_TOO_LOW_COLOR = Color.CYAN;
    private static final double[] temperatureBounds = new double[]{-100, 60};  // too low, too high values
    private static final double[] voltageBounds = new double[]{4000.0, 4200.0};
    private final JLabel temperatureLabel = new JLabel("0000.00");  // labels
    private final JLabel voltageLabel = new JLabel("0000.00");
    private double temperature;  // remember last value set
    private double voltage;

    Cell() {
        setLayout(new GridLayout(2, 1));  // rows, columns
        setup();
    }

    /*
     * Getters & setters
     */

    /**
     * Retrieve temperature value
     *
     * @return getter method for temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Show new value in label and bar
     *
     * @param value new temperature to set
     */
    public void setTemperature(double value) {
        temperature = value;  // update value
        update(temperatureLabel, temperatureBounds, value);
    }

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
        update(voltageLabel, voltageBounds, value);
    }

    /**
     * Sets temperature bounds
     *
     * @param min minimum value; below this value background will color VALUE_TOO_LOW_COLOR
     * @param max maximum value; over this value background will color VALUE_TOO_HIGH_COLOR
     */
    void setTemperatureBounds(double min, double max) {
        temperatureBounds[0] = min;
        temperatureBounds[1] = max;

        setTemperature(temperature);  // update colors
    }

    /**
     * Sets temperature bounds
     *
     * @param min minimum value; below this value background will color VALUE_TOO_LOW_COLOR
     * @param max maximum value; over this value background will color VALUE_TOO_HIGH_COLOR
     */
    void setVoltageBounds(double min, double max) {
        voltageBounds[0] = min;
        voltageBounds[1] = max;

        setVoltage(voltage);  // update colors
    }

    /**
     * Get temperature bounds
     *
     * @return [min, max] array
     */
    double[] getTemperatureBounds() {
        return temperatureBounds;
    }

    /**
     * Get voltage bounds
     *
     * @return [min, max] array
     */
    double[] getVoltageBounds() {
        return voltageBounds;
    }

    /*
     * dialog to show more info
     */

    /**
     * Show dialog with more info about cell
     *
     * @param title title of dialog
     */
    void showDialog(String title) {
        ChartFrame dialog = new ChartFrame(title, new String[]{"Voltage (mV)", "Temperature (K)"});
        dialog.setLocationRelativeTo(null);  // center in screen

        Timer updater = new Timer(10, e -> {
            dialog.updateSeriesOrFail(0, getVoltage());
            dialog.updateSeriesOrFail(1, getTemperature());
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
        temperatureLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        voltageLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(temperatureLabel);  // add items
        add(voltageLabel);

        setVisible(true);  // open
    }

    /**
     * Update widget with new value
     *
     * @param label  label to update series
     * @param bounds min, max of value allowed
     * @param value  new value
     */
    private void update(JLabel label, double[] bounds, double value) {
        label.setText(SHORT_DEC_FORMAT.format(value));
        updateBackground(value, bounds);
    }

    /**
     * Changes background on given update series of value
     *
     * @param value  new value to update
     * @param bounds min, max of value
     */
    private void updateBackground(double value, double[] bounds) {
        if (value >= bounds[1]) {  // too high
            setBackground(VALUE_TOO_HIGH_COLOR);
        } else if (value <= bounds[0]) {  // too low
            setBackground(VALUE_TOO_LOW_COLOR);
        } else {  // normal
            setBackground(VALUE_NORMAL_COLOR);
        }
    }
}
