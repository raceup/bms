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

package com.raceup.ed.bms.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utils method on in/out streams
 */
public class Streams {

    /**
     * Reads all content from stream
     *
     * @param reader reader of content
     * @return content (in string format)
     * @throws IOException when it cannot read stream
     */
    public static String readAllFromStream(final BufferedReader reader)
            throws IOException {
        String content = "";

        boolean thereIsAnotherLine = true;
        while (thereIsAnotherLine) {
            String line = reader.readLine();
            if (line != null) {
                content += line + "\n";
            } else {
                thereIsAnotherLine = false;
            }

        }

        return content;
    }

    /**
     * Removes all content in file
     *
     * @param file file to be emptied
     */
    public static void emptyFileOrFail(File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
