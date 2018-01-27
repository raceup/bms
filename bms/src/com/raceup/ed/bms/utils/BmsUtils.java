package com.raceup.ed.bms.utils;

import com.raceup.ed.bms.battery.BmsDevice;
import com.raceup.ed.bms.battery.BmsDevicePosition;
import com.raceup.ed.bms.battery.Segment;
import com.raceup.ed.bms.stream.bms.data.BmsData;

public class BmsUtils {
    /**
     * Gets raw data from pre-defined values
     *
     * @param type    type of data
     * @param cell    number of cell broadcasting value
     * @param segment number of segment broadcasting value
     * @param value   value of cell of segment
     * @return raw data from pre-defined values
     */
    public static String getRawDataFromValues(String type, String cell, String
            segment, String value) {
        BmsData data = new BmsData(type, cell, segment, value);
        return data.getJsonValue();
    }

    /**
     * Creates random data to simulate Arduino incoming data
     *
     * @return random data as if Arduino was running
     */
    public static String getRandomData() {
        String type, value;
        double randTypeNum = Math.random();
        double minCell = 0, maxCell = 18;
        double minSegment = 0, maxSegment = 2;
        double randCell = minCell + Math.random() * (maxCell - minCell);
        double randSegment = minSegment + Math.random() * (maxSegment -
                minCell);

        String cell = Integer.toString((int) randCell);
        String segment = Integer.toString((int) randSegment);

        double rawValueProb = 0.75;

        if (randTypeNum < rawValueProb) {  // generate random value
            if (randTypeNum < rawValueProb / 2) {  // value is voltage
                type = "voltage";

                double minVolt = 3950, maxVolt = 4050;
                double randVolt = minVolt + Math.random() * (maxVolt -
                        minVolt);
                value = Integer.toString((int) randVolt);
            } else {  // value is temperature
                type = "temperature";

                double minTemp = 25, maxTemp = 45;
                double randTemp = minTemp + Math.random() * (maxTemp -
                        minTemp);
                value = Double.toString(randTemp);
            }
        } else {  // generate random status
            double randStatusNum = Math.random();
            if (randStatusNum < 1.0 / 3) {
                type = "Alert";
                value = "Possible warning in cell " + cell + ", segment " +
                        segment;
            } else if (randStatusNum < 2.0 / 3) {
                type = "Fault";
                value = "Fault in cell " + cell + ", segment " + segment;
            } else {
                type = "Log";
                value = "All right down here for as far as I can tell";
            }
        }

        return getRawDataFromValues(type, cell, segment, value);
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
     * Finds position of each bms device in segments
     *
     * @param totalBmsDevices total number of bms devices
     * @param segments        segments to look into
     * @return list of positions (each position is relative to one device)
     */
    public static BmsDevicePosition[] generateBmsDevicePositions(
            int totalBmsDevices, Segment[] segments) {
        BmsDevicePosition[] positions = new BmsDevicePosition[totalBmsDevices];
        for (int i = 0; i < totalBmsDevices; i++) {
            positions[i] = getPositionOfBmsDevice(i, segments);
        }
        return positions;
    }


    /**
     * Creates list of segments for pack
     *
     * @param numberOfBmsPerSegment # cell in each segment
     * @param numberOfCellsPerBms   # cells controlled by each bms
     * @return list of segments
     */
    public static Segment[] createSegments(
            int[] numberOfBmsPerSegment, int numberOfCellsPerBms) {
        final int numberOfSegments = numberOfBmsPerSegment.length;
        Segment[] segments = new Segment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {  // open segments
            segments[i] = new Segment(numberOfBmsPerSegment[i],
                    numberOfCellsPerBms);
        }
        return segments;
    }

    /**
     * Creates list of bms devices for segment
     *
     * @param numberOfCells       # cell in segment to be controlled by bms
     *                            devices
     * @param numberOfCellsPerBms # cells controlled by each bms device
     * @return list of bms devices for segment
     */
    public static BmsDevice[] createBmsDevices(int numberOfCells, int
            numberOfCellsPerBms) {
        int numberOfBmsDevices = (int) Math.ceil(numberOfCells /
                numberOfCellsPerBms);
        BmsDevice[] bmsDevices = new BmsDevice[numberOfBmsDevices];
        for (BmsDevice bmsDevice : bmsDevices) {
            bmsDevice = new BmsDevice(numberOfCellsPerBms);
        }
        return bmsDevices;
    }
}
