package syriana.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import syriana.common.ExcelUtil;
import syriana.util.StringUtils;

public class Test2 {


    public static void main(String[] args) throws Exception {

        Set<String> excelNameSet = new HashSet<String>();
        Set<String> _574_Set = new HashSet<String>();
        Set<String> fristSet = new HashSet<String>();

        Workbook name9 = ExcelUtil.createWorkBook(new File("C:/Users/zh/Desktop/version/教材版本.xlsx"));
        Sheet sheet = name9.getSheetAt(0);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell nameCell = row.getCell(0);
            String name = ExcelUtil.getCellStringValue(nameCell);
            excelNameSet.add(name);
        }

        File file = new File("C:/Users/zh/Desktop/version/574_exsist_books.txt");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            BufferedInputStream in = new BufferedInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(!StringUtils.isEmpty(line)){
                    _574_Set.add(line);
                }
            }
        }

        File file3 = new File("C:/Users/zh/Desktop/version/first_import_boos.txt");
        try (FileInputStream fileInputStream = new FileInputStream(file3)) {
            BufferedInputStream in = new BufferedInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(!StringUtils.isEmpty(line)){
                    fristSet.add(line);
                }
            }
        }


        System.out.println("excelNameSet : " + excelNameSet.size());
        System.out.println("_574_Set : " + _574_Set.size());
        System.out.println("firstSet : " + fristSet.size() + "\n\n\n\n");

        for(String execelName : excelNameSet){
            if(fristSet.contains(execelName)){
                continue;
            }
            if(!_574_Set.contains(execelName)){
                System.out.println(execelName);
            }
        }
    }
}
