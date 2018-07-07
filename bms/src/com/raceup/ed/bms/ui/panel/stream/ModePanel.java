package com.raceup.ed.bms.ui.panel.stream;

import com.raceup.ed.bms.control.BmsOperatingMode;

import javax.swing.*;
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

        for (BmsOperatingMode.OperatingMode key : OPERATING_MODE.keySet()) {
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
    }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel("Select operating mode"));
        add(modeChooser);
    }
}
