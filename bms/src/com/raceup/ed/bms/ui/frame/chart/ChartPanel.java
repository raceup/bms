package com.raceup.ed.bms.ui.frame.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class ChartPanel extends org.jfree.chart.ChartPanel {
    private static final int CHART_RANGE = 60 * 1000;  // seconds

    /**
     * Create new frame with chart with title
     *
     * @param titleOfSeries list of title of series to add to chart
     */
    public ChartPanel(final String[] titleOfSeries) {
        super(
                ChartFactory.createTimeSeriesChart(
                        "",  // no title
                        "Time (seconds)",
                        "Values",
                        createChartDataSet(titleOfSeries),
                        true,  // legend
                        false,  // tooltips
                        false  // urls
                )
        );
        configureChart();
    }

    /**
     * Setup chart with series
     *
     * @param titleOfSeries list of title of series to add to chart
     * @return new dataset with given series
     */
    private static TimeSeriesCollection createChartDataSet(
            String[] titleOfSeries) {
        TimeSeries[] series = new TimeSeries[titleOfSeries.length];  // create
        // list of
        // time series
        final TimeSeriesCollection dataSet = new TimeSeriesCollection();  //
        // create chart data set

        for (int i = 0; i < titleOfSeries.length; i++) {  // loop through
            // series
            series[i] = new TimeSeries(titleOfSeries[i]);
            // setup series
            dataSet.addSeries(series[i]);
        }
        return dataSet;
    }

    /**
     * Adds new value and plot it
     *
     * @param timeSeries time series to update
     * @param var        new value to plot and add in dataset
     */
    public void updateSeriesOrFail(int timeSeries, double var) {
        try {
            TimeSeriesCollection dataSet = (TimeSeriesCollection) getChart()
                    .getXYPlot()
                    .getDataset();
            dataSet.getSeries(timeSeries).addOrUpdate(new Millisecond(), var);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Personalizes and configures chart to have range of 1 minute
     */
    private void configureChart() {
        final XYPlot plot = getChart().getXYPlot();  // configure chart
        plot.getDomainAxis().setFixedAutoRange(CHART_RANGE);  //
        // range on time axis
        plot.getRangeAxis().setAutoRange(true);  // range on domain axis
    }
}
