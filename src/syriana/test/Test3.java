package syriana.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import syriana.common.ExcelUtil;
import syriana.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Test3 {


    public static void main(String[] args) throws Exception {

        Set<Integer> gwdbSet = new HashSet<>();

        File file3 = new File("C:/Users/zh/Desktop/gwdb查询结果.txt");
        try (FileInputStream fileInputStream = new FileInputStream(file3)) {
            BufferedInputStream in = new BufferedInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(!StringUtils.isEmpty(line)){
                   int id = Integer.parseInt(line);
                    gwdbSet.add(id);
                }
            }
        }

        int i = 0;
        File file4 = new File("C:/Users/zh/Desktop/manage导出结果.txt");
        try (FileInputStream fileInputStream = new FileInputStream(file4)) {
            BufferedInputStream in = new BufferedInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(!StringUtils.isEmpty(line)){
                    int id = Integer.parseInt(line);
                    if(!gwdbSet.contains(id)){
                        System.out.println(id);
                        i++;
                    }
                }
            }
        }
        System.out.println("=======>count :" + i);
    }
}
