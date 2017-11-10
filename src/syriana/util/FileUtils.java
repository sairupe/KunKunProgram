package syriana.util;

import java.io.File;

import org.apache.poi.ss.usermodel.Sheet;

import syriana.general.SheetCombine5_9;

public class FileUtils {

	static File file = new File("C:\\Users\\zh\\Desktop\\周末补贴5-9月份");
	
	public static void main(String[] args){
	}
	
	public static void recursionAllFile(File file, SheetCombine5_9 sheetcombine, Sheet toSaveSheet) throws Exception{
		File[] files = file.listFiles();
		for(File file2 : files){
			if(file2.isDirectory()){
				recursionAllFile(file2, sheetcombine, toSaveSheet);
			}
			else{
				sheetcombine.dealFileRecursive(file2, toSaveSheet);
			}
		}
	}
}
