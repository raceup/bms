package com.raceup.ed.bms.battery;

// TODO add docs
public class BmsDevice {
    private Cell[] cells;

    public BmsDevice(int numberOfCells) {
        cells = new Cell[numberOfCells];
    }

    public double getTemperature1() {
        return cells[0].getTemperature();
    }

    public void setTemperature1(double value) {
        for (int i = 0; i < (cells.length + 1) / 2; i++) {
            cells[i].setTemperature(value);
        }
    }

    public double getTemperature2() {
        return cells[cells.length - 1].getTemperature();  // last cell
    }

    public void setTemperature2(double value) {
        for (int i = (cells.length + 1) / 2; i < cells.length; i++) {
            cells[i].setTemperature(value);
        }
    }

    public double getVoltageOfCell(int index) {
        return cells[index].getVoltage();
    }

    public void setVoltage(int index, double value) {
        cells[index].setVoltage(value);
    }
}
