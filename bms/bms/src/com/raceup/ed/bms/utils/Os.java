/*
 *  Copyright 2016-2017 RaceUp ED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.raceup.ed.bms.utils;

import javax.swing.*;

/**
 * Utils method to deal with different OSs
 */
public class Os {
    private static final String nameOfOs = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return nameOfOs.contains("win");
    }

    public static boolean isMac() {
        return nameOfOs.contains("mac");
    }

    public static boolean isUnix() {
        return nameOfOs.contains("nix") || nameOfOs.contains("nux") || nameOfOs.contains("aix");
    }

    public static boolean isSolaris() {
        return nameOfOs.contains("sunos");
    }

    public static void setNativeLookAndFeelOrFail() {
        try {
            String nativeLookAndFeelPackage = "";

            if (isWindows()) {
                nativeLookAndFeelPackage = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            } else if (isMac()) {
                nativeLookAndFeelPackage = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (isUnix()) {
                nativeLookAndFeelPackage = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } else if (isSolaris()) {
                nativeLookAndFeelPackage = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }

            UIManager.setLookAndFeel(nativeLookAndFeelPackage);
        } catch (Exception e) {
            System.err.println(
                    "Native look and feel not supported in this Os.\n" +
                            "System.getProperty(\"os.name\") = " + System.getProperty("os.name") + "\n" +
                            "Exception = " + e.toString()
            );
        }
    }
}
