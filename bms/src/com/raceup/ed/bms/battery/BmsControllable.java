package com.raceup.ed.bms.battery;

/**
 * Something that is controlled by a BMS device or that has voltage and
 * temperatures features
 */
public interface BmsControllable {
    /**
     * Retrieve temperature value
     *
     * @return temperature of cell
     */
    double getTemperature();

    /**
     * Retrieve voltage value
     *
     * @return voltage of cell
     */
    double getVoltage();
}
