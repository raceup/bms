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
 * All possible types of data coming from arduino
 */
public enum Data {
    Status,  // log
    Alert,
    Fault,
    Log,
    Voltage,  // value
    Temperature;

    public boolean isValue() {
        return (this.name().equals(Data.Voltage.name()) ||
                this.name().equals(Data.Temperature.name()));
    }

    public boolean isLog() {
        return (this.name().equals(Data.Status.name()) ||
                this.name().equals(Data.Alert.name()) ||
                this.name().equals(Data.Fault.name()) ||
                this.name().equals(Data.Log.name()));
    }
}
