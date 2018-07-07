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


package com.raceup.ed.bms.ui.frame.chart;

import com.raceup.ed.bms.ui.panel.chart.ChartPanel;

import javax.swing.*;

/**
 * Frame containing chart with data from BMS
 */
public class ChartFrame extends JFrame {
    private ChartPanel chartPanel;

    /**
     * Create new frame with chart with title
     *
     * @param title         title of frame
     * @param titleOfSeries list of title of series to add to chart
     */
    public ChartFrame(final String title, final String[] titleOfSeries) {
        super(title);  // new frame with title
        chartPanel = new ChartPanel(
                titleOfSeries
        );

        setupLayout();
    }

    /**
     * Adds new value and plot it
     *
     * @param timeSeries time series to update
     * @param var        new value to plot and add in data-set
     */
    public void updateOrFail(int timeSeries, double var) {
        chartPanel.updateSeriesOrFail(timeSeries, var);
    }

    /**
     * Setups layout
     */
    private void setupLayout() {
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS)
        );

        add(chartPanel);  // add chart in chartPanel
        pack();
        setVisible(true);
    }
}
