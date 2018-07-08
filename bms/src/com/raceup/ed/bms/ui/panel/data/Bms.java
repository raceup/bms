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

import static com.raceup.ed.bms.models.battery.BmsDevice.TEMPERATURE_BOUNDS;
import static com.raceup.ed.bms.models.battery.BmsDevice.VOLTAGE_BOUNDS;

/**
 * GUI frame that contains battery cell info
 */
public class Bms extends JPanel {
    private final NumAlerter voltageMinPanel = new NumAlerter(
            "Min volt (mV)", "DNF", VOLTAGE_BOUNDS, BoxLayout.PAGE_AXIS
    );
    private final NumAlerter voltageAvgPanel = new NumAlerter(
            "Avg volt (mV)", "DNF", VOLTAGE_BOUNDS, BoxLayout.PAGE_AXIS
    );
    private final NumAlerter voltageMaxPanel = new NumAlerter(
            "Max volt (mV)", "DNF", VOLTAGE_BOUNDS, BoxLayout.PAGE_AXIS
    );
    private final NumAlerter temperature1Panel = new NumAlerter(
            "Temp 1 (C°)", "DNF", TEMPERATURE_BOUNDS, BoxLayout.LINE_AXIS
    );
    private final NumAlerter temperature2Panel = new NumAlerter(
            "Temp 2 (C°)", "DNF", TEMPERATURE_BOUNDS, BoxLayout.LINE_AXIS
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
     * Show new value in label and bar
     *
     * @param value new voltage to set
     */
    public void setMinVoltage(double value) {
        voltageMinPanel.update(value);
    }

    public void setAvgVoltage(double value) {
        voltageAvgPanel.update(value);
    }

    public void setMaxVoltage(double value) {
        voltageMaxPanel.update(value);
    }

    public void setTemperature1(double value) {
        temperature1Panel.update(value);
    }

    public void setTemperature2(double value) {
        temperature2Panel.update(value);
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

        upRight.add(temperature1Panel);
        upRight.add(temperature2Panel);

        up.add(button);
        up.add(Box.createRigidArea(new Dimension(120, 0)));
        up.add(upRight);

        JPanel down = new JPanel();
        down.setLayout(new BoxLayout(down, BoxLayout.LINE_AXIS));

        down.add(voltageMinPanel);
        down.add(Box.createRigidArea(new Dimension(20, 0)));
        down.add(voltageAvgPanel);
        down.add(Box.createRigidArea(new Dimension(20, 0)));
        down.add(voltageMaxPanel);

        add(up);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(down);
    }
}
