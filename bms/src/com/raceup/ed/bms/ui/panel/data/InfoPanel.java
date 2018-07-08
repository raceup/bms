package com.raceup.ed.bms.ui.panel.data;

import javax.swing.*;
import java.awt.*;

import static com.raceup.ed.bms.models.battery.BmsDevice.VOLTAGE_BOUNDS;

/**
 * Shows min, max, total voltage of pack and temperature
 */
public class InfoPanel extends JPanel {
    private final NumAlerter minVoltage = new NumAlerter(
            "Min volt (mV)", "DNF", VOLTAGE_BOUNDS, BoxLayout.PAGE_AXIS
    );
    private final NumAlerter maxVoltage = new NumAlerter(
            "Max volt (mV)", "DNF", VOLTAGE_BOUNDS, BoxLayout.PAGE_AXIS
    );
    private final NumAlerter totVoltage = new NumAlerter(
            "Tot volt (V)", "DNF", VOLTAGE_BOUNDS, BoxLayout.PAGE_AXIS
    );
    private final NumAlerter maxTemperature = new NumAlerter(
            "Max temp (C°)", "DNF", VOLTAGE_BOUNDS, BoxLayout.PAGE_AXIS
    );

    public InfoPanel() {
        super();
        setup();
    }

    private void setup() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        add(minVoltage);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(maxVoltage);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(totVoltage);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(maxTemperature);
        add(Box.createRigidArea(new Dimension(20, 0)));

        setVisible(true);
    }

    public void setMinVoltage(double value) {
        minVoltage.update(value);
    }

    public void setMaxVoltage(double value) {
        maxVoltage.update(value);
    }

    public void setTotVoltage(double value) {
        totVoltage.update(value);
    }

    public void setMaxTemperature(double value) {
        maxTemperature.update(value);
    }
}
