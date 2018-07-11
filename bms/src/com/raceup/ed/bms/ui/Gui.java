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
import com.raceup.ed.bms.models.battery.Pack;
import com.raceup.ed.bms.ui.panel.data.DataPanel;
import com.raceup.ed.bms.ui.panel.data.InfoPanel;
import com.raceup.ed.bms.ui.panel.stream.ModePanel;
import com.raceup.ed.bms.utils.gui.AboutDialog;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static com.raceup.ed.bms.utils.Streams.readAllFromStream;

/**
 * Font-end (ui) of BMS
 * - ~spreadsheet with race data values and dialog to get more info (i.e
 * DataPanel)
 * - chart with total tension of battery (i.e ChartFrame)
 * - text area with errors (i.e ErrorsAreaFrame)
 */
public class Gui extends ApplicationFrame implements Runnable {
    private static final String APP_NAME_SETTINGS = "com.apple.mrj" +
            ".application.apple.menu.about.name";
    private static final String THIS_PACKAGE = "com.raceup.ed.bms.ui.Gui";
    private static final String ICON_PATH = "/res/images/icon.png";
    private static final Image appIcon = Toolkit.getDefaultToolkit().getImage(
            THIS_PACKAGE.getClass().getResource(ICON_PATH)
    );
    private static final String TAG = "Gui";
    private ModePanel modePanel;
    private DataPanel dataPanel;  // ui panels
    private InfoPanel infoPanel = new InfoPanel();
    private Bms bms;
    private Thread bmsThread;

    /**
     * Prepare and run ui
     */
    public Gui(Bms bms) {
        super("YOLO Bms");  // set title

        this.bms = bms;
        setup();
        setupLayout();  // setup frame manager
    }

    /**
     * Start frontend GUI and backend engines
     */
    public void open() {
        startMonitorBms();
        dataPanel.setVisible(true);

        pack();
        setLocation(0, 0);  // top left corner
        setResizable(false);
        setVisible(true);
    }

    public void close() {
        bms.close();
    }

    private void startMonitorBms() {
        bms.setup();  // start backend
        bmsThread = new Thread(bms);
        bmsThread.start();
    }

    /**
     * Setup ui and backend
     */
    private void setup() {
        dataPanel = new DataPanel(bms.getBatteryPack());
        modePanel = new ModePanel(bms);

        setIconImage(appIcon);  // set icon
        System.setProperty(APP_NAME_SETTINGS, "YOLO Bms Desktop");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // destroy
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

        add(modePanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(infoPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(dataPanel);
        setJMenuBar(createMenuBar());  // set menu-bar
    }

    /**
     * Creates a menu bar for frame
     *
     * @return menu bar for frame
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());  // file
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

    @Override
    public void run() {
        while (true) {
            try {
                loop();
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }

    public void loop() {
        Pack battery = bms.getBatteryPack();
        HashMap<String, Double> info = battery.getInfoOverall();
        updateInfoPanel(info);

        for (int i = 0; i < battery.getNumberOfBms(); i++) {
            updateBmsDevice(battery.getCurrentValues(i), i);
        }

        modePanel.updateStatus(bms.getCurrentStatus());
    }

    private void updateInfoPanel(HashMap<String, Double> info) {
        if (info != null) {
            infoPanel.setMinVoltage(info.get("min"));
            infoPanel.setMaxVoltage(info.get("max"));
            infoPanel.setTotVoltage(info.get("tot") / 1000.0);  // mv -> V
            infoPanel.setMaxTemperature(info.get("tMax"));
        }
    }

    private void updateBmsDevice(HashMap<String, Double> info, int i) {
        if (info != null) {
            dataPanel.setMinVoltage(i, info.get("min"));
            dataPanel.setMaxVoltage(i, info.get("max"));
            dataPanel.setAvgVoltage(i, info.get("avg"));
            dataPanel.setTemperature1(i, info.get("t1"));
            dataPanel.setTemperature2(i, info.get("t2"));
        }
    }
}
