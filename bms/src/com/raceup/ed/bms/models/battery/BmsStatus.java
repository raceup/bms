package com.raceup.ed.bms.models.battery;

import com.raceup.ed.bms.models.stream.bms.BmsLog;

public class BmsStatus {
    private BmsLog currentStatus;
    private BmsLog lastStatus;

    public BmsStatus(BmsLog currentStatus) {
        update(currentStatus);
    }

    public void update(BmsLog currentStatus) {
        lastStatus = this.currentStatus;
        this.currentStatus = currentStatus;
    }

    public boolean hasChanged() {
        if (currentStatus == null) {
            return false;
        }

        if (lastStatus == null) {
            return true;
        }

        return currentStatus.getTime() != lastStatus.getTime();

    }

    public String getStatus() {
        if (currentStatus != null) {
            return currentStatus.getRawValue();
        }

        return null;
    }
}
