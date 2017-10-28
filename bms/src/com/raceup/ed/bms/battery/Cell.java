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

package com.raceup.ed.bms.battery;

/**
 * Battery cell with temperature and voltage readings
 */
class Cell {
    private double temperature;
    private double voltage;

    Cell() {
        temperature = 0.0;
        voltage = 0.0;
    }

    /**
     * Retrieve temperature value
     *
     * @return temperature of cell
     */
    public synchronized double getTemperature() {
        return temperature;
    }

    /**
     * Update temperature of cell
     *
     * @param temperature new temperature reading
     */
    public synchronized void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Retrieves voltage value
     *
     * @return voltage of cell
     */
    public synchronized double getVoltage() {
        return voltage;
    }

    /**
     * Updates voltage of cell
     *
     * @param voltage new voltage reading
     */
    public synchronized void setVoltage(double voltage) {
        this.voltage = voltage;
    }
}
