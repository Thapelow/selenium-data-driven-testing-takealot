package org.example;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        ReadExcelFile config = new ReadExcelFile("takeAlotSheet.xlsx");
        String err = config.getData(0,0,4);
        System.out.println(err);
    }
}