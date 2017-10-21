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

package com.raceup.ed.bms.gui.frame.data;

import com.raceup.ed.bms.gui.frame.chart.ChartFrame;

import javax.swing.*;
import java.awt.*;

/**
 * GUI frame that contains entire battery segment
 */
class Segment extends JPanel {
    final Cell[] cells;  // battery cells

    /**
     * New GUI frame representing a battery segment
     *
     * @param numberOfCells number of cells in segment
     */
    Segment(int numberOfCells) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));  // horizontal alignment
        cells = new Cell[numberOfCells];  // build cells list
        setup();
    }

    /*
     * Dialog to show more info
     */

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
            temperatureLabels[c] = "Temperature of cell " + Integer.toString(c + 1);
        }

        ChartFrame voltagesChart = createAndSetupChart("Voltage of cells in " + title, voltageLabels);  // create charts
        ChartFrame temperaturesChart = createAndSetupChart("Temperature of cells in " + title, temperatureLabels);
        temperaturesChart.setLocation(voltagesChart.getX(), voltagesChart.getY() + voltagesChart.getHeight());  // under voltages chart

        Timer updater = new Timer(10, e -> {
            for (int c = 0; c < cells.length; c++) {
                voltagesChart.updateSeriesOrFail(c, getVoltageOfCell(c));
                temperaturesChart.updateSeriesOrFail(c, getTemperatureOfCell(c));
            }
        });  // timer to update dialog values
        updater.start();
    }

    /*
     * Getters and setters
     */

    /**
     * Retrieve temperature value of given cell
     *
     * @param cellPosition cell position in segment (numbers open from 0)
     * @return temperature of cell
     */
    double getTemperatureOfCell(int cellPosition) {
        return cells[cellPosition].getTemperature();
    }

    /**
     * Retrieve voltage value of given cell
     *
     * @param cellPosition cell position in segment (numbers open from 0)
     * @return voltage of cell
     */
    double getVoltageOfCell(int cellPosition) {
        return cells[cellPosition].getVoltage();
    }

    /**
     * Update temperature in selected cell
     *
     * @param cellNumber position of cell to updateSeriesOrFail (open at 0)
     * @param value      new value
     */
    void setTemperatureOfCell(int cellNumber, double value) {
        cells[cellNumber].setTemperature(value);
    }

    /**
     * Update voltage in selected cell
     *
     * @param cellNumber position of cell to updateSeriesOrFail (open at 0)
     * @param value      new value
     */
    void setVoltageOfCell(int cellNumber, double value) {
        cells[cellNumber].setVoltage(value);
    }

    /*
     * Setup
     */

    /**
     * Setup gui and widgets
     */
    private void setup() {
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.PAGE_AXIS));  // vertical alignment
        labelsPanel.add(new JLabel("Cell number"));  // add headers
        labelsPanel.add(new JLabel("Temperature (K)"));
        labelsPanel.add(new JLabel("Voltage (mV)"));
        add(labelsPanel);  // add headers in main panel
        add(Box.createRigidArea(new Dimension(20, 0)));  // add spacing

        for (int i = 0; i < cells.length; i++) {  // add all cells
            cells[i] = new Cell();

            JPanel cellPanel = new JPanel();
            cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.PAGE_AXIS));  // vertical alignment
            cellPanel.add(new JLabel(Integer.toString(i + 1)));  // number of cell
            cellPanel.add(cells[i]);
            add(cellPanel);
            add(Box.createRigidArea(new Dimension(10, 0)));  // add spacing
        }
        setVisible(true);
    }

    /**
     * Create and setup chart info dialog
     *
     * @param title         title of frame
     * @param titleOfSeries list of title of series to add to chart
     * @return info dialog with chart
     */
    private ChartFrame createAndSetupChart(final String title, final String[] titleOfSeries) {
        ChartFrame dialog = new ChartFrame(title, titleOfSeries);
        dialog.setLocationRelativeTo(null);  // center in screen
        return dialog;
    }
}
