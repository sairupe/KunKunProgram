package syriana.test.上下半年统计经费;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import syriana.common.ExcelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class 上半年 {

    private static final String SAVE_PATH = "C:\\Users\\Administrator\\Desktop\\sda\\2017上半年";
    private static final String READ_PATH = "C:\\Users\\Administrator\\Desktop\\sda\\2017上半年";
    private static final int HEJI_CELL_INDEX = 3;
    private static final int FILENAME_CELL_INDEX = 4;
    private static int saveFileRowIndex = 0;

    public static void main(String[] args) {
        File saveFile = new File(SAVE_PATH + "//上半年.xlsx");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        String currentFileName = "";
        String currentSheetName = "";
        int readIndex = 0;
        int currentCellIndex = 0;
        try {
            // 保存的新文件
            Workbook toSave = new XSSFWorkbook();
            Sheet toSaveSheet = toSave.createSheet("上半年");

            // 读取数据
            File rootPath = new File(READ_PATH);
            File[] files = rootPath.listFiles();
            for (File file : files) {
                if (!file.getName().endsWith(".xls") && !file.getName().endsWith(".xlsx")) {
                    continue;
                }
                currentFileName = file.getName();
                Workbook workbook = ExcelUtil.createWorkBook(file);
                Sheet sheet = workbook.getSheetAt(0);
                currentSheetName = sheet.getSheetName();
                int rowLength = sheet.getLastRowNum();
                int startRow = 0;
                // 金额
                double moneyTotal = 0;
                List<Row> createNewRowList = new ArrayList<>();
                if(currentFileName.contains("赴仙葫校区上课老师交通补助")){
                    System.out.println();
                }
                for (readIndex = startRow; readIndex <= rowLength; readIndex++) {
                    Row readRow = sheet.getRow(readIndex);
                    if (readRow == null) {
                        continue;
                    }
                    int cellIndex = 0;
                    Cell checkCell = readRow.getCell(cellIndex);
                    String str = ExcelUtil.getCellStringValue(checkCell);
                    if(str == null || str.trim() == ""){
                        continue;
                    }
                    // 数据不为空行才创建
                    Row saveRow = toSaveSheet.createRow(saveFileRowIndex);
                    createNewRowList.add(saveRow);
                    for (; cellIndex < 3; cellIndex++) {
                        currentCellIndex = cellIndex;
                        Cell cell = readRow.getCell(cellIndex);
                        String cellValue = ExcelUtil.getCellStringValue(cell);
                        Cell newCell = saveRow.createCell(cellIndex);
                        newCell.setCellValue(cellValue);

                        // 第一行是金额才进行总数统计
                        if(cellIndex == 0){
                            moneyTotal += Double.parseDouble(cellValue);
                        }
                    }
                    Cell hejiCell = saveRow.createCell(HEJI_CELL_INDEX);
                    Cell fileNameCell = saveRow.createCell(FILENAME_CELL_INDEX);
                    saveFileRowIndex++;
                }
                for(Row saveRow : createNewRowList){
                    Cell hejiCell = saveRow.getCell(HEJI_CELL_INDEX);
//                    if(hejiCell == null){
//                        hejiCell = saveRow.createCell(HEJI_CELL_INDEX);
//                    }
                    hejiCell.setCellValue(String.valueOf(moneyTotal));
                    Cell fileNameCell = saveRow.getCell(FILENAME_CELL_INDEX);
//                    if(fileNameCell == null){
//                        fileNameCell = saveRow.createCell(FILENAME_CELL_INDEX);
//                    }
                    fileNameCell.setCellValue(currentFileName);
                }
            }

            //保存
            saveFile.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(saveFile);
            toSave.write(fileOut);
            fileOut.close();
            System.out.println("===>>>处理完毕,在选择目录下生成【上半年.xlsx】");
        } catch (Exception e) {
            System.out.println("===>>>处理【" + currentFileName
                    + "】时表名为【" + currentSheetName + "】的第【" + readIndex + "】行【" + currentCellIndex + "】列数据时，格式不符合规范，程序出错了");
            System.out.println("===>>>处理【" + currentFileName
                    + "】时表名为【" + currentSheetName + "】的第【" + readIndex + "】行【" + currentCellIndex + "】列数据时，格式不符合规范，程序出错了");
            e.printStackTrace();
        }
    }
}
