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
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Simple splash screen of app
 */
class SplashScreen extends JFrame implements Runnable {
    private static final int statusBarHeight = 20;  // height of statusbar in pixel
    private volatile boolean stopped = false;
    private JLabel statusBar;

    /**
     * Creates a splash screen
     *
     * @param backgroundImage image to set as background
     */
    public SplashScreen(BufferedImage backgroundImage) {
        super();
        setUndecorated(true);  // remove window borders
        setupGui(backgroundImage);
        setSize(new Dimension(backgroundImage.getWidth(), backgroundImage.getHeight()));
        setLocationRelativeTo(null);  // center in screen
    }

    /**
     * Set update text in screen
     *
     * @param text text to set in statusbar
     */
    public void update(String text) {
        statusBar.setText(text);
    }

    /**
     * Keep splash screen on-screen
     */
    public void run() {
        setVisible(true);

        stopped = false;  // when restating stopped is true ..
        while (!stopped) {
        }
    }

    /**
     * Exit and dispose splash screen
     */
    public void stop() {
        setVisible(false);
        dispose();
        stopped = true;
    }

    /**
     * Setup splash screen GUI
     *
     * @param backgroundImage image to set as background
     */
    private void setupGui(BufferedImage backgroundImage) {
        add(new ImagePanel(backgroundImage));

        JPanel statusPanel = new JPanel();  // where status bar will be
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(getWidth(), statusBarHeight));  // setup statusbar
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

        statusBar = new JLabel("");
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);

        statusPanel.add(statusBar);
        add(statusPanel, BorderLayout.SOUTH);  // add panel to frame
    }

    /**
     * Make it easy to draw background images in frames
     */
    private class ImagePanel extends JComponent {
        private final Image image;

        public ImagePanel(Image image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }
}
