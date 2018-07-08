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

package com.raceup.ed.bms.models.battery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Battery segment containing battery cells
 */
public class Segment implements BmsControllable {
    private final BmsDevice[] bmsDevices;
    // bms

    /**
     * Create segment with selected number of cells
     *
     * @param numberOfBms number of cells in segment
     */
    public Segment(int numberOfBms) {
        bmsDevices = new BmsDevice[numberOfBms];
        for (int i = 0; i < bmsDevices.length; i++) {
            bmsDevices[i] = new BmsDevice();
        }
    }

    public int getNumberOfBms() {
        return bmsDevices.length;
    }


    /**
     * Retrieve average temperature value of segment
     *
     * @return average temperature of segment
     */
    public double getTemperature() {
        double result = 0.0;
        int samples = 0;
        for (BmsDevice device : bmsDevices) {
            double temperature = device.getTemperature();
            if (temperature > 0) {
                result += temperature;
                samples += 1;
            }
        }

        if (result == 0.0) {
            throw new NoSuchElementException("Cannot find avg");
        }

        result /= samples;
        return result;
    }

    /**
     * Update temperature of cell of given cell
     *
     * @param temperature  new temperature reading
     */
    public void setTemperature1OfBms(int bmsDevice, double temperature) {
        bmsDevices[bmsDevice].setTemperature1(temperature);
    }

    /**
     * Update temperature of cell of given cell
     *
     * @param temperature new temperature reading
     */
    public void setTemperature2OfBms(int bmsDevice, double temperature) {
        bmsDevices[bmsDevice].setTemperature2(temperature);
    }

    /**
     * Update temperature of cell of given cell
     */
    public double getTemperature1OfBms(int bmsDevice) {
        return bmsDevices[bmsDevice].getTemperature1();
    }

    /**
     * Update temperature of cell of given cell
     */
    public double getTemperature2OfBms(int bmsDevice) {
        return bmsDevices[bmsDevice].getTemperature2();
    }

    /*
     * Voltages
     */

    /**
     * Retrieve average voltage value of segment
     *
     * @return average voltage of segment
     */
    public double getVoltage() {
        double result = 0.0;
        for (BmsDevice device : bmsDevices) {
            double voltage = device.getVoltage();
            if (voltage > 0) {
                result += voltage;
            }
        }

        if (result == 0.0) {
            throw new NoSuchElementException("Cannot find tot");
        }

        return result;

    }

    /**
     * Update voltage of cell of given cell
     *
     * @param bmsDevice cell position in segment (numbers show from 0)
     * @param voltage      new voltage reading
     */
    public void setVoltageOfBms(int bmsDevice, int cell, double voltage) {
        bmsDevices[bmsDevice].setVoltage(cell, voltage);
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param bmsDevice cell position in segment (numbers show from 0)
     */
    public double getVoltage(int bmsDevice, int cell) {
        return bmsDevices[bmsDevice].getVoltage(cell);
    }

    public double getMinVoltage(int bmsDevice) {
        return bmsDevices[bmsDevice].getMinVoltage();
    }

    public double getAvgVoltage(int bmsDevice) {
        return bmsDevices[bmsDevice].getAvgVoltage();
    }

    public double getMaxVoltage(int bmsDevice) {
        return bmsDevices[bmsDevice].getMaxVoltage();
    }

    public HashMap<String, Double> getCurrentValues(int bmsDevice) {
        try {
            return bmsDevices[bmsDevice].getCurrentValues();
        } catch (Exception e) {
            return null;
        }
    }

    public double getMinVoltage() {
        ArrayList<Double> voltages = new ArrayList<>();
        for (BmsDevice bms : bmsDevices) {
            try {
                voltages.add(bms.getMinVoltage());
            } catch (Exception e) {
            }
        }

        int index = voltages.indexOf(Collections.min(voltages));
        return voltages.get(index);
    }

    public double getMaxVoltage() {
        ArrayList<Double> voltages = new ArrayList<>();
        for (BmsDevice bms : bmsDevices) {
            try {
                voltages.add(bms.getMaxVoltage());
            } catch (Exception e) {
            }
        }

        int index = voltages.indexOf(Collections.max(voltages));
        return voltages.get(index);
    }
}
