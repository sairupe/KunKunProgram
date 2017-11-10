package syriana.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import syriana.util.StringUtils;

public class ExcelUtil {
	public static String getCellStringValue(Cell cell){
		if (null != cell) {
            switch (cell.getCellType()){// 判断单元格的数据类型
			case Cell.CELL_TYPE_NUMERIC:
				if (cell.getNumericCellValue() > 0
						&& cell.getNumericCellValue() > (int) cell
								.getNumericCellValue()) {
					return cell.getNumericCellValue() + "";
				} 
				else if(cell.getNumericCellValue() < 0
						&& cell.getNumericCellValue() < (int) cell
						.getNumericCellValue()){
					return cell.getNumericCellValue() + "";
				}
				else {
					return (int) cell.getNumericCellValue() + "";
				}
//				if(cell.getNumericCellValue() > (int)cell.getNumericCellValue()){
//					return cell.getNumericCellValue() + "";
//				}
//				else{
//					return (int)cell.getNumericCellValue() + "";
//				}
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getNumericCellValue() + "";
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? "true" : "false";
			default:
				return cell.getStringCellValue();
			}
		}
		return "";
	}
	
	public static boolean isEmpty(Cell cell){
		return StringUtils.isEmpty(getCellStringValue(cell));
	}
	
	public static Workbook createWorkBook(File path) throws Exception {
		InputStream  in = new FileInputStream(path);
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了,PATH:" + path);
    }
}
