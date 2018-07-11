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

package com.raceup.ed.bms.models.stream.bms;

/**
 * BMS logging system.
 * Can intercept any failure, alert or error derived from malfunction of BMS.
 * Informs anyone of its status.
 */
public class BmsLog extends BmsData {
    /**
     * Cast from generic data to value
     *
     * @param data generic data type
     */
    public BmsLog(BmsData data) {
        super(data.getType(), Integer.toString(data.getBms()), data.value);
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
     * String converting method
     *
     * @return object properties written in a nice way
     */
    @Override
    public String toString() {
        return getValue();
    }
}
