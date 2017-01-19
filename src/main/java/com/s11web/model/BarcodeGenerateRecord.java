package com.s11web.model;

import lombok.Getter;
import lombok.Setter;

public class BarcodeGenerateRecord {

    @Setter @Getter
    int id;

    @Setter @Getter
    String userName;

    @Setter @Getter
    String cargoType;

    @Setter @Getter
    String sourceFC;

    @Setter @Getter
    String destinationFC;

    @Setter @Getter
    int number;

    @Setter @Getter
    String startBarcode;

    @Setter @Getter
    String endBarcode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStartBarcode() {
        return startBarcode;
    }

    public void setStartBarcode(String startBarcode) {
        this.startBarcode = startBarcode;
    }

    public String getEndBarcode() {
        return endBarcode;
    }

    public void setEndBarcode(String endBarcode) {
        this.endBarcode = endBarcode;
    }
}
