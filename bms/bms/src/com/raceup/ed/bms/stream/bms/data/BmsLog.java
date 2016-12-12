/*
 * Copyright 2016-2017 RaceUp Team ED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raceup.ed.bms.stream.bms.data;

/**
 * BMS logging system.
 * Can intercept any failure, alert or error derived from malfunction of BMS.
 * Informs anyone of its status.
 */
public class BmsLog extends BmsData {
    /**
     * Empty constructor as default
     */
    public BmsLog() {
        super();
    }

    /**
     * Cast from generic data to value
     *
     * @param data generic data type
     */
    public BmsLog(BmsData data) {
        super(data.getType().toString(), Integer.toString(data.getCell()), Integer.toString(data.getSegment()), data.value);
    }

    /**
     * Create and set params of new data
     *
     * @param type    type of data
     * @param cell    number of cell broadcasting value
     * @param segment number of segment broadcasting value
     * @param value   value of cell of segment
     */
    public BmsLog(String type, String cell, String segment, String value) {
        super(type, cell, segment, value);
    }

    /**
     * Get type of log
     *
     * @return type of log, one of [Status, Alert, Fault, Log]
     */
    public String getTypeOfLog() {
        return getType().name();
    }

    /**
     * Getter for data value
     *
     * @return parsed data value
     */
    public String getValue() {
        return value;
    }

    /**
     * Check if data is a status log
     *
     * @return True iff data is a status log
     */
    public boolean isStatus() {
        return getTypeOfLog().equals(Data.Status.name());
    }

    /**
     * Check if data is a alert log
     *
     * @return True iff data is a alert log
     */
    public boolean isAlert() {
        return getTypeOfLog().equals(Data.Alert.name());
    }

    /**
     * Check if data is a fault log
     *
     * @return True iff data is a fault log
     */
    public boolean isFault() {
        return getTypeOfLog().equals(Data.Fault.name());
    }

    /**
     * Check if data is a simple log
     *
     * @return True iff data is a simple log
     */
    public boolean isLog() {
        return getTypeOfLog().equals(Data.Log.name());
    }

    /**
     * String converting method
     *
     * @return object properties written in a nice way
     */
    @Override
    public String toString() {
        return getType().toString() + " : " + getValue();
    }
}
