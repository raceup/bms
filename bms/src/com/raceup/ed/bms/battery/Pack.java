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
    private final BmsDevicePosition[] bmsDevicesPositions;

    public Pack(int[] numberOfCellsPerSegment, int numberOfCellsPerBms) {
        segments = createSegments(numberOfCellsPerSegment,
                numberOfCellsPerBms);
        bmsDevicesPositions = findBmsDevicePositions(
                getNumberOfBmsDevices(),
                segments
        );
    }

    /*
     * General info
     */

    /**
     * Creates list of segments for pack
     *
     * @param numberOfCellsPerSegment # cell in each segment
     * @param numberOfCellsPerBms # cells controlled by each bms
     * @return list of segments
     */
    public static Segment[] createSegments(
            int[] numberOfCellsPerSegment, int numberOfCellsPerBms) {
        final int numberOfSegments = numberOfCellsPerSegment.length;
        Segment[] segments = new Segment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {  // open segments
            segments[i] = new Segment(numberOfCellsPerSegment[i],
                    numberOfCellsPerBms);
        }
        return segments;
    }

    /**
     * Finds number of segment of bms device
     *
     * @param bmsDevice # of bms device
     * @param segments  list of segments to look into
     * @return index of segment with bms device
     */
    public static BmsDevicePosition getPositionOfBmsDevice(
            int bmsDevice, Segment[] segments) {
        if (bmsDevice < 0) {
            throw new IllegalArgumentException("Cannot find bms device #" +
                    Integer.toString(bmsDevice));
        }

        int bmsDevicesCounter = 0;
        for (int i = 0; i < segments.length; ++i) {
            int bmsDevicesInSegment = segments[i].getNumberOfBmsDevices();
            if (bmsDevice < bmsDevicesCounter + bmsDevicesInSegment) {
                int positionRelativeToSegment = bmsDevice - bmsDevicesCounter;
                return new BmsDevicePosition(positionRelativeToSegment, i);
            }

            bmsDevicesCounter += bmsDevicesInSegment;  // update counter
        }

        throw new IllegalArgumentException("Cannot find bms device #" +
                Integer.toString(bmsDevice));
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
     * Number of segments in battery pack
     *
     * @return length of list of segments
     */
    public int getNumberOfSegments() {
        return segments.length;
    }

    /**
     * Find number of bms devices in pack
     *
     * @return number of bms devices in pack
     */
    public int getNumberOfBmsDevices() {
        int bmsDevicesCounter = 0;
        for (Segment s : segments) {
            int bmsDevicesInSegment = s.getNumberOfBmsDevices();
            bmsDevicesCounter += bmsDevicesInSegment;  // update counter
        }
        return bmsDevicesCounter;
    }

    /*
     * Temperatures and voltages
     */

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
     * Finds position of each bms device in segments
     * @param totalBmsDevices total number of bms devices
     * @param segments segments to look into
     * @return list of positions (each position is relative to one device)
     */
    public static BmsDevicePosition[] findBmsDevicePositions(
            int totalBmsDevices, Segment[] segments) {
        BmsDevicePosition[] positions = new BmsDevicePosition[totalBmsDevices];
        for (int i = 0; i < totalBmsDevices; i++) {
            positions[i] = getPositionOfBmsDevice(i, segments);
        }
        return positions;
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
     * Retrieves temperature of bms device
     *
     * @param bmsDevice # of bms device
     * @return temperature of bms device
     */
    public double getTemperatureOfBms(int bmsDevice) {
        BmsDevicePosition position = bmsDevicesPositions[bmsDevice];
        return segments[position.segment].getTemperatureOfBms(position
                .positionInSegment);
    }

    /**
     * Retrieve average voltage value of pack
     *
     * @return average voltage
     */
    public double getVoltage() {
        double sum = 0.0;
        for (Segment segment : segments) {
            sum += segment.getVoltage();
        }
        return sum;
    }

    /**
     * Retrieves voltage of bms device
     *
     * @param bmsDevice # of bms device
     * @return voltage of bms device
     */
    public double getVoltageOfBms(int bmsDevice) {
        BmsDevicePosition position = bmsDevicesPositions[bmsDevice];
        return segments[position.segment].getVoltageOfBms(position
                .positionInSegment);
    }
}
