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
 * Battery pack containing segments of battery cells
 */
public class Pack {
    private final Segment[] segments;  // list of segments in battery pack

    public Pack(int[] numberOfCellsPerSegment) {
        final int numberOfSegments = numberOfCellsPerSegment.length;
        this.segments = new Segment[numberOfSegments];  // create list of segments
        for (int i = 0; i < numberOfSegments; i++) {  // open segments
            segments[i] = new Segment(numberOfCellsPerSegment[i]);
        }
    }

    /**
     * Number of segments in battery pack
     *
     * @return length of list of segments
     */
    private int getNumberOfSegments() {
        return segments.length;
    }

    /**
     * Number of cells for each segment in battery pack
     *
     * @return list of number of cells
     */
    public int[] getNumberOfCellsPerSegment() {
        int[] numberOfCellsPerSegment = new int[getNumberOfSegments()];  // result list
        for (int i = 0; i < numberOfCellsPerSegment.length; i++) {   // loop through each segment
            numberOfCellsPerSegment[i] = segments[i].getNumberOfCells();  // count cells in each segment
        }
        return numberOfCellsPerSegment;
    }

    /**
     * Retrieve temperature value of given cell
     *
     * @param segment      segment number
     * @param cellPosition cell position in segment (numbers open from 0)
     * @return temperature of cell
     */
    public double getTemperatureOfCell(int segment, int cellPosition) {
        return segments[segment].getTemperatureOfCellOrFail(cellPosition);
    }

    /**
     * Retrieve average temperature value of segment
     *
     * @param segment segment number
     * @return average temperature of segment
     */
    public double getAverageTemperatureOfSegment(int segment) {
        return segments[segment].getAverageTemperature();
    }

    /**
     * Retrieve average temperature value of pack
     *
     * @return average temperature
     */
    public double getAverageTemperature() {
        double sum = 0.0;
        for (Segment segment : segments) {
            sum += segment.getAverageTemperature();
        }
        return sum / segments.length;
    }

    /**
     * Retrieve temperature of all cells in segment
     *
     * @param segment segment number
     * @return list of temperature of all cells
     */
    public double[] getTemperatureInSegment(int segment) {
        return segments[segment].getTemperatures();
    }

    /**
     * Update temperature of cell of given cell
     *
     * @param segment      segment number
     * @param cellPosition cell position in segment (numbers open from 0)
     * @param temperature  new temperature reading
     */
    public void setTemperatureOfCell(int segment, int cellPosition, double temperature) {
        segments[segment].setTemperatureOfCell(cellPosition, temperature);
    }

    /**
     * Retrieve voltage value of given cell
     *
     * @param segment      segment number
     * @param cellPosition cell position in segment (numbers open from 0)
     * @return voltage of cell
     */
    public double getVoltageOfCell(int segment, int cellPosition) {
        return segments[segment].getVoltageOfCell(cellPosition);
    }

    /**
     * Retrieve average voltage value of segment
     *
     * @param segment segment number
     * @return average voltage of segment
     */
    public double getAverageVoltageOfSegment(int segment) {
        return segments[segment].getAverageVoltage();
    }

    /**
     * Sum all cell voltage in all segments and return sum
     *
     * @return sum of all cell voltage in all segments
     */
    public double getSumOfAllVoltages() {
        double sum = 0.0;
        for (Segment segment : segments) {
            sum += segment.getSumOfAllVoltages();
        }
        return sum;
    }

    /**
     * Retrieve average voltage value of pack
     *
     * @return average voltage
     */
    public double getAverageVoltage() {
        double sum = 0.0;
        for (Segment segment : segments) {
            sum += segment.getAverageVoltage();
        }
        return sum / segments.length;
    }

    /**
     * Retrieve voltage of all cells in segment
     *
     * @param segment segment number
     * @return list of voltage of all cells
     */
    public double[] getVoltagesInSegment(int segment) {
        return segments[segment].getVoltages();
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param segment      segment number
     * @param cellPosition cell position in segment (numbers open from 0)
     * @param voltage      new voltage reading
     */
    public void setVoltageOfCell(int segment, int cellPosition, double voltage) {
        segments[segment].setVoltageOfCell(cellPosition, voltage);
    }
}
