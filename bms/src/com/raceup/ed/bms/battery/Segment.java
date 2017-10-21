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
 * Battery segment containing battery cells
 */
class Segment {
    private final Cell[] cells;  // list of cell in segment

    /**
     * Create segment with selected number of cells
     *
     * @param numberOfCells number of cells in segment
     */
    Segment(int numberOfCells) {
        this.cells = new Cell[numberOfCells];  // create list of cells
        for (int i = 0; i < numberOfCells; i++) {
            cells[i] = new Cell();
        }
    }

    /**
     * Find number of cells in segment
     *
     * @return number of cells in segment
     */
    public int getNumberOfCells() {
        return cells.length;
    }

    /**
     * Retrieve temperature value of given cell
     *
     * @param cellPosition cell position in segment (numbers show from 0)
     * @return temperature of cell
     */
    public double getTemperatureOfCellOrFail(int cellPosition) {
        return cells[cellPosition].getTemperature();
    }

    /**
     * Retrieve average temperature value of segment
     *
     * @return average temperature of segment
     */
    public double getAverageTemperature() {
        double sum = 0.0;
        for (Cell cell : cells) {
            sum += cell.getTemperature();
        }
        return sum / cells.length;
    }

    /**
     * Retrieve temperature of all cells in segment
     *
     * @return list of temperature of all cells
     */
    public double[] getTemperatures() {
        int numberOfCells = cells.length;
        double[] temperatures = new double[numberOfCells];
        for (int i = 0; i < numberOfCells; i++) {  // loop through cells
            temperatures[i] = getTemperatureOfCellOrFail(i);
        }
        return temperatures;
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

    /**
     * Retrieve voltage value of given cell
     *
     * @param cellPosition cell position in segment (numbers show from 0)
     * @return voltage of cell
     */
    public double getVoltageOfCell(int cellPosition) {
        return cells[cellPosition].getVoltage();
    }

    /**
     * Retrieve average voltage value of segment
     *
     * @return average voltage of segment
     */
    public double getAverageVoltage() {
        double sum = 0.0;
        for (Cell cell : cells) {
            sum += cell.getVoltage();
        }
        return sum / cells.length;
    }

    /**
     * Sum all cells voltage in pack
     *
     * @return all cells voltage in pack
     */
    public double getSumOfAllVoltages() {
        double sum = 0.0;
        for (Cell cell : cells) {
            sum += cell.getVoltage();
        }
        return sum;
    }

    /**
     * Retrieve voltage of all cells in segment
     *
     * @return list of voltage of all cells
     */
    public double[] getVoltages() {
        int numberOfCells = cells.length;
        double[] voltages = new double[numberOfCells];
        for (int i = 0; i < numberOfCells; i++) {  // loop through cells
            voltages[i] = getVoltageOfCell(i);
        }
        return voltages;
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param cellPosition cell position in segment (numbers show from 0)
     * @param voltage      new voltage reading
     */
    public void setVoltageOfCell(int cellPosition, double voltage) {
        cells[cellPosition].setVoltage(voltage);
    }
}
