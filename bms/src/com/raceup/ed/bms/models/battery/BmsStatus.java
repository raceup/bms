package com.raceup.ed.bms.models.battery;

import com.raceup.ed.bms.models.stream.bms.BmsLog;

public class BmsStatus {
    private final BmsLog currentStatus;
    private BmsLog lastStatus;

    public BmsStatus(BmsLog currentStatus) {
        updateStatus(currentStatus);

        this.currentStatus = currentStatus;
    }

    private void updateStatus(BmsLog currentStatus) {
        lastStatus = currentStatus;
    }

    public boolean hasChanged() {
        return currentStatus.getTime() != lastStatus.getTime();
    }

    public String getStatus() {
        return currentStatus.getValue();
    }
}
