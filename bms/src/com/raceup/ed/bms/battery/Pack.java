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

    /**
     * Builds new battery pack model
     *
     * @param numberOfBmsPerSegment number of bms in each segment
     */
    public Pack(int numberOfSegments, int numberOfBmsPerSegment) {
        segments = new Segment[numberOfSegments];
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new Segment(numberOfBmsPerSegment);
        }
    }

    /*
     * General info
     */

    /**
     * Number of cells for each segment in battery pack
     *
     * @return list of number of cells
     */
    public int getNumberOfBmsPerSegment() {
        return segments[0].getNumberOfBms();
    }

    /**
     * Number of segments in battery pack
     *
     * @return length of list of segments
     */
    private int getNumberOfSegments() {
        return segments.length;
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
        return sum / segments.length;
    }

    /**
     * Update temperature of cell of given cell
     *
     * @param segment      segment number
     * @param value new temperature reading
     */
    public void setTemperature1(int segment, int bms, double value) {
        segments[segment].setTemperature1OfBms(bms, value);
    }

    public void setTemperature2(int segment, int bms, double value) {
        segments[segment].setTemperature2OfBms(bms, value);
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param segment      segment number
     * @param value      new voltage reading
     */
    public void setVoltage(int segment, int bms, int cell, double value) {
        segments[segment].setVoltageOfBms(bms, cell, value);
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
}
