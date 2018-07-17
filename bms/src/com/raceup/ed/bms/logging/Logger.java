package com.raceup.ed.bms.logging;

import java.io.PrintStream;

import static com.raceup.ed.bms.utils.Utils.convertTime;

public class Logger {
    public final String TAG;
    protected final PrintStream errors = System.err;
    protected final PrintStream output = System.out;

    public Logger() {
        this("LOGGER");
    }

    public Logger(String tag) {
        TAG = tag;
    }

    protected void logAction(String message) {
        log(output, message);
    }

    protected void logError(String message) {
        log(errors, message);
    }

    protected void log(PrintStream out, String message) {
        String timing = "[" + convertTime(System.currentTimeMillis()) + "]";
        String content = ": " + message;
        out.println(timing + " " + TAG + content);
    }
}
