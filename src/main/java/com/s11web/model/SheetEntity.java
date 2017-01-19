package com.s11web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SheetEntity {
    @Setter @Getter
    private String SheetName;

    @Setter @Getter
    private List<String[]> RowList;

    public String getSheetName() {
        return SheetName;
    }

    public void setSheetName(String sheetName) {
        SheetName = sheetName;
    }

    public List<String[]> getRowList() {
        return RowList;
    }

    public void setRowList(List<String[]> rowList) {
        RowList = rowList;
    }
}
