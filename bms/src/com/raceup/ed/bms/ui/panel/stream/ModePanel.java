package com.raceup.ed.bms.ui.panel.stream;

import com.raceup.ed.bms.control.BmsOperatingMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;

import static com.raceup.ed.bms.control.Bms.OPERATING_MODE;

/**
 * Selects BMS operating mode
 */
public class ModePanel extends JPanel {
    private JComboBox<String> modeChooser;
    private HashMap<String, BmsOperatingMode.OperatingMode> modeCommandBms = new HashMap<>();

    public ModePanel() {
        super();

        setup();
        setupLayout();
    }

    private void setup() {
        modeChooser = new JComboBox<>();
        BmsOperatingMode.OperatingMode[] modes = new BmsOperatingMode
                .OperatingMode[]{
                BmsOperatingMode.OperatingMode.NORMAL,
                BmsOperatingMode.OperatingMode.BALANCE,
                BmsOperatingMode.OperatingMode.SLEEP,
                BmsOperatingMode.OperatingMode.DEBUG
        };

        for (BmsOperatingMode.OperatingMode key : modes) {
            BmsOperatingMode mode = OPERATING_MODE.get(key);
            String description = mode.getDescription();
            modeChooser.addItem(description);
            modeCommandBms.put(description, key);
        }

        modeChooser.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) itemEvent.getItem();
                System.out.println("SELECTED: " + item + " (" +
                        modeCommandBms.get(item) + ")");
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
    }
}
