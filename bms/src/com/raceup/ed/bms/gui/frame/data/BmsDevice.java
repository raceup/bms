package com.raceup.ed.bms.gui.frame.data;

import com.raceup.ed.bms.utils.gui.JPanelsUtils;

import javax.swing.*;

public class BmsDevice extends NumAlerter {
    private static final double[] TEMPERATURE_BOUNDS = new double[]{-100,
            60};  // too low, too high values
    private Cell[] cells;
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

    public void setVoltage(int index, double value) {
        cells[index].setVoltage(value);
        update(label, Cell.VOLTAGE_BOUNDS, value);
    }

    private void setupVoltageLabels(int numberOfCells) {
        JPanel cellsPanel = new JPanel();
        cellsPanel.setLayout(new BoxLayout(cellsPanel, BoxLayout.Y_AXIS));

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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        label = new JLabel("BMS " + Integer.toString(index));
        add(label);

        setupVoltageLabels(numberOfCells);
        setupTemperatureLabels();
        setupFlagLabels();

        setVisible(true);  // open
    }
}
