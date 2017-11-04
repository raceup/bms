package com.raceup.ed.bms.utils;

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
        double minSegment = 0, maxSegment = 8;
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
}
