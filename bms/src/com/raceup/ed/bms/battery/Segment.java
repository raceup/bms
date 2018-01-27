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

import com.raceup.ed.bms.utils.BmsUtils;

/**
 * Battery segment containing battery cells
 */
public class Segment implements BmsControllable {
    private final Cell[] cells;  // list of cell in segment
    private final BmsDevice[] bmsDevices;
    // bms

    /**
     * Create segment with selected number of cells
     *
     * @param numberOfCells number of cells in segment
     * @param numberOfCellsPerBms number of cells controlled by 1 bms device
     */
    public Segment(int numberOfCells, int numberOfCellsPerBms) {
        cells = new Cell[numberOfCells];  // create list of cells
        for (int i = 0; i < numberOfCells; i++) {
            cells[i] = new Cell();
        }

        bmsDevices = BmsUtils.createBmsDevices(numberOfCells,
                numberOfCellsPerBms);  // todo use
    }

    /*
     * General info
     */

    /**
     * Find number of cells in segment
     *
     * @return number of cells in segment
     */
    public int getNumberOfCells() {
        return cells.length;
    }

    /**
     * Find number of bms devices in segment
     *
     * @return number of bms devices in segment
     */
    public int getNumberOfBmsDevices() {
        return bmsDevices.length;
    }

    /*
     * Temperatures
     */

    /**
     * Retrieve average temperature value of segment
     *
     * @return average temperature of segment
     */
    public double getTemperature() {
        double sum = 0.0;
        for (Cell cell : cells) {
            sum += cell.getTemperature();
        }
        return sum / cells.length;
    }

    /**
     * Update temperature of cell of given cell
     *
     * @param cellPosition cell position in segment (numbers show from 0)
     * @param temperature  new temperature reading
     */
    public void setTemperatureOfCell(int cellPosition, double temperature) {
        cells[cellPosition].setTemperature(temperature);
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
        for (Cell cell : cells) {
            sum += cell.getVoltage();
        }
        return sum;
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param cellPosition cell position in segment (numbers show from 0)
     * @param voltage      new voltage reading
     */
    void setVoltageOfCell(int cellPosition, double voltage) {
        cells[cellPosition].setVoltage(voltage);
    }

}
