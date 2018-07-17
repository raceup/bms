package com.raceup.ed.bms.logging;

import java.io.PrintStream;

import static com.raceup.ed.bms.utils.Utils.convertTime;

public class Logger {
    public String TAG = "LOGGER";

    protected void logAction(String message) {
        log(System.out, message);
    }

    protected void logError(String message) {
        log(System.err, message);
    }

    protected void log(PrintStream out, String message) {
        String timing = "[" + convertTime(System.currentTimeMillis()) + "]";
        String content = ": " + message;
        out.println(timing + " " + TAG + content);
    }
}
