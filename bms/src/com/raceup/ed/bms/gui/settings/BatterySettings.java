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

package com.raceup.ed.bms.gui.settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel to set battery pack options
 */
class BatterySettings extends JPanel {
    private static final int minNumberOfBmsPerSegment = 1;  // min, max of
    // number of cells in segment
    private static final int maxNumberOfBmsPerSegment = 100;
    private static final int statusBarHeight = 60;  // height of status-bar
    private static final int borderSpace = 10;
    // in pixel
    private final ArrayList<Integer> numberOfBmsPerSegment = new
            ArrayList<>();  // default number of segments

    BatterySettings() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));  // add items
        // vertically
        loadDefaultBatteryPack();
        setupGui();
    }

    /**
     * Get list of number of cells for each segment
     *
     * @return number of cells per segment
     */
    public int[] getNumberOfBmsPerSegment() {
        return numberOfBmsPerSegment.stream().mapToInt(i -> i).toArray();
        // ArrayList > Integer[] > int[]
    }

    /**
     * Loads default battery pack
     */
    private void loadDefaultBatteryPack() {
        numberOfBmsPerSegment.clear();  // start from scratch
        numberOfBmsPerSegment.add(3);  // first segment...
        numberOfBmsPerSegment.add(3);
        numberOfBmsPerSegment.add(3);
        numberOfBmsPerSegment.add(3);
        numberOfBmsPerSegment.add(3);
        numberOfBmsPerSegment.add(3);
        numberOfBmsPerSegment.add(3);
        numberOfBmsPerSegment.add(3);  // ...and last segment have 34 cells
    }

    /**
     * Setup gui with layouts anf buttons
     */
    private void setupGui() {
        removeAll();  // clear components
        revalidate();
        repaint();

        addCellNumberSpinners();  // spinners
        addStatusBar();  // status-bar
    }

    /**
     * Setup spinners of cell numbers of segments
     */
    private void addCellNumberSpinners() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        for (int i = 0; i < numberOfBmsPerSegment.size(); i++) {
            JPanel segmentPanel = new JPanel();  // where to put segment

            JLabel segmentName = new JLabel("Segment " + Integer.toString(i
                    + 1) + " | bms:");  // label with name of segment
            JSpinner cellNumberSpinner = new JSpinner(
                    new SpinnerNumberModel(
                            minNumberOfBmsPerSegment,  // initial value
                            minNumberOfBmsPerSegment,  // min
                            maxNumberOfBmsPerSegment, // max
                            1  //step
                    )
            );  // possible values of cells
            cellNumberSpinner.setValue(numberOfBmsPerSegment.get(i));  //
            // set initial value

            final int segmentNumber = i;
            cellNumberSpinner.addChangeListener(  // edit segment number of
                    // cells on edit
                    e -> numberOfBmsPerSegment.set(segmentNumber,
                            (Integer) cellNumberSpinner.getValue())
            );

            segmentPanel.add(segmentName, BorderLayout.LINE_START);
            segmentPanel.add(cellNumberSpinner, BorderLayout.LINE_END);
            mainPanel.add(segmentPanel);  // add segment
        }

        add(mainPanel);  // add scroll panel to main panel
    }

    /**
     * Create statusbar with Add/Remove buttons to edit battery pack
     */
    private void addStatusBar() {
        JPanel statusBar = new JPanel();  // where status bar will be
        statusBar.setPreferredSize(new Dimension(getWidth(),
                statusBarHeight));  // buttons and statusbar

        JButton addNewSegmentButton = new JButton("Add one");
        addNewSegmentButton.addActionListener(e -> addNewSegment());
        JButton removeLastSegmentButton = new JButton("Remove one");  //
        // exit button
        removeLastSegmentButton.addActionListener(e -> removeLastSegment());

        statusBar.add(addNewSegmentButton);  // add buttons and statusbar to
        // lower panel
        statusBar.add(removeLastSegmentButton);

        statusBar.setVisible(true);
        add(statusBar);
        setBorder(BorderFactory.createEmptyBorder(
                borderSpace,
                borderSpace,
                borderSpace,
                borderSpace
        ));
    }

    /**
     * Add new segment
     */
    private void addNewSegment() {
        numberOfBmsPerSegment.add(minNumberOfBmsPerSegment);  // add new
        // segment with min number of cells
        setupGui();  // update layout
    }

    /**
     * Remove last segment in list
     */
    private void removeLastSegment() {
        numberOfBmsPerSegment.remove(numberOfBmsPerSegment.size() - 1);
        // remove last
        setupGui();  // update layout
    }
}
