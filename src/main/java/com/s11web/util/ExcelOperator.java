package com.s11web.util;

import java.util.List;

import com.s11web.model.SheetEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelOperator {

    public XSSFWorkbook generateExcel(List<SheetEntity> sheetEntityList) {

        XSSFWorkbook wb = new XSSFWorkbook();
        for (SheetEntity sheetEntity : sheetEntityList) {
            Sheet sheet = wb.createSheet(sheetEntity.getSheetName());
            List<String[]> rowList = sheetEntity.getRowList();
            for (int i = 0; i < rowList.size(); ++i) {
                Row row = sheet.createRow(i);
                String[] rowInfo = rowList.get(i);
                for (int j = 0; j < rowInfo.length; ++j) {
                    row.createCell(j).setCellValue(rowInfo[j]);
                }
            }
        }

        return wb;
    }
}

