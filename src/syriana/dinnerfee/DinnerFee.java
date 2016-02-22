package syriana.dinnerfee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

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
 * @author Syriana
 */
public class DinnerFee extends AbstractHandler{

	public HashMap<String, String> code2nameMap = new HashMap<String, String>();
	
	public DinnerFee() {
		
	}
	
	@Override
	public void init() throws Exception {
		// 加载科室配置
		InputStream stream = DinnerFee.class.getClassLoader().getResourceAsStream(Commanager.getRootPath() + "/dinnerfee/officecode.xml");
		System.out.println();
		Document doc = XmlUtils.load(stream);
		Element config = doc.getDocumentElement();
		Element[] officeNodeList = XmlUtils.getChildrenByName(config, "office");
		for(Element officeNode : officeNodeList){
			String code = officeNode.getAttribute("id");
			String name = officeNode.getAttribute("name");
			code2nameMap.put(code, name);
		}
	}
	
	public void handle_10010 (String path, HashMap<String, String> params) throws Exception{
		Commanager.addLogger2Queue("===>>>开始处理夜餐费文件.....请不要乱动");
		System.out.println("===>>>开始处理夜餐费文件.....请不要乱动");
		File saveFile = new File(path + "//汇总.xlsx");
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
			toSave.createSheet("人数汇总");
			toSave.createSheet("科室汇总");
			Sheet empSheet = toSave.getSheet("人数汇总");
			Sheet officeSheet = toSave.getSheet("科室汇总");
			
			// 读取数据
			File rootPath = new File(path);
			File[] files = rootPath.listFiles();
			for(File file : files){
				if(!file.getName().endsWith(".xls") && !file.getName().endsWith(".xlsx")){
					continue;
				}
				String officeCode = file.getName().split("\\.")[0].substring(0,3);
				String officeName = code2nameMap.get(officeCode);
				currentFileName = file.getName();
				double officeTotalFee = 0L;
				Workbook workbook = ExcelUtil.createWorkBook(file);
				int sheetLength = workbook.getNumberOfSheets();
				for(sheetIndex = 0; sheetIndex < sheetLength; sheetIndex++){
					Sheet sheet = workbook.getSheetAt(sheetIndex);
					String sheetName = sheet.getSheetName();
					if(sheetName.contains("编号")){
						continue;
					}
					currentSheetName = sheetName;
					int rowLength = sheet.getLastRowNum();
					int startRow = 3;
					for(lineIndex = startRow; lineIndex < rowLength; lineIndex++){
						Row row = sheet.getRow(lineIndex);
						if(row != null){
							Cell cellName = row.getCell(1);
							Cell cellCardNumber = row.getCell(4);
							Cell cellFeeNum = row.getCell(5);
							String empName = ExcelUtil.getCellStringValue(cellName);
							String empCard = ExcelUtil.getCellStringValue(cellCardNumber);
							String empFee = ExcelUtil.getCellStringValue(cellFeeNum);
							if(empName == null || empName.trim().equals("")){
								break;
							}
							// 草尼玛乱插一行空的
							try{
								if(StringUtils.isEmpty(empFee)){
									empFee = 0 + "";
								}
								else if(empFee.contains("元")){
									empFee = empFee.replace("元", "");
								}
								officeTotalFee += Double.parseDouble(empFee);
							} catch(Exception e){
								Commanager.addLogger2Queue("===>>>处理【" + currentFileName
										+ "】时表名为【" + currentSheetName + "】的第【" + lineIndex + "】行数据时，第6列数据时，不是数字，已跳过处理");
								System.out.println("===>>>处理【" + currentFileName
										+ "】时表名为【" + currentSheetName + "】的第【" + lineIndex + "】行数据时，第6列数据时，不是数字，已跳过处理");
								continue;
							}
							
							Row empRow = empSheet.createRow(empSheet.getLastRowNum() + 1);
							
							Cell officeCodeCell = empRow.createCell(0);
							Cell officeNameCell = empRow.createCell(1);
							Cell empCellName = empRow.createCell(2);
							Cell empCellCard = empRow.createCell(3);
							Cell empCellFee = empRow.createCell(4);
							officeCodeCell.setCellValue(officeCode);
							officeNameCell.setCellValue(officeName);
							empCellName.setCellValue(empName);
							empCellCard.setCellValue(empCard);
							empCellFee.setCellValue(empFee);
						}
					}
				}
				// 记录总今个
				Row officeRow = officeSheet.createRow(officeSheet.getLastRowNum() + 1);
				Cell codeOfficeCell = officeRow.createCell(0);
				Cell nameOfficeCell = officeRow.createCell(1);
				Cell totalFeeOfficeCell = officeRow.createCell(2);
				codeOfficeCell.setCellValue(officeCode);
				nameOfficeCell.setCellValue(officeName);
				totalFeeOfficeCell.setCellValue(officeTotalFee + "");
			}
			//保存
			saveFile.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(saveFile);
			toSave.write(fileOut);
			fileOut.close();
			Commanager.addLogger2Queue("===>>>处理完毕,在选择目录下生成【汇总.xls】");
			System.out.println("===>>>处理完毕,在选择目录下生成【汇总.xls】");
		} catch (Exception e) {
			Commanager.addLogger2Queue("===>>>处理【" + currentFileName
					+ "】时表名为【" + currentSheetName + "】的第【" + lineIndex + "】行数据时，格式不符合规范，程序出错了");
			System.out.println("===>>>处理【" + currentFileName
					+ "】时表名为【" + currentSheetName + "】的第【" + lineIndex + "】行数据时，格式不符合规范，程序出错了");
			e.printStackTrace();
		}
	}
	
	public void createOfficeCodeXml(String samplePath) throws Exception{
		Document doc = XmlUtils.blankDocument("config");
		Element rootNode = doc.getDocumentElement();
		File sampleFile = new File(samplePath);
		Workbook workbook = ExcelUtil.createWorkBook(sampleFile);
		Sheet sheet = workbook.getSheet("科室编号");
		int rowLength = sheet.getLastRowNum();
		int startRow = 1;
		for(int i = startRow; i < rowLength; i++){
			Row row = sheet.getRow(i);
			Cell code = row.getCell(0);
			Cell name = row.getCell(1);
			String codeStr = ExcelUtil.getCellStringValue(code);
			if(StringUtils.isEmpty(codeStr)){
				break;
			}
			String nameStr = ExcelUtil.getCellStringValue(name);
			Element idNode = XmlUtils.createChild(doc, rootNode, "office");
			idNode.setAttribute("id", codeStr);
			idNode.setAttribute("name", nameStr);
		}
		XmlUtils.save("D:\\12315.xml",doc);
	}
	
	public static void main(String[] args) throws Exception{
//		String path = "E:\\kunkun";
//		DinnerFee dinnerFee = new DinnerFee(MainType.DinnerFee, SubType.Default);
//		dinnerFee.init();
//		dinnerFee.handleDinnerFee(path, null);
//		dinnerFee.createOfficeCodeXml("E:\\kunkun\\样表1.xls");
	}

	@Override
	public void disPatch(int subType, String path) throws Exception {
		SubType sub = SubType.getSubTypeById(subType);
		switch (sub) {
		case DinnerFeeDefault:{
			handle_10010(path, null);
			break;
		}
		default:
			break;
		}
	}
}
