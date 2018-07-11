package com.raceup.ed.bms.ui.panel.stream;

import com.raceup.ed.bms.control.Bms;
import com.raceup.ed.bms.control.BmsOperatingMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

import static com.raceup.ed.bms.control.Bms.OPERATING_MODE;

/**
 * Selects BMS operating mode
 */
public class ModePanel extends JPanel {
    private static final BmsOperatingMode.OperatingMode[] OPERATING_MODES = new BmsOperatingMode
            .OperatingMode[]{
            BmsOperatingMode.OperatingMode.NORMAL,
            BmsOperatingMode.OperatingMode.BALANCE,
            BmsOperatingMode.OperatingMode.SLEEP,
            BmsOperatingMode.OperatingMode.DEBUG
    };
    private JLabel statusLabel = new JLabel("DNF");

    private JComboBox<String> modeChooser;

    public ModePanel(Bms bms) {
        super();

        setup(bms);
        setupLayout();
    }

    private void setup(Bms bms) {
        modeChooser = new JComboBox<>();
        for (BmsOperatingMode.OperatingMode key : OPERATING_MODES) {
            BmsOperatingMode mode = OPERATING_MODE.get(key);
            String description = mode.getDescription();
            modeChooser.addItem(description);
        }

        modeChooser.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) itemEvent.getItem();
                for (BmsOperatingMode.OperatingMode key : OPERATING_MODES) {
                    BmsOperatingMode mode = OPERATING_MODE.get(key);
                    String description = mode.getDescription();
                    if (description.equals(item)) {
                        bms.setMode(key);
                    }
                }
            }
        });
        modeChooser.setSelectedIndex(0);
    }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(new JLabel("Select operating mode:"));
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(modeChooser);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(new JLabel("Current status:"));
        add(statusLabel);
    }

    public void updateStatus(String status) {
        try {
            statusLabel.setText(status);
        } catch (Exception e) {
        }
    }
}
