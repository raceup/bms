package com.raceup.ed.bms.battery;

public interface BmsControllable {
    /**
     * Retrieve temperature value
     *
     * @return temperature of cell
     */
    double getTemperature();

    /**
     * Update temperature of cell
     *
     * @param temperature new temperature reading
     */
    void setTemperature(double temperature);

    /**
     * Retrieve voltage value
     *
     * @return voltage of cell
     */
    double getVoltage();

    /**
     * Update voltage of cell
     *
     * @param voltage new voltage reading
     */
    void setVoltage(double voltage);
}
