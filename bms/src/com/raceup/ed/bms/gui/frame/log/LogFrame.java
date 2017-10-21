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


package com.raceup.ed.bms.gui.frame.log;

import com.raceup.ed.bms.stream.bms.data.BmsLog;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Frame containing errors, failures and debug notes from BMS
 */
public class LogFrame extends JFrame {
    private final StatusPanel statusPanel = new StatusPanel();  // panel with last log status
    private final ConsolePane consolePane = new ConsolePane();  // setup pane with log status

    public LogFrame() {
        super("Logging");
        setLayout(new BorderLayout());
        setup();
    }

    /**
     * Update scroll pane with new data
     *
     * @param data bms log data
     */
    public void update(BmsLog data) {
        statusPanel.update(data.getTypeOfLog(), getColorOfStatus(data), data.getValue());
        consolePane.updateOrFail(data.toString() + "\n", getColorOfStatus(data));
    }

    /**
     * Get color that represents status
     *
     * @param data log data
     * @return color of status
     */
    private Color getColorOfStatus(BmsLog data) {
        if (data.isStatus()) {
            return Color.WHITE;
        } else if (data.isAlert()) {
            return Color.YELLOW;
        } else if (data.isFault()) {
            return Color.RED;
        } else if (data.isLog()) {
            return Color.GREEN;
        } else {
            return Color.GRAY;
        }
    }

    /**
     * Setup gui
     */
    private void setup() {
        add(statusPanel, BorderLayout.NORTH);  // add status panel on top layout

        JScrollPane scrollPane = new JScrollPane(consolePane);  // add scrollbar
        add(scrollPane, BorderLayout.CENTER);  // add scrollbar on rest of layout
    }

    /**
     * Panel with status
     * | status | color | log
     */
    private class StatusPanel extends JPanel {
        private JLabel title;
        private OvalComponent ovalComponent;
        private JLabel log;

        StatusPanel() {
            super();
            setLayout(new GridLayout(2, 2));  // add components horizontally
            setup();
        }

        /**
         * Update panel with name, color and log
         *
         * @param title title of status
         * @param c     color of status
         * @param log   verbose log
         */
        private void update(String title, Color c, String log) {
            this.title.setText(title);
            this.ovalComponent.setColor(c);
            this.log.setText(log);
        }

        /**
         * Setup component
         */
        private void setup() {
            title = new JLabel("Title");  // setup components
            add(title);  // add components

            ovalComponent = new OvalComponent();  // create new circle
            add(ovalComponent);

            log = new JLabel("Log");
            add(log);
        }

        /**
         * Circle to mark recent log status
         */
        private class OvalComponent extends JComponent {
            private final int x;  // settings to paint component
            private final int y;
            private final int width;
            private final int height;
            private Color c;

            /**
             * Create new oval component
             */
            OvalComponent() {
                super();

                this.x = 0;
                this.y = 0;
                this.width = 10;
                this.height = 10;
            }

            /**
             * Set color of this oval component
             *
             * @param c color to fill this component
             */
            public void setColor(Color c) {
                this.c = c;
                repaint();
            }

            /**
             * Paint in component with graphics
             *
             * @param g graphics to paint component
             */
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(c);  // set color
                g.fillOval(x, y, width, height);  // fill oval
            }
        }
    }

    /**
     * Autoscroll pane
     */
    private class ConsolePane extends JTextPane {
        private final Style textStyle = addStyle("default", null);  // add style
        private final int BUFFER_SIZE = 50 * 10;  // ~10 rows
        private boolean isAutoScrolling = true;

        ConsolePane() {
            super();
            setup();
        }

        /**
         * Update with new text
         *
         * @param msg new message
         * @param c   color of message to set in pane
         */
        private void updateOrFail(String msg, Color c) {
            StyleConstants.setForeground(textStyle, c);  // change color
            StyledDocument doc = (StyledDocument) getDocument();  // get document in pane
            try {
                doc.insertString(doc.getLength(), msg, textStyle);
            } catch (Exception e) {
                System.err.println(e.toString());
            }

            if (isAutoScrolling) {
                setCaretPosition(doc.getLength());  // set position on last line
            }

            if (doc.getLength() > BUFFER_SIZE) {  // panel buffer is at max size -> shift
                try {
                    int firstLineBreak = doc.getText(0, doc.getLength()).indexOf("\n");
                    int secondLineBreak = doc.getText(0, doc.getLength()).indexOf("\n", firstLineBreak + 1);
                    getDocument().remove(0, secondLineBreak);
                    System.out.println(secondLineBreak);
                } catch (BadLocationException e) {
                    System.err.println("Cannot edit buffer size in console pane");
                }
            }
        }

        /**
         * Set autoscroll mode
         *
         * @param isAutoScrolling True iff pane should be autoscrolling on new lines
         */
        private void setAutoScroll(boolean isAutoScrolling) {
            this.isAutoScrolling = isAutoScrolling;
        }

        /**
         * Setup component
         */
        private void setup() {
            setBackground(Color.black);  // black screen with colorful logs
            setEditable(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (SwingUtilities.isLeftMouseButton(mouseEvent)) {  // left button
                        setAutoScroll(true);
                    } else if (SwingUtilities.isRightMouseButton(mouseEvent)) {  // right button
                        setAutoScroll(false);
                    }
                }
            });

            StyleConstants.setBold(textStyle, true);  // bold
        }
    }
}
