package com.raceup.ed.bms.gui.frame.data;

import com.raceup.ed.bms.gui.frame.chart.ChartFrame;
import com.raceup.ed.bms.utils.gui.JPanelsUtils;

import javax.swing.*;
import java.awt.*;

public class BmsDevice extends NumAlerter {
    public static final double[] TEMPERATURE_BOUNDS = new double[]{-100,
            60};  // too low, too high values
    public Cell[] cells;
    private final JLabel temperature1Label = new JLabel("0000.00");
    private final JLabel temperature2Label = new JLabel("0000.00");
    private JLabel label = new JLabel("BMS #");

    public BmsDevice(int index, int numberOfCells) {
        setup(index, numberOfCells);
    }

    public void setTemperature1(double value) {
        update(temperature1Label, TEMPERATURE_BOUNDS, value);
    }

    public void setTemperature2(double value) {
        update(temperature2Label, TEMPERATURE_BOUNDS, value);
    }

    public double getTemperature1() {
        return Double.parseDouble(temperature1Label.getText());
    }

    public double getTemperature2() {
        return Double.parseDouble(temperature2Label.getText());
    }

    public double getAverageTemperature() {
        return (getTemperature1() + getTemperature2()) / 2.0;
    }

    public void setVoltage(int index, double value) {
        cells[index].setVoltage(value);
        update(label, Cell.VOLTAGE_BOUNDS, value);
    }

    public double getVoltage(int index) {
        return cells[index].getVoltage();
    }

    private void setupVoltageLabels(int numberOfCells) {
        JPanel cellsPanel = new JPanel();
        cellsPanel.setLayout(new BoxLayout(cellsPanel, BoxLayout.X_AXIS));

        cells = new Cell[numberOfCells];
        for (int i = 0; i < numberOfCells; i++) {
            cells[i] = new Cell(i);
            cellsPanel.add(cells[i]);
        }

        JPanelsUtils.addTitleBorderOnPanel(cellsPanel, "Voltages");
        add(cellsPanel);
    }

    private void setupTemperatureLabels() {
        JPanel temperaturePanel = new JPanel();
        temperaturePanel.setLayout(new BoxLayout(temperaturePanel, BoxLayout
                .Y_AXIS));

        temperature1Label.setHorizontalAlignment(SwingConstants.RIGHT);
        temperature2Label.setHorizontalAlignment(SwingConstants.RIGHT);

        temperaturePanel.add(temperature1Label);
        temperaturePanel.add(temperature2Label);

        add(temperaturePanel);
    }

    private void setupFlagLabels() {
        JPanel flagPanel = new JPanel();
        flagPanel.setLayout(new BoxLayout(flagPanel, BoxLayout
                .Y_AXIS));

        flagPanel.add(new JLabel("1"));
        flagPanel.add(new JLabel("0"));

        add(flagPanel);
    }

    private void setup(int index, int numberOfCells) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        label = new JLabel("BMS " + Integer.toString(index));
        add(label);
        add(Box.createRigidArea(new Dimension(10, 0)));  // add spacing

        setupVoltageLabels(numberOfCells);
        add(Box.createRigidArea(new Dimension(10, 0)));  // add spacing

        setupTemperatureLabels();
        add(Box.createRigidArea(new Dimension(10, 0)));  // add spacing

        setupFlagLabels();
        setVisible(true);  // open
    }

    /**
     * Show dialog with more info about segment
     *
     * @param title title of dialog
     */
    void showDialog(String title) {
        String[] voltageLabels = new String[cells.length];  // labels of chart
        String[] temperatureLabels = new String[cells.length];
        for (int c = 0; c < cells.length; c++) {
            voltageLabels[c] = "Voltage of cell " + Integer.toString(c + 1);
            temperatureLabels[c] = "Temperature of cell " + Integer.toString
                    (c + 1);
        }

        ChartFrame voltagesChart = createAndSetupChart("Voltage of cells in " +
                "" + title, voltageLabels);  // create charts
        ChartFrame temperaturesChart = createAndSetupChart("Temperature of " +
                "cells in " + title, temperatureLabels);
        temperaturesChart.setLocation(voltagesChart.getX(), voltagesChart
                .getY() + voltagesChart.getHeight());  // under voltages chart

        Timer updater = new Timer(50, e -> {
            for (int c = 0; c < cells.length; c++) {
                voltagesChart.updateOrFail(c, getVoltage(c));
                temperaturesChart.updateOrFail(c, getAverageTemperature());
            }
        });  // timer to update dialog values
        updater.start();
    }

    /**
     * Create and setup chart info dialog
     *
     * @param title         title of frame
     * @param titleOfSeries list of title of series to add to chart
     * @return info dialog with chart
     */
    private ChartFrame createAndSetupChart(final String title, final
    String[] titleOfSeries) {
        ChartFrame dialog = new ChartFrame(title, titleOfSeries);
        dialog.setLocationRelativeTo(null);  // center in screen
        return dialog;
    }
}
