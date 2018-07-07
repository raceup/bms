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

package com.raceup.ed.bms.ui.panel.data;

import javax.swing.*;
import java.awt.*;

/**
 * GUI frame that contains battery cell info
 */
public class Bms extends JPanel {
    public static final double[] VOLTAGE_BOUNDS = new double[]{3000.0,
            4200.0};
    public static final double[] TEMPERATURE_BOUNDS = new double[]{0.0,
            20.0};
    private final NumAlerter voltageMinLabel = new NumAlerter(
            "DNF ", VOLTAGE_BOUNDS
    );
    private final NumAlerter voltageAvgLabel = new NumAlerter(
            "DNF ", VOLTAGE_BOUNDS
    );
    private final NumAlerter voltageMaxLabel = new NumAlerter(
            "DNF", VOLTAGE_BOUNDS
    );
    private final NumAlerter temperature1Label = new NumAlerter(
            "DNF", TEMPERATURE_BOUNDS
    );
    private final NumAlerter temperature2Label = new NumAlerter(
            "DNF", TEMPERATURE_BOUNDS
    );
    private JButton button;

    Bms(int indexInBms) {
        button = new JButton(Integer.toString(indexInBms + 1));
        setup();
    }

    /*
     * Getters & setters
     */

    /**
     * Retrieve voltage value
     *
     * @return getter method for voltage
     */
    public double getVoltage() {
        return Double.parseDouble(voltageAvgLabel.getText());
    }

    /**
     * Show new value in label and bar
     *
     * @param value new voltage to set
     */
    public void setMinVoltage(double value) {
        voltageMinLabel.update(value);
    }

    public void setAvgVoltage(double value) {
        voltageAvgLabel.update(value);
    }

    public void setMaxVoltage(double value) {
        voltageMaxLabel.update(value);
    }

    public void setTemperature1(double value) {
        temperature1Label.update(value);
    }

    public void setTemperature2(double value) {
        temperature2Label.update(value);
    }

    /*
     * Setup and update
     */

    /**
     * Add to panel all widgets
     */
    private void setup() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel up = new JPanel();
        up.setLayout(new BoxLayout(up, BoxLayout.LINE_AXIS));

        JPanel upRight = new JPanel();
        upRight.setLayout(new BoxLayout(upRight, BoxLayout.PAGE_AXIS));

        JPanel temperature1Panel = new JPanel();
        temperature1Panel.setLayout(new BoxLayout(temperature1Panel,
                BoxLayout.LINE_AXIS));
        temperature1Panel.add(new JLabel("Temp 1 (C°)"));
        temperature1Panel.add(Box.createRigidArea(new Dimension(20, 0)));
        temperature1Panel.add(temperature1Label);

        JPanel temperature2Panel = new JPanel();
        temperature2Panel.setLayout(new BoxLayout(temperature2Panel,
                BoxLayout.LINE_AXIS));
        temperature2Panel.add(new JLabel("Temp 2 (C°)"));
        temperature2Panel.add(Box.createRigidArea(new Dimension(20, 0)));
        temperature2Panel.add(temperature2Label);

        upRight.add(temperature1Panel);
        upRight.add(temperature2Panel);

        up.add(button);
        up.add(Box.createRigidArea(new Dimension(120, 0)));
        up.add(upRight);

        JPanel down = new JPanel();
        down.setLayout(new BoxLayout(down, BoxLayout.LINE_AXIS));

        JPanel minPanel = new JPanel();
        minPanel.setLayout(new BoxLayout(minPanel, BoxLayout.PAGE_AXIS));
        minPanel.add(new JLabel("Min volt (mV)"));
        minPanel.add(voltageMinLabel);

        JPanel avgPanel = new JPanel();
        avgPanel.setLayout(new BoxLayout(avgPanel, BoxLayout.PAGE_AXIS));
        avgPanel.add(new JLabel("Avg volt (mV)"));
        avgPanel.add(voltageAvgLabel);

        JPanel maxPanel = new JPanel();
        maxPanel.setLayout(new BoxLayout(maxPanel, BoxLayout.PAGE_AXIS));
        maxPanel.add(new JLabel("Max volt (mV)"));
        maxPanel.add(voltageMaxLabel);

        down.add(minPanel);
        down.add(Box.createRigidArea(new Dimension(20, 0)));
        down.add(avgPanel);
        down.add(Box.createRigidArea(new Dimension(20, 0)));
        down.add(maxPanel);

        add(up);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(down);

        setVisible(true);  // open
    }
}
