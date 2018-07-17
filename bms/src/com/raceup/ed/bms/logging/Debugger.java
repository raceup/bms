package com.raceup.ed.bms.logging;

public class Debugger extends Logger {
    private final boolean debug;
    public String TAG = "DEBUGGER";

    public Debugger(boolean debug) {
        this.debug = debug;
    }

    @Override
    protected void logError(String message) {
        if (debug) {
            log(System.err, message);
        }
    }
}
