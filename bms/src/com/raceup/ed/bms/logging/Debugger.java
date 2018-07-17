package com.raceup.ed.bms.logging;

import java.io.PrintStream;

public class Debugger extends Logger {
    private final boolean debug;

    public Debugger(boolean debug) {
        this("DEBUGGER", debug);
    }

    public Debugger(String tag, boolean debug) {
        super(tag);
        this.debug = debug;
    }

    @Override
    protected void logError(String message) {
        if (debug) {
            super.logError(message);
        }
    }

    @Override
    protected void log(PrintStream out, String message) {
        if (debug) {
            super.log(out, message);
        }
    }
}
