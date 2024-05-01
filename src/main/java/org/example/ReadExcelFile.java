package org.example;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

public class ReadExcelFile {
    XSSFWorkbook wb;
    XSSFSheet sheet;

    public ReadExcelFile(String excelPath){
        try {
            File file = new File(excelPath);
            FileInputStream fileInputStream = new FileInputStream(file);
            wb = new XSSFWorkbook(fileInputStream);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getData(int sheetIndex, int row, int column){
        sheet = wb.getSheetAt(sheetIndex);
        return sheet.getRow(row).getCell(column).getStringCellValue();
    }

    public int getRowCount(int sheetIndex){
        int row = wb.getSheetAt(sheetIndex).getLastRowNum();
        return row + 1;
    }
}
