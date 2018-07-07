package com.raceup.ed.bms.control;

public class BmsOperatingMode {
    private final String arduinoCommand;
    private final String description;

    public BmsOperatingMode(String arduinoCommand, String description) {
        this.arduinoCommand = arduinoCommand;
        this.description = description;
    }

    public String getArduinoCommand() {
        return arduinoCommand;
    }

    public String getDescription() {
        return description;
    }

    public enum OperatingMode {
        NORMAL,
        BALANCE,
        SLEEP,
        DEBUG
    }
}
