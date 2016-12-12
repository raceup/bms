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

package com.raceup.ed.bms.utils.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * Utils methods that concern panels
 */
public class JPanelsUtils {
    /**
     * Set a title border on panel
     *
     * @param panel where to add border
     * @param title title of border
     */
    public static void addTitleBorderOnPanel(JPanel panel, String title) {
        panel.setBorder(
                new CompoundBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), title),  // title border
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)  // empty border
                )
        );
    }

    /**
     * Set an empty border on panel
     *
     * @param panel where to add border
     * @param title title of border
     */
    public static void addTitleEmptyBorderOnPanel(JPanel panel, String title) {
        panel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), title)  // title border
        );
    }
}
