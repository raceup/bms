package com.raceup.ed.bms.models.battery;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class BmsDevice implements BmsControllable {
    public static final double[] VOLTAGE_BOUNDS = new double[]{3000.0,
            4200.0};
    public static final double[] TOT_VOLTAGE_BOUNDS = new double[]{
            426.0, 596.4};
    public static final double[] TEMPERATURE_BOUNDS = new double[]{0.0,
            60.0};
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
        return Math.max(getTemperature1(), getTemperature2());
    }

    @Override
    public double getVoltage() {
        double sum = 0.0;
        for (double voltage : voltages) {
            sum += voltage;
        }
        return sum;
    }

    public double getMinVoltage() throws NoSuchElementException {
        double result = Double.MAX_VALUE;
        for (double voltage : voltages) {
            if (voltage > 0 && voltage < result) {
                result = voltage;
            }
        }

        if (result == Double.MAX_VALUE) {
            throw new NoSuchElementException("Cannot find min");
        }

        return result;
    }

    public double getMaxVoltage() throws NoSuchElementException {
        double result = Double.MIN_VALUE;
        for (double voltage : voltages) {
            if (voltage > 0 && voltage > result) {
                result = voltage;
            }
        }

        if (result == Double.MIN_VALUE) {
            throw new NoSuchElementException("Cannot find max");
        }

        return result;
    }

    public double getAvgVoltage() throws NoSuchElementException {
        double result = 0.0;
        int samples = 0;
        for (double voltage : voltages) {
            if (voltage > 0) {
                result += voltage;
                samples += 1;
            }
        }

        if (result == 0.0) {
            throw new NoSuchElementException("Cannot find avg");
        }

        result /= samples;
        return result;
    }

    public HashMap<String, Double> getCurrentValues() {
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
            map.put("avg", getAvgVoltage());
        } catch (Exception e) {
            map.put("avg", null);
        }

        try {
            map.put("t1", getTemperature1());
        } catch (Exception e) {
            map.put("t1", null);
        }

        try {
            map.put("t2", getTemperature2());
        } catch (Exception e) {
            map.put("t2", null);
        }

        return map;
    }
}
