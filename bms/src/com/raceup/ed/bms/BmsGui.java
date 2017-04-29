/*
 *  Copyright 2016-2017 RaceUp ED
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


package com.raceup.ed.bms;

import com.raceup.ed.bms.gui.frame.chart.ChartFrame;
import com.raceup.ed.bms.gui.frame.data.DataFrame;
import com.raceup.ed.bms.gui.frame.log.LogFrame;
import com.raceup.ed.bms.stream.bms.data.BmsData;
import com.raceup.ed.bms.stream.bms.data.BmsLog;
import com.raceup.ed.bms.stream.bms.data.BmsValue;
import com.raceup.ed.bms.utils.gui.AboutDialog;
import com.raceup.ed.bms.utils.gui.JPanelsUtils;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.raceup.ed.bms.utils.Streams.readAllFromStream;

/**
 * Font-end (gui) of BMS
 * - ~spreadsheet with rae data values and dialog to get more info (i.e DataFrame)
 * - chart with total tension of battery (i.e ChartFrame)
 * - text area with errors (i.e ErrorsAreaFrame)
 */
public class BmsGui extends ApplicationFrame implements Runnable, StartAndStop {
    static final Image appIcon = Toolkit.getDefaultToolkit().getImage("com.raceup.ed.bms.BmsGui".getClass().getResource("/res/images/icon.png"));
    private static final Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension QUARTER_SCREEN = new Dimension((int) (SCREEN.getWidth() * 0.5), (int) (SCREEN.getHeight() * 0.5));
    private static int msGuiIntervalUpdate = 10;  // GUI interval update
    private final Bms bms;  // bms manager
    private final JButton startButton = new JButton("Start");  // start and stop buttons
    private final JButton pauseButton = new JButton("Pause");
    private final JButton stopButton = new JButton("Stop");
    private final JButton rechargeButton = new JButton("Recharge");  // recharge button
    private volatile boolean amIStarted = false;  // start and stop management
    private volatile boolean amIPaused = false;
    private volatile boolean amIStopped = false;
    private DataFrame dataFrame;  // gui frames
    private ChartFrame chartFrame;
    private LogFrame logFrame;  // frame used for logging

    /**
     * Prepare and run gui
     *
     * @param bms bms manager to monitor
     */
    public BmsGui(Bms bms) {
        super("Bms manager");  // set title
        setIconImage(appIcon);  // set icon
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "AppName");  // set app name

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // destroy app when closed
        this.bms = bms;  // setup bms

        setup();
    }

    /**
     * Start frontend GUI and backend engines
     */
    public void open() {
        dataFrame.pack();
        dataFrame.setLocation(0, 0);  // top left corner
        dataFrame.setVisible(true);

        chartFrame.pack();
        chartFrame.setLocation(dataFrame.getX(), dataFrame.getY() + dataFrame.getHeight());  // under data frame
        chartFrame.setVisible(true);

        logFrame.setSize(new Dimension((int) (QUARTER_SCREEN.getWidth() * 0.5), (int) (QUARTER_SCREEN.getHeight() * 0.5)));
        logFrame.setLocation(chartFrame.getX() + chartFrame.getWidth(), chartFrame.getY());  // right of chart frame
        logFrame.setVisible(true);

        pack();
        setLocation((int) SCREEN.getWidth(), 0);  // right top corner
        setVisible(true);
    }

    /*
     * Thread
     */

    /**
     * Start GUI and bms
     */
    public void start() {
        if (!amIStopped && !amIStarted) {  // only when i'm not stopped
            bms.start();  // start bms thread
            amIStarted = true;  // start updating

            if (!amIPaused) {  // first time to start
                new Thread(this, this.getClass().getName()).start();  // start gui thread
            }
        }
    }

    /**
     * Pause reading data from arduino and updating GUI
     */
    public void pause() {
        amIStarted = false;  // stop updating
        amIPaused = true;

        bms.pause();
    }

    /**
     * Close arduino connection but keep GUI on screen (stop updating)
     */
    public void stop() {
        amIStarted = false;
        amIPaused = false;
        amIStopped = true;

        bms.stop();  // stop bms backend
    }

    /**
     * Start monitoring arduino port in new thread and updateOrFail series screen with values
     */
    public void run() {
        while (!amIStopped) {
            while (amIStarted) {
                updateOrFail();
            }
        }
    }

    /*
     * Setup main frame
     */

    /**
     * Setup gui and backend
     */
    private void setup() {
        int[] numberOfCellsPerSegment = this.bms.batteryPack.getNumberOfCellsPerSegment();

        setupFrame();  // setup frame manager
        chartFrame = new ChartFrame("Average voltage and temperature of battery pack over time", new String[]{"Voltage (mV)"});  // setup gui
        dataFrame = new DataFrame(numberOfCellsPerSegment);
        logFrame = new LogFrame();
    }

    /**
     * Setup gui of main frame (manager)
     */
    private void setupFrame() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));  // add components vertically
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // border

        setJMenuBar(createMenuBar());  // set menubar
        add(createStartStopPanel());  // add panels
        add(createRechargePanel());
    }

    /**
     * Create panel that starts and stops monitor the bms
     *
     * @return panel with buttons to monitor bms
     */
    private JPanel createStartStopPanel() {
        JPanel panel = new JPanel();
        JPanelsUtils.addTitleBorderOnPanel(panel, "Run monitor");

        startButton.addActionListener(e -> start());  // add action listeners
        pauseButton.addActionListener(e -> pause());
        stopButton.addActionListener(e -> stop());

        panel.add(startButton);  // add to panel
        panel.add(pauseButton);
        panel.add(stopButton);
        return panel;
    }

    private JPanel createRechargePanel() {
        JPanel panel = new JPanel();

        rechargeButton.addActionListener(e -> sendRechargeAction());  // add action listeners

        panel.add(rechargeButton);  // add to panel
        return panel;
    }

    /*
     * Update
     */

    /**
     * Update screen with newest data
     */
    private void updateOrFail() {
        try {
            BmsData data = bms.getNewestData();  // get newest data

            if (data.isStatusType()) {  // if it's a log, update log frame
                updateLogFrameOrFail(data);
            } else if (data.isValueType()) {  // if it's a value update data and chart frames
                updateDataFrameOrFail(data);
                updateChartFrameOrFail();
            }

            Thread.sleep(msGuiIntervalUpdate);  // wait until next update
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Fetch newest data from bms and updates screen
     *
     * @param data new data with which update log frame
     */
    private void updateDataFrameOrFail(BmsData data) {
        try {
            if (data.isValueType()) {
                dataFrame.updateCellValue(new BmsValue(data));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Fetch newest average tmp/volt value and updateOrFail series chart
     */
    private void updateChartFrameOrFail() {
        try {
            chartFrame.updateSeriesOrFail(0, bms.batteryPack.getSumOfAllVoltages());  // update voltage
        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    /**
     * Update log frame
     *
     * @param data new data with which update log frame
     */
    private void updateLogFrameOrFail(BmsData data) {
        try {
            logFrame.update(new BmsLog(data));
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /*
     * Menu bar
     */

    /**
     * Creates a menu bar for frame
     *
     * @return menu bar for frame
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());  // file
        menuBar.add(createShowFramesMenu());  // windows toggle status
        menuBar.add(createEditMenu());  // edit update intervals
        menuBar.add(createCellBalanceMenu());  // balance cells
        menuBar.add(createHelpMenu());  // help/ about

        return menuBar;
    }

    /**
     * Creates file menu with save/exit options
     *
     * @return file menu
     */
    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");  // file menu

        JMenuItem item = new JMenuItem("Exit");
        item.addActionListener(e -> System.exit(0));
        menu.add(item);

        return menu;
    }

    /**
     * Menu to toggle view status of each frame
     *
     * @return frame show menu
     */
    private JMenu createShowFramesMenu() {
        JMenu menu = new JMenu("Windows");  // file menu

        JMenuItem item = new JMenu("Data");
        JMenuItem subMenuItem = new JMenuItem("Toggle view status");
        subMenuItem.addActionListener(e -> dataFrame.setVisible(!dataFrame.isVisible()));
        item.add(subMenuItem);
        menu.add(item);

        item = new JMenu("Chart");
        subMenuItem = new JMenuItem("Toggle view status");
        subMenuItem.addActionListener(e -> chartFrame.setVisible(!chartFrame.isVisible()));
        item.add(subMenuItem);
        menu.add(item);

        item = new JMenu("Log");
        subMenuItem = new JMenuItem("Toggle view status");
        subMenuItem.addActionListener(e -> logFrame.setVisible(!logFrame.isVisible()));
        item.add(subMenuItem);
        menu.add(item);

        return menu;
    }

    /**
     * Creates new edit menu
     *
     * @return edit menu
     */
    private JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");  // file menu

        JMenuItem item = new JMenuItem("GUI update interval");
        item.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog("Milliseconds between two consecutive GUI updates", msGuiIntervalUpdate);
            msGuiIntervalUpdate = Integer.parseInt(userInput);  // update
        });
        menu.add(item);

        item = new JMenuItem("Bms update interval");
        item.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog("Milliseconds between two consecutive bms updates", bms.msSerialIntervalUpdate);
            bms.msSerialIntervalUpdate = Integer.parseInt(userInput);  // update
        });
        menu.add(item);

        item = new JMenuItem("Log update interval");
        item.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog("Milliseconds between two consecutive log data updates", bms.msLogIntervalUpdate);
            bms.msLogIntervalUpdate = Integer.parseInt(userInput);  // update
        });
        menu.add(item);

        item = new JMenuItem("Values alert interval");
        item.addActionListener(e -> showValueAlertIntervalEditDialog());
        menu.add(item);

        return menu;
    }

    /**
     * Creates cell balance menu with balance cell methods
     *
     * @return balance cells menu
     */
    private JMenu createCellBalanceMenu() {
        JMenu menu = new JMenu("Cell balance");  // file menu

        for (int segment = 0; segment < bms.batteryPack.getNumberOfCellsPerSegment().length; segment++) {
            JMenuItem item = new JMenu("Segment " + Integer.toString(segment + 1));
            menu.add(item);  // add segment menu

            for (int cell = 0; cell < bms.batteryPack.getNumberOfCellsPerSegment()[segment]; cell++) {
                JMenuItem subMenuItem = new JMenuItem("Cell " + Integer.toString(cell + 1));
                final int numberOfCell = cell;
                final int numberOfSegment = segment;
                subMenuItem.addActionListener(e -> bms.balanceCellOrFail(numberOfCell, numberOfSegment));  // balance cell in segment
                item.add(subMenuItem);  // add cell menu
            }
        }

        return menu;
    }

    /**
     * Creates new help menu with help/about options
     *
     * @return help menu
     */
    private JMenu createHelpMenu() {
        JMenu menu = new JMenu("Help");  // help menu

        JMenuItem item = new JMenuItem("Help");  // help menu -> help
        item.addActionListener(e -> showHelpDialogOrFail());
        menu.add(item);

        item = new JMenuItem("About");  // help menu -> about
        item.addActionListener(e -> showAboutDialogOrFail());
        menu.add(item);

        return menu;
    }

    /*
     * Dialogs
     */

    /**
     * Show a dialog to edit values alert interval
     */
    private void showValueAlertIntervalEditDialog() {
        MinMaxValuePanel minMaxTemperaturePanel = new MinMaxValuePanel(
                "Temperature bounds (K)",  // title
                dataFrame.getTemperatureBounds()[0],  // min value
                dataFrame.getTemperatureBounds()[1]  // max value
        );

        MinMaxValuePanel minMaxVoltagePanel = new MinMaxValuePanel(
                "Voltage bounds (mV)",  // title
                dataFrame.getVoltageBounds()[0],  // min value
                dataFrame.getVoltageBounds()[1]  // max value
        );

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));  // add components vertically with horizontal gap, vertical gap
        panel.add(minMaxTemperaturePanel);  // add sub-panels
        panel.add(minMaxVoltagePanel);

        int userInput = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Edit values bounds",  // question for the user
                JOptionPane.CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (!(userInput == JOptionPane.CANCEL_OPTION || userInput < 0)) {  // user has not clicked "Cancel" button nor exited panel
            dataFrame.setTemperatureBounds(
                    minMaxTemperaturePanel.getMin(),
                    minMaxTemperaturePanel.getMax()
            );  // update values

            dataFrame.setVoltageBounds(
                    minMaxVoltagePanel.getMin(),
                    minMaxVoltagePanel.getMax()
            );  // update values
        }
    }

    /**
     * Show a dialog about the app
     */
    private void showAboutDialogOrFail() {
        String content = "";
        try {
            InputStream inputStream = BmsGui.class.getResourceAsStream("/res/strings/about.html");
            content = readAllFromStream(new BufferedReader(new InputStreamReader(inputStream)));  // read content of dialog from html
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        String title = "About this app";
        new AboutDialog(this, content, title).setVisible(true);
    }

    /**
     * Show a help dialog about the app
     */
    private void showHelpDialogOrFail() {
        String content = "";
        InputStream inputStream = BmsGui.class.getResourceAsStream("/res/strings/help.html");
        try {
            content = readAllFromStream(new BufferedReader(new InputStreamReader(inputStream)));  // read content of dialog from html
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        String title = "Help";
        new AboutDialog(this, content, title).setVisible(true);
    }

    /**
     * Sends to Arduino recharge action
     */
    private void sendRechargeAction() {
        bms.sendRechargeAction();
    }

    /*
     * Recharge
     */

    /**
     * Panel to get/set min, max values
     */
    private class MinMaxValuePanel extends JPanel {
        JTextArea minInput;  // input labels
        JTextArea maxInput;
        private double min;  // input values
        private double max;

        /**
         * New MinMaxValuePanel
         *
         * @param title title of panel
         * @param min   initial min value
         * @param max   initial max value
         */
        MinMaxValuePanel(String title, double min, double max) {
            super();
            this.min = min;  // initial values
            this.max = max;

            setup();
            setLayout(new GridLayout(2, 2));  // rows, columns
            JPanelsUtils.addTitleEmptyBorderOnPanel(this, title);  // add title
        }

        /**
         * Get min value
         *
         * @return min value
         */
        public double getMin() {
            min = Double.parseDouble(minInput.getText());  // update value
            return min;
        }

        /**
         * Get max value
         *
         * @return max value
         */
        public double getMax() {
            max = Double.parseDouble(maxInput.getText());  // update value
            return max;
        }

        private void setup() {
            add(new JLabel("Min value"));
            minInput = new JTextArea(Double.toString(min));
            add(minInput);

            add(new JLabel("Max value"));
            maxInput = new JTextArea(Double.toString(max));
            add(maxInput);
        }
    }
}
