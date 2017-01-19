package com.s11web.model;

import lombok.Getter;
import lombok.Setter;

public class Lane {

    @Setter @Getter
    private String LaneE;

    @Setter @Getter
    private String departTime;

    @Setter @Getter
    private int deliveryDuration;

    public String getLaneE() {
        return LaneE;
    }

    public void setLaneE(String laneE) {
        LaneE = laneE;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }
}
