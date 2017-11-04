package com.raceup.ed.bms.battery;

public class BmsDevice {
    private Cell[] cells;

    /**
     * Builds new Bms device
     *
     * @param numberOfCells number of cells in BMS
     */
    public BmsDevice(int numberOfCells) {
        cells = new Cell[numberOfCells];
    }

    /**
     * Gets temperature 1
     * @return temperature 1
     */
    public double getTemperature1() {
        return cells[0].getTemperature();
    }

    /**
     * Sets temperature 2 value
     * @param value temperature 2
     */
    public void setTemperature1(double value) {
        for (int i = 0; i < (cells.length + 1) / 2; i++) {
            cells[i].setTemperature(value);
        }
    }

    /**
     * Gets temperature 2
     * @return temperature 2
     */
    public double getTemperature2() {
        return cells[cells.length - 1].getTemperature();  // last cell
    }

    /**
     * Sets temperature 2
     * @param value temperature 2
     */
    public void setTemperature2(double value) {
        for (int i = (cells.length + 1) / 2; i < cells.length; i++) {
            cells[i].setTemperature(value);
        }
    }

    /**
     * Gets voltage of specific cell
     * @param index number of cell
     * @return voltage of cell
     */
    public double getVoltageOfCell(int index) {
        return cells[index].getVoltage();
    }

    /**
     * Sets voltage of specific cell
     * @param index number of cell
     * @param value voltage of cell
     */
    public void setVoltage(int index, double value) {
        cells[index].setVoltage(value);
    }
}
