/*
 *  Copyright 2016-2018 Race Up Electric Division
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

import org.json.JSONObject;

/**
 * Generic data coming from arduino
 */
public class BmsData {
    final String value;  // value of cell of segment
    private final String type;  // type of data
    private final int cell;  // number of cell broadcasting value
    private final int segment;  // number of segment broadcasting value
    private String jsonValue;

    /**
     * Create and set params of new data
     *
     * @param type    type of data
     * @param cell    number of cell broadcasting value
     * @param segment number of segment broadcasting value
     * @param value   value of cell of segment
     */
    public BmsData(String type, String cell, String segment, String value) {
        this.type = type;
        this.cell = Integer.parseInt(cell);
        this.segment = Integer.parseInt(segment);
        this.value = value;

        jsonValue = "" +
                "{\n" +
                "\t\"type\": \"" + this.type + "\",\n" +
                "\t\"cell\": \"" + Integer.toString(this.cell) + "\",\n" +
                "\t\"segment\": \"" + Integer.toString(this.segment) + "\"," +
                "\n" +
                "\t\"value\": \"" + this.value + "\"\n" +
                "}";
    }

    /**
     * Parse raw data
     *
     * @param root json data
     */
    public BmsData(JSONObject root) {
        this(
                root.getString("type"),
                root.getString("cell"),
                root.getString("segment"),
                root.getString("value")
        );
    }

    /**
     * Check if current data is a bms log
     *
     * @return True iff data represents a log
     */
    public boolean isStatusType() {
        return type.equals("Status") ||
                type.equals("Alert") ||
                type.equals("Fault") ||
                type.equals("Log");
    }

    /**
     * Check if current data is a battery cell value
     *
     * @return True iff data is a value
     */
    public boolean isValueType() {
        return (type.equals("voltage") ||
                type.equals("temperature"));
    }

    /**
     * Getter for type
     *
     * @return data type
     */
    String getType() {
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

    /**
     * Getter for value as json format
     *
     * @return Json value with info about this data
     */
    public String getJsonValue() {
        return jsonValue;
    }
}
