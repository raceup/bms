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


package com.raceup.ed.bms.ui;

import com.raceup.ed.bms.control.Bms;
import com.raceup.ed.bms.models.stream.bms.BmsData;
import com.raceup.ed.bms.models.stream.bms.BmsLog;
import com.raceup.ed.bms.models.stream.bms.BmsValue;
import com.raceup.ed.bms.ui.frame.chart.ChartPanel;
import com.raceup.ed.bms.ui.frame.data.DataFrame;
import com.raceup.ed.bms.ui.frame.log.LogFrame;
import com.raceup.ed.bms.utils.gui.AboutDialog;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import static com.raceup.ed.bms.utils.Streams.readAllFromStream;

/**
 * Font-end (ui) of BMS
 * - ~spreadsheet with race data values and dialog to get more info (i.e
 * DataFrame)
 * - chart with total tension of battery (i.e ChartFrame)
 * - text area with errors (i.e ErrorsAreaFrame)
 */
public class Gui extends ApplicationFrame {
    private static final String APP_NAME_SETTINGS = "com.apple.mrj" +
            ".application.apple.menu.about.name";
    private static final String THIS_PACKAGE = "com.raceup.ed.bms.ui.Gui";
    private static final String ICON_PATH = "/res/images/icon.png";
    private static final Image appIcon = Toolkit.getDefaultToolkit().getImage(
            THIS_PACKAGE.getClass().getResource(ICON_PATH)
    );
    private static final String TAG = "Gui";
    private final JButton balanceButton = new JButton("Balance cells");
    private DataFrame dataPanel;  // ui frames
    private ChartPanel chartPanel;
    private LogFrame logPanel;  // frame used for logging
    private Bms bms;

    /**
     * Prepare and run ui
     */
    public Gui(Bms bms) {
        super("YOLO Bms");  // set title

        this.bms = bms;
        setIconImage(appIcon);  // set icon
        System.setProperty(APP_NAME_SETTINGS, "YOLO Bms Desktop");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // destroy

        setup();
    }

    /**
     * Start frontend GUI and backend engines
     */
    public void open() {
        dataPanel.setVisible(true);
        // chartPanel.setVisible(true);
        // logPanel.setVisible(true);

        pack();
        setLocation(0, 0);  // top left corner
        setVisible(true);
    }

    /**
     * Start GUI and bms
     */
    public void start() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("[" + System.currentTimeMillis() + "]: " +
                        "new run");
                try {
                    BmsData data = null;  // todo get newest data
                    if (data != null) {
                        if (data.isStatusType()) {  // if it's a log, update log frame
                            updateLogFrameOrFail(data);
                        } else if (data.isValueType()) {  // if it's a value update
                            // data and chart frames
                            updateDataFrameOrFail(data);
                        }
                    }
                } catch (Exception e) {
                    System.err.println(TAG + " has encountered some errors while " +
                            "updateOrFail()");
                    e.printStackTrace();
                    System.err.println();
                }
            }
        }, 0, 150);
    }

    public void close() {

    }

    /*
     * Setup main frame
     */

    /**
     * Setup ui and backend
     */
    private void setup() {
        /*int[] numberOfBmsPerSegment = bms.batteryPack
                .getNumberOfBmsPerSegment();
        chartPanel = new ChartPanel(new String[]{"Voltage (mV)"});
        chartPanel.setMaximumSize(
                new Dimension(1000, 800)
        );  // aliasing blurring*/
        dataPanel = new DataFrame(8, 3);
        logPanel = new LogFrame();
        setupLayout();  // setup frame manager
    }

    /**
     * Setup ui of main frame (manager)
     */
    private void setupLayout() {
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS)
        );  // add components horizontally
        getRootPane().setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );  // border

        // data and chart
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setAlignmentX(JScrollPane.RIGHT_ALIGNMENT);
        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );  // border

        // panel.add(chartPanel);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(createStartStopPanel());

        add(panel);
        add(dataPanel);
        setJMenuBar(createMenuBar());  // set menu-bar
    }

    /**
     * Create panel that starts and stops monitor the bms
     *
     * @return panel with buttons to monitor bms
     */
    private JPanel createStartStopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(balanceButton);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));

        return panel;
    }

    /**
     * Fetch newest data from bms and updates screen
     *
     * @param data new data with which update log frame
     */
    private void updateDataFrameOrFail(BmsData data) {
        try {
            if (data.isValueType()) {
                dataPanel.update(new BmsValue(data));
            }
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
            logPanel.update(new BmsLog(data));
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
        subMenuItem.addActionListener(e -> dataPanel.setVisible(!dataPanel
                .isVisible()));
        item.add(subMenuItem);
        menu.add(item);

        item = new JMenu("Chart");
        subMenuItem = new JMenuItem("Toggle view status");
        subMenuItem.addActionListener(e -> chartPanel.setVisible(
                !chartPanel.isVisible()));
        item.add(subMenuItem);
        menu.add(item);

        item = new JMenu("Log");
        subMenuItem = new JMenuItem("Toggle view status");
        subMenuItem.addActionListener(e -> logPanel.setVisible(!logPanel
                .isVisible()));
        item.add(subMenuItem);
        menu.add(item);

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

    /**
     * Show a dialog about the app
     */
    private void showAboutDialogOrFail() {
        String content = "";
        try {
            InputStream inputStream = Gui.class.getResourceAsStream
                    ("/res/strings/about.html");
            content = readAllFromStream(new BufferedReader(new
                    InputStreamReader(inputStream)));  // read content of
            // dialog from html
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
        InputStream inputStream = Gui.class.getResourceAsStream
                ("/res/strings/help.html");
        try {
            content = readAllFromStream(new BufferedReader(new
                    InputStreamReader(inputStream)));  // read content of
            // dialog from html
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        String title = "Help";
        new AboutDialog(this, content, title).setVisible(true);
    }
}
