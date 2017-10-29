package com.raceup.ed.bms.gui.frame.data;

import javax.swing.*;
import java.awt.*;

public class BmsDevice extends Cell {
    // TODO 2 temperatures -> 2 cells layout
    // +------------------------+
    // +-        voltage        -
    // +------------------------+
    // +-   temp  -++-   temp   -
    // +------------------------+
    // TODO    instead of
    // +-----------++-----------+
    // +- voltage -++-  voltage -
    // +-----------++-----------+
    // +-   temp  -++-   temp  -+
    // +-----------++-----------+

    private final JLabel temperature1Label = new JLabel("0000.00");
    private final JLabel temperature2Label = new JLabel("0000.00");
    private double temperature1;
    private double temperature2;

    BmsDevice() {
        setLayout(new GridLayout(2, 2));  // rows, columns
        setup();
    }

    public void setTemperature1(double value) {
        temperature1 = value;  // update value
        update(temperature1Label, temperatureBounds, value);
    }

    public void setTemperature2(double value) {
        temperature2 = value;  // update value
        update(temperature2Label, temperatureBounds, value);
    }

    private void setup() {
        temperature1Label.setHorizontalAlignment(SwingConstants.RIGHT);
        temperature2Label.setHorizontalAlignment(SwingConstants.RIGHT);
        voltageLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(voltageLabel);
        add(temperature1Label);
        add(temperature2Label);

        setVisible(true);  // open
    }
}
