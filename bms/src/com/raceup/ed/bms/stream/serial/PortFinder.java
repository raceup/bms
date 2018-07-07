package com.raceup.ed.bms.stream.serial;

import com.raceup.ed.bms.utils.Os;
import com.raceup.ed.bms.utils.OsType;
import jssc.SerialPortList;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class PortFinder {
    private static final Pattern OS_LINUX = Pattern.compile("" +
            "(ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm|ttyO)" +
            "[0-9]{1,3}");
    private static final Pattern OS_SUNOS = Pattern.compile("[0-9]*|[a-z]*");
    private static final Pattern OS_MAC_OS_X = Pattern.compile("tty.(serial|usbserial|usbmodem).*");
    private static final Pattern OS_WINDOWS = Pattern.compile("");
    private static final Comparator<String> StringComparator = (Comparator<String>)
            String::compareTo;

    /**
     * Get serial port names matching pattern
     */
    private String[] getAvailablePorts(Pattern pattern, Comparator<String>
            comparator) {
        String[] portNames = SerialPortList.getPortNames();
        if (portNames == null) {
            return new String[]{};
        }

        TreeSet<String> ports = new TreeSet<>(comparator);
        for (String portName : portNames) {
            if (pattern.matcher(portName).find()) {
                ports.add(portName);
            }
        }
        return ports.toArray(new String[ports.size()]);
    }

    public String[] getAvailablePortsWindows() {
        return getAvailablePorts(OS_WINDOWS, StringComparator);
    }

    public String[] getAvailablePortsMac() {
        return getAvailablePorts(OS_MAC_OS_X, StringComparator);
    }

    public String[] getAvailablePortsLinux() {
        return getAvailablePorts(OS_LINUX, StringComparator);
    }

    public String[] getAvailablePortsSunos() {
        return getAvailablePorts(OS_SUNOS, StringComparator);
    }

    public String[] getAvailablePorts(OsType os) {
        if (os == OsType.WINDOWS) {
            return getAvailablePortsWindows();
        } else if (os == OsType.LINUX) {
            return getAvailablePortsLinux();
        } else if (os == OsType.MAC_OSX) {
            return getAvailablePortsMac();
        } else if (os == OsType.SUNOS) {
            return getAvailablePortsSunos();
        }

        return new String[]{};
    }

    public String[] getAvailablePorts() {
        return getAvailablePorts(Os.getCurrentOs());
    }
}
