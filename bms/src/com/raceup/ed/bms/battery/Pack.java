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
public class Pack implements BmsControllable {
    private Segment[] segments;  // list of segments in battery pack

    public Pack(int[] numberOfCellsPerSegment, int numberOfCellsPerBms) {
        segments = createSegments(numberOfCellsPerSegment);
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
        int[] numberOfCellsPerSegment = new int[getNumberOfSegments()];  //
        // result list
        for (int i = 0; i < numberOfCellsPerSegment.length; i++) {   // loop
            // through each segment
            numberOfCellsPerSegment[i] = segments[i].getNumberOfCells();  //
            // count cells in each segment
        }
        return numberOfCellsPerSegment;
    }

    /**
     * Retrieve average temperature value of pack
     *
     * @return average temperature
     */
    public double getTemperature() {
        double sum = 0.0;
        for (Segment segment : segments) {
            sum += segment.getTemperature();
        }
        return sum;
    }

    /**
     * Update temperature of cell of given cell
     *
     * @param segment      segment number
     * @param cellPosition cell position in segment (numbers open from 0)
     * @param temperature  new temperature reading
     */
    public void setTemperatureOfCell(int segment, int cellPosition, double
            temperature) {
        segments[segment].setTemperatureOfCell(cellPosition, temperature);
    }

    /**
     * Retrieve average voltage value of pack
     *
     * @return average voltage
     */
    public double getVoltage() {
        double sum = 0.0;
        for (Segment segment : segments) {
            sum += segment.getSumOfAllVoltages();
        }
        return sum;
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param segment      segment number
     * @param cellPosition cell position in segment (numbers open from 0)
     * @param voltage      new voltage reading
     */
    public void setVoltageOfCell(int segment, int cellPosition, double
            voltage) {
        segments[segment].setVoltageOfCell(cellPosition, voltage);
    }

    /**
     * Creates list of segments for pack
     *
     * @param numberOfCellsPerSegment # cell in each segment
     * @return list of segments
     */
    public static Segment[] createSegments(
            int[] numberOfCellsPerSegment) {
        final int numberOfSegments = numberOfCellsPerSegment.length;
        Segment[] segments = new Segment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {  // open segments
            segments[i] = new Segment(numberOfCellsPerSegment[i]);
        }
        return segments;
    }
}
