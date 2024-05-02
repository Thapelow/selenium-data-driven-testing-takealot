package org.example;

import org.apache.poi.ss.usermodel.Cell;
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


    public String getData(int sheetIndex, int row, int column) {
        sheet = wb.getSheetAt(sheetIndex);
        Cell cell = sheet.getRow(row).getCell(column);

        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    // Check if the numeric value is an integer
                    if (cell.getNumericCellValue() == (int) cell.getNumericCellValue()) {
                        return String.valueOf((int) cell.getNumericCellValue());
                    } else {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case STRING:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                default:
                    return "";
            }
        } else {
            return ""; // Return empty string if cell is null
        }
    }

    public int getRowCount(int sheetIndex){
        int row = wb.getSheetAt(sheetIndex).getLastRowNum();
        return row + 1;
    }
}
