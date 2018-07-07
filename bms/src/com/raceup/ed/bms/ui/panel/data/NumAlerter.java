package com.raceup.ed.bms.ui.panel.data;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class NumAlerter extends JLabel {
    public static final DecimalFormat NUM_FORMAT = new DecimalFormat
            ("    ");  // decimal places
    final Color VALUE_TOO_HIGH_COLOR = Color.RED;
    final Color VALUE_NORMAL_COLOR = Color.GREEN;
    final Color VALUE_TOO_LOW_COLOR = Color.CYAN;
    private final double[] bounds;

    public NumAlerter(String text, double[] bounds) {
        super(text);
        setOpaque(true);

        this.bounds = bounds;
    }

    /**
     * Update widget with new value
     *
     * @param value  new value
     */
    protected void update(double value) {
        setText(NUM_FORMAT.format(value));
        updateBackground(value, bounds);
    }

    /**
     * Changes background on given update series of value
     *
     * @param value  new value to update
     * @param bounds min, max of value
     */
    private void updateBackground(double value, double[] bounds) {
        if (value >= bounds[1]) {  // too high
            setBackground(VALUE_TOO_HIGH_COLOR);
        } else if (value <= bounds[0]) {  // too low
            setBackground(VALUE_TOO_LOW_COLOR);
        } else {  // normal
            setBackground(VALUE_NORMAL_COLOR);
        }
    }
}
