/*
 * Copyright 2016-2017 RaceUp Team ED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.raceup.ed.bms.gui.frame.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;

/**
 * Frame containing chart with data from BMS
 */
public class ChartFrame extends JFrame {
    private static final int CHART_RANGE_SECONDS = 60 * 1000;  // chart range in seconds
    private TimeSeries[] series;  // list of time series of chart

    /**
     * Create new frame with chart with title
     *
     * @param title         title of frame
     * @param titleOfSeries list of title of series to add to chart
     */
    public ChartFrame(final String title, final String[] titleOfSeries) {
        super(title);  // new frame with title
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        TimeSeriesCollection dataset = createChartDataset(titleOfSeries);
        ChartPanel chartPanel = new ChartPanel(createChart(dataset));
        add(chartPanel);  // add chart in chartPanel

        pack();
        setVisible(true);
    }

    /**
     * Adds new value and plot it
     *
     * @param timeSeries time series to update
     * @param var        new value to plot and add in dataset
     */
    public void updateSeriesOrFail(int timeSeries, double var) {
        try {
            series[timeSeries].addOrUpdate(new Millisecond(), var);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Setup chart with series
     *
     * @param titleOfSeries list of title of series to add to chart
     * @return new dataset with given series
     */
    private TimeSeriesCollection createChartDataset(String[] titleOfSeries) {
        series = new TimeSeries[titleOfSeries.length];  // create list of time series
        final TimeSeriesCollection dataset = new TimeSeriesCollection();  // create chart dataset

        for (int i = 0; i < titleOfSeries.length; i++) {  // loop through series
            series[i] = new TimeSeries(titleOfSeries[i], Millisecond.class);  // setup series
            dataset.addSeries(series[i]);
        }
        return dataset;
    }

    /**
     * Create chart and add dataset to it.
     *
     * @param dataset dataset of chart
     * @return chart chart with title and dataset
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "",
                "Time (seconds)",
                "Values",
                dataset,
                true,  // legend
                false,  // tooltips
                false  // urls
        );

        final XYPlot plot = chart.getXYPlot();  // configure chart
        plot.getDomainAxis().setFixedAutoRange(CHART_RANGE_SECONDS);  // range on time axis
        plot.getRangeAxis().setAutoRange(true);  // range on domain axis
        return chart;
    }
}
