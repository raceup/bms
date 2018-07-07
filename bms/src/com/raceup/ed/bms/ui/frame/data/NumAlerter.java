package com.raceup.ed.bms.ui.frame.data;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class NumAlerter extends JLabel {
    private static final DecimalFormat NUM_FORMAT = new DecimalFormat
            ("0000.00");  // decimal places
    final Color VALUE_TOO_HIGH_COLOR = Color.RED;
    final Color VALUE_NORMAL_COLOR = Color.GREEN;
    final Color VALUE_TOO_LOW_COLOR = Color.CYAN;

    public NumAlerter(String text) {
        super(text);
    }

    /**
     * Update widget with new value
     *
     * @param bounds min, max of value allowed
     * @param value  new value
     */
    protected void update(double[] bounds, double value) {
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
