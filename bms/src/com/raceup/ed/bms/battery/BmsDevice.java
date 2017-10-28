package com.raceup.ed.bms.battery;

public class BmsDevice extends Cell {
    private double temperature1;
    private double temperature2;

    /**
     * Retrieve temperature value
     *
     * @return temperature of cell
     */
    public synchronized double getTemperature1() {
        return temperature1;
    }

    /**
     * Retrieve temperature value
     *
     * @return temperature of cell
     */
    public synchronized double getTemperature2() {
        return temperature2;
    }

    @Override
    public synchronized double getTemperature() {
        return (temperature1 + temperature2) / 2.0;  // average
    }

    /**
     * Updatse voltage of device
     *
     * @param temperature1 temp1
     * @param temperature2 temp2
     */
    public synchronized void setTemperature(double temperature1, double
            temperature2) {
        this.temperature1 = temperature1;
        this.temperature2 = temperature2;
    }
}
