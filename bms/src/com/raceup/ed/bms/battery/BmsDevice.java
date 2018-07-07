package com.raceup.ed.bms.battery;

public class BmsDevice implements BmsControllable {
    private double[] voltages;
    private double temperature1;
    private double temperature2;

    public BmsDevice() {
        this(6);
    }

    /**
     * Builds new Bms device
     *
     * @param numberOfVoltages number of voltages in BMS
     */
    public BmsDevice(int numberOfVoltages) {
        voltages = new double[numberOfVoltages];
    }

    /**
     * Gets temperature 1
     * @return temperature 1
     */
    public double getTemperature1() {
        return temperature1;
    }

    /**
     * Sets temperature 2 value
     * @param value temperature 2
     */
    public void setTemperature1(double value) {
        temperature1 = value;
    }

    /**
     * Gets temperature 2
     * @return temperature 2
     */
    public double getTemperature2() {
        return temperature2;
    }

    /**
     * Sets temperature 2
     * @param value temperature 2
     */
    public void setTemperature2(double value) {
        temperature2 = value;
    }

    /**
     * Gets voltage of specific cell
     * @param index number of cell
     * @return voltage of cell
     */
    public double getVoltage(int index) {
        return voltages[index];
    }

    /**
     * Sets voltage of specific cell
     * @param index number of cell
     * @param value voltage of cell
     */
    public void setVoltage(int index, double value) {
        voltages[index] = value;
    }

    @Override
    public double getTemperature() {
        return (getTemperature1() + getTemperature2()) / 2.0;
    }

    @Override
    public double getVoltage() {
        double sum = 0.0;
        for (double voltage : voltages) {
            sum += voltage;
        }
        return sum / voltages.length;
    }
}
