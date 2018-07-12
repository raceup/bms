package com.raceup.ed.bms.models.battery;

import com.raceup.ed.bms.models.stream.bms.BmsLog;

public class BmsStatus {
    private final BmsLog currentStatus;
    private BmsLog lastStatus;

    public BmsStatus(BmsLog currentStatus) {
        update(currentStatus);

        this.currentStatus = currentStatus;
    }

    public void update(BmsLog currentStatus) {
        lastStatus = currentStatus;
    }

    public boolean hasChanged() {
        return currentStatus == null || lastStatus == null || currentStatus.getTime() != lastStatus.getTime();

    }

    public String getStatus() {
        if (currentStatus != null) {
            return currentStatus.getValue();
        }

        return null;
    }
}
