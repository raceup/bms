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

/**
 * Panel with file chooser to log file there
 */
public class LoggerSettings extends JPanel {
    private String pathChosen = System.getProperty("user.home");  // default
    // is the current working directory
    private JLabel pathChosenLabel = new JLabel(pathChosen);  // label with
    // current path choice

    /**
     * New logger settings with file chooser
     */
    LoggerSettings() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));  // add items
        // vertically

        setupGui();
    }

    /**
     * Get path user has chosen
     *
     * @return path to file user has chosen
     */
    public String getPathChosen() {
        return pathChosen;
    }

    /**
     * Adds a button to choose a folder
     */
    private void setupGui() {
        JButton browseFileButton = new JButton("Browse");
        browseFileButton.addActionListener(e -> chooseFile());
        add(browseFileButton);

        pathChosenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(pathChosenLabel);
    }

    /**
     * Open browse file to choose a folder
     */
    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Browse files");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            // user has decided
            pathChosen = chooser.getSelectedFile().toString();  // store
            // user decision
            pathChosenLabel.setText(pathChosen);
        }
    }
}
