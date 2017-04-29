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

package com.raceup.ed.bms.stream.bms.data;

/**
 * Well parsed values coming from arduino
 */
public class BmsValue extends BmsData {
    /**
     * Empty constructor as default
     */
    public BmsValue() {
        super();
    }

    /**
     * Cast from generic data to value
     *
     * @param data generic data type
     */
    public BmsValue(BmsData data) {
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
    public BmsValue(String type, String cell, String segment, String value) {
        super(type, cell, segment, value);
    }

    /**
     * Check if data is a temperature value
     *
     * @return True iff data is a temperature value
     */
    public boolean isTemperature() {
        return getType().name().equals(Data.Temperature.name());
    }

    /**
     * Check if data is a voltage value
     *
     * @return True iff data is a voltage value
     */
    public boolean isVoltage() {
        return getType().name().equals(Data.Voltage.name());
    }

    /**
     * Getter for data value
     *
     * @return parsed data value
     */
    public double getValue() {
        return Double.parseDouble(value);
    }
}
