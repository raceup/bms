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
    public int getNumberOfSegments() {
        return segments.length;
    }

    /**
     * Number of segments in battery pack
     *
     * @return length of list of segments
     */
    public int getNumberOfBms() {
        return getNumberOfBmsPerSegment() * getNumberOfSegments();
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
        double result = 0.0;
        int samples = 0;
        for (Segment segment : segments) {
            double temperature = segment.getTemperature();
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

    public double getMaxTemperature() {
        double result = Double.MIN_VALUE;
        for (Segment segment : segments) {
            double temperature = segment.getTemperature();
            if (temperature > 0 && temperature > result) {
                result = temperature;
            }
        }

        if (result == Double.MIN_VALUE) {
            throw new NoSuchElementException("Cannot find max");
        }

        return result;
    }

    /**
     * Update temperature of cell of given cell
     *
     * @param value new temperature reading
     */
    public void setTemperature1(int bms, double value) {
        segments[getSegmentOfBms(bms)].setTemperature1OfBms(
                getBmsIndex(bms), value
        );
    }

    public void setTemperature2(int bms, double value) {
        segments[getSegmentOfBms(bms)].setTemperature2OfBms(
                getBmsIndex(bms), value
        );
    }

    public double getTemperature1(int bms) {
        return segments[getSegmentOfBms(bms)].getTemperature1OfBms(
                getBmsIndex(bms));
    }

    public double getTemperature2(int bms) {
        return segments[getSegmentOfBms(bms)].getTemperature2OfBms(
                getBmsIndex(bms));
    }

    /**
     * Update voltage of cell of given cell
     *
     * @param value      new voltage reading
     */
    public void setVoltage(int bms, int cell, double value) {
        segments[getSegmentOfBms(bms)].setVoltageOfBms(
                getBmsIndex(bms), cell, value
        );
    }

    /**
     * Retrieve average voltage value of pack
     *
     * @return average voltage
     */
    public double getVoltage() {
        double result = 0.0;
        for (Segment segment : segments) {
            double voltage = segment.getVoltage();
            if (voltage > 0) {
                result += voltage;
            }
        }

        if (result == 0.0) {
            throw new NoSuchElementException("Cannot find tot");
        }

        return result;
    }

    public double getVoltage(int bms, int cell) {
        return segments[getSegmentOfBms(bms)].getVoltage(
                getBmsIndex(bms), cell);
    }

    public double getMinVoltage(int bms) {
        return segments[getSegmentOfBms(bms)].getMinVoltage(getBmsIndex(bms));
    }

    public double getMaxVoltage(int bms) {
        return segments[getSegmentOfBms(bms)].getMaxVoltage(getBmsIndex(bms));
    }

    public double getAvgVoltage(int bms) {
        return segments[getSegmentOfBms(bms)].getAvgVoltage(getBmsIndex(bms));
    }

    public double getMinVoltage() {
        ArrayList<Double> voltages = new ArrayList<>();
        for (Segment segment : segments) {
            try {
                voltages.add(segment.getMinVoltage());
            } catch (Exception e) {
            }
        }

        int index = voltages.indexOf(Collections.min(voltages));
        return voltages.get(index);
    }

    public double getMaxVoltage() {
        ArrayList<Double> voltages = new ArrayList<>();
        for (Segment segment : segments) {
            try {
                voltages.add(segment.getMaxVoltage());
            } catch (Exception e) {
            }
        }

        int index = voltages.indexOf(Collections.min(voltages));
        return voltages.get(index);
    }

    public int getSegmentOfBms(int bms) {
        return bms / getNumberOfBmsPerSegment();
    }

    public int getBmsIndex(int bms) {
        return bms % getNumberOfBmsPerSegment();
    }

    public HashMap<String, Double> getInfoOverall() {
        HashMap<String, Double> map = new HashMap<>();
        try {
            map.put("min", getMinVoltage());
        } catch (Exception e) {
            map.put("min", null);
        }

        try {
            map.put("max", getMaxVoltage());
        } catch (Exception e) {
            map.put("max", null);
        }

        try {
            map.put("tot", getVoltage());
        } catch (Exception e) {
            map.put("tot", null);
        }

        try {
            map.put("tMax", getMaxTemperature());
        } catch (Exception e) {
            map.put("tMax", null);
        }

        return map;
    }

    public HashMap<String, Double> getCurrentValues(int bms) {
        try {
            return segments[getSegmentOfBms(bms)].getCurrentValues
                    (getBmsIndex(bms));
        } catch (Exception e) {
            return null;
        }
    }
}
