package com.raceup.ed.bms.battery;

/**
 * Position of device in pack
 */
class BmsDevicePosition {
    public int positionInSegment;
    public int segment;

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
}
