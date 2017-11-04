package com.raceup.ed.bms.battery;

/**
 * Position of device in pack
 */
public class BmsDevicePosition {
    private int positionInSegment;
    private int segment;

    /**

     * Creates new position
     *
     * @param positionInSegment position relative to segment
     * @param segment           which segment device belongs to
     */
    public BmsDevicePosition(int positionInSegment, int segment) {
        this.positionInSegment = positionInSegment;
        this.segment = segment;
    }

    /**
     * Gets number of bms in segment
     *
     * @return number of bms in segment
     */
    public int getPositionInSegment() {
        return positionInSegment;
    }

    /**
     * Gets segment
     *
     * @return segment of bms
     */
    public int getSegment() {
        return segment;
    }
}
