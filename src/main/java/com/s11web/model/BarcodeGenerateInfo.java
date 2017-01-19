package com.s11web.model;

import lombok.Getter;
import lombok.Setter;

public class BarcodeGenerateInfo {


    String cargoType;


    String sourceFC;


    String destinationFC;


    String currentNumber;


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

    public String getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(String currentNumber) {
        this.currentNumber = currentNumber;
    }
}
