package com.s11web.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by xietian
 * 2017/5/4.
 */


@Entity
@Table(name = "S11_Storage_Rate")
public class StoreRate {
    @Id
    @Column(name = "id")
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String carrierName;
    @Getter
    @Setter
    private String arc;
    @Getter
    @Setter
    private String sourceFC;
    @Getter
    @Setter
    private String destinationFC;
    @Getter
    @Setter
    private String cargoesType;
    @Getter
    @Setter
    private String departureDate;
    @Getter
    @Setter
    private String departureNum;
    @Getter
    @Setter
    private String destinationDate;
    @Getter
    @Setter
    private String destinationNum;
    @Getter
    @Setter
    private String StorageRate;

    public StoreRate(String carrierName, String arc, String sourceFC, String destinationFC, String cargoesType, String departureDate, String departureNum, String destinationDate, String destinationNum, String storageRate) {
        this.carrierName = carrierName;
        this.arc = arc;
        this.sourceFC = sourceFC;
        this.destinationFC = destinationFC;
        this.cargoesType = cargoesType;
        this.departureDate = departureDate;
        this.departureNum = departureNum;
        this.destinationDate = destinationDate;
        this.destinationNum = destinationNum;
        this.StorageRate = storageRate;
        this.id = carrierName + arc + departureDate.toString();
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getArc() {
        return arc;
    }

    public void setArc(String arc) {
        this.arc = arc;
    }

    public String getSourceFC() {
        return sourceFC;
    }

    public void setSourceFC(String sourceFC) {
        this.sourceFC = sourceFC;
    }

    public String getDestinationFC() {
        return destinationFC;
    }

    public void setDestinationFC(String destinationFC) {
        this.destinationFC = destinationFC;
    }

    public String getCargoesType() {
        return cargoesType;
    }

    public void setCargoesType(String cargoesType) {
        this.cargoesType = cargoesType;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureNum() {
        return departureNum;
    }

    public void setDepartureNum(String departureNum) {
        this.departureNum = departureNum;
    }

    public String getDestinationDate() {
        return destinationDate;
    }

    public void setDestinationDate(String destinationDate) {
        this.destinationDate = destinationDate;
    }

    public String getDestinationNum() {
        return destinationNum;
    }

    public void setDestinationNum(String destinationNum) {
        this.destinationNum = destinationNum;
    }

    public String getStorageRate() {
        return StorageRate;
    }

    public void setStorageRate(String storageRate) {
        StorageRate = storageRate;
    }
}