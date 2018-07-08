package com.raceup.ed.bms.ui.panel.data;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class NumAlerter extends JPanel {
    public static final DecimalFormat NUM_FORMAT = new DecimalFormat
            ("    ");  // decimal places
    final Color VALUE_TOO_HIGH_COLOR = Color.RED;
    final Color VALUE_NORMAL_COLOR = Color.GREEN;
    final Color VALUE_TOO_LOW_COLOR = Color.CYAN;
    private final JLabel text;
    private final double[] bounds;

    public NumAlerter(String label, String text, double[] bounds, int layout) {
        super();

        this.text = new JLabel(text);
        this.bounds = bounds;
        setup(label, layout);
    }

    private void setup(String label, int layout) {
        setLayout(new BoxLayout(this, layout));

        add(new JLabel(label));
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(text);

        setOpaque(true);
    }

    /**
     * Update widget with new value
     *
     * @param value  new value
     */
    public void update(double value) {
        try {
            text.setText(NUM_FORMAT.format(value));
            updateBackground(value, bounds);
        } catch (Exception e) {
        }
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
