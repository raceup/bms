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
        double sum = 0.0;
        for (BmsDevice device : bmsDevices) {
            sum += device.getTemperature();
        }
        return sum / bmsDevices.length;
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

    /*
     * Voltages
     */

    /**
     * Retrieve average voltage value of segment
     *
     * @return average voltage of segment
     */
    public double getVoltage() {
        double sum = 0.0;
        for (BmsDevice device : bmsDevices) {
            sum += device.getVoltage();
        }
        return sum;
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param bmsDevice cell position in segment (numbers show from 0)
     * @param voltage      new voltage reading
     */
    void setVoltageOfBms(int bmsDevice, int cell, double voltage) {
        bmsDevices[bmsDevice].setVoltage(cell, voltage);
    }

}
