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
 * Generic data coming from arduino
 */
public class BmsData {
    final String value;  // value of cell of segment
    private final Data type;  // type of data
    private final int cell;  // number of cell broadcasting value
    private final int segment;  // number of segment broadcasting value

    /**
     * Empty constructor as default
     */
    BmsData() {
        type = null;
        cell = -1;
        segment = -1;
        value = null;
    }

    /**
     * Create and set params of new data
     *
     * @param type    type of data
     * @param cell    number of cell broadcasting value
     * @param segment number of segment broadcasting value
     * @param value   value of cell of segment
     */
    public BmsData(String type, String cell, String segment, String value) {
        this.type = Data.valueOf(type);
        this.cell = Integer.parseInt(cell);
        this.segment = Integer.parseInt(segment);
        this.value = value;
    }

    /**
     * Check if current data is a bms log
     *
     * @return True iff data represents a log
     */
    public boolean isLog() {
        return type.isLog();
    }

    /**
     * Check if current data is a battery cell value
     *
     * @return True iff data is a value
     */
    public boolean isValue() {
        return type.isValue();
    }

    /**
     * Getter for type
     *
     * @return data type
     */
    public Data getType() {
        return type;
    }

    /**
     * Getter for cell
     *
     * @return data cell
     */
    public int getCell() {
        return cell;
    }

    /**
     * Getter for segment
     *
     * @return data segment
     */
    public int getSegment() {
        return segment;
    }
}
