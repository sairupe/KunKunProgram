package syriana.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import syriana.common.AbstractHandler;
import syriana.common.Commanager;
import syriana.common.ExcelUtil;
import syriana.common.SubType;
import syriana.util.StringUtils;
import syriana.util.XmlUtils;

/**
 * 2016-2-16 下午1:39:01
 * 
 * file : 2016-2017规培夜餐.rar
 *
 * @author Syriana
 */
public class SheetCombine2006_2007 extends AbstractHandler{
	
	private int newRowIndex = 0;
	
	@Override
	public void init() throws Exception {
	}
	
	public void handle_10031 (String path, HashMap<String, String> params) throws Exception{
		Commanager.addLogger2Queue("===>>>开始处理带教费合并.....请不要乱动");
		File saveFile = new File(path + "//带教费合并.xlsx");
		if(saveFile.exists()){
			saveFile.delete();
		}
		String currentFileName = "";
		String currentSheetName = "";
		int lineIndex = 0;
		int sheetIndex;
		try {
			// 保存的新文件
			Workbook toSave = new XSSFWorkbook();
			Sheet toSaveSheet = toSave.createSheet("合并");
			
			// 读取数据
			File rootPath = new File(path);
			File[] files = rootPath.listFiles();
			for(File file : files){
				if(!file.getName().endsWith(".xls") && !file.getName().endsWith(".xlsx")){
					continue;
				}
				currentFileName = file.getName();
				Workbook workbook = ExcelUtil.createWorkBook(file);
				int sheetLength = workbook.getNumberOfSheets();
				for(sheetIndex = 0; sheetIndex < sheetLength; sheetIndex++){
					Sheet sheet = workbook.getSheetAt(sheetIndex);
					String sheetName = sheet.getSheetName();
					currentSheetName = sheetName;
					int rowLength = sheet.getLastRowNum();
					int startRow = 0;
					for(lineIndex = startRow; lineIndex < rowLength; lineIndex++){
						Row row = sheet.getRow(lineIndex);
						if(row != null){
							Row newRow = toSaveSheet.createRow(newRowIndex);
							boolean hasValue = false;
							for(int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++){
								Cell cell = row.getCell(cellIndex);
								Cell newCell = newRow.createCell(cellIndex);
								String str = ExcelUtil.getCellStringValue(cell);
								if(str.contains("合计")){
									continue;
								}
								hasValue = StringUtils.isEmpty(str) | hasValue;
								newCell.setCellValue(str);
							}
							if(!hasValue){
								toSaveSheet.removeRow(newRow);
							}else{
								newRowIndex++;
							}
						}
					}
				}
			}
			
			//保存
			saveFile.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(saveFile);
			toSave.write(fileOut);
			fileOut.close();
			Commanager.addLogger2Queue("===>>>处理完毕,在选择目录下生成【带教费合并.xls】");
		} catch (Exception e) {
			Commanager.addLogger2Queue("===>>>处理【" + currentFileName
					+ "】时表名为【" + currentSheetName + "】的第【" + lineIndex + "】行数据时，格式不符合规范，程序出错了");
			System.out.println("===>>>处理【" + currentFileName
					+ "】时表名为【" + currentSheetName + "】的第【" + lineIndex + "】行数据时，格式不符合规范，程序出错了");
			e.printStackTrace();
		}
	}

	@Override
	public void disPatch(int subType, String path) throws Exception {
		SubType sub = SubType.getSubTypeById(subType);
		switch (sub) {
		case SheetCombine2006_2007:{
			handle_10031(path, null);
			break;
		}
		default:
			break;
		}
	}
}
