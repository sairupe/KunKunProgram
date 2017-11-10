package syriana.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import syriana.common.Commanager;
import syriana.common.ExcelUtil;
import syriana.util.StringUtils;


public class ReNameAll {
	
	private static Map<String, Integer> name2CountMap = new LinkedHashMap<String, Integer>();
	private static List<String> nameList = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {
		int rowLength = 0;
		int rowIndex = 1;
		try {
			// 读取EXCEL
			Workbook name9 = ExcelUtil.createWorkBook(new File(
					"D://12315/NAME9.xls"));
			Workbook name10 = ExcelUtil.createWorkBook(new File(
					"D://12315/NAME10.xls"));

			Sheet sheet = name9.getSheetAt(0);
			rowLength = sheet.getLastRowNum();
			for (rowIndex = 1; rowIndex <= rowLength; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row != null) {
					Cell cellName = row.getCell(1);
					String roleName = ExcelUtil.getCellStringValue(cellName);
					name2CountMap.put(roleName, 0);
					nameList.add(roleName);
				}
			}
			System.out.println("==============表格分割线===============");
			sheet = name10.getSheetAt(0);
			rowLength = sheet.getLastRowNum();
			for (rowIndex = 1; rowIndex <= rowLength; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row != null) {
					Cell cellName = row.getCell(1);
					String roleName = ExcelUtil.getCellStringValue(cellName);
					name2CountMap.put(roleName, 0);
					nameList.add(roleName);
				}
			}
			System.out.println("===========>>>>name2CountMap" + name2CountMap.size());
			System.out.println("===========>>>>nameList" + nameList.size());
			dealmysql();
			
		} catch (Exception e) {
			System.out.println("===========>>>>rowLength" + rowIndex + " 出错了");
			e.printStackTrace();
		}
	}
		
		
		
		
		
		
	public static void dealmysql() {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://192.168.40.253:3306/cehua_backup";
		// String urlstr = "jdbc:mysql://localhost:3306/cehua_bak";
		Connection con;
		String sql = "";
		Statement stmt;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("classnotfoundexception :");
			System.err.print(e.getMessage());
		}
		try {
			con = DriverManager.getConnection(urlstr, "cehua", "445566");
			// con = DriverManager.getConnection(urlstr, "root", "root");
			stmt = con.createStatement();
			// 向test表中插入一条数据
			// sql = "select count(*) from player";
			// ============================================先查出所有玩家==========================================
			sql = "select * from player";
			// 检索test表中的所有记录并获取数据输出
			ResultSet rs0 = stmt.executeQuery(sql);
			while (rs0.next()) {
				int id = rs0.getInt("id");
				String nickName = nameList.get(dealnum);
				String updateSql = "";
				if (name2CountMap.containsKey(nickName)) {
					int repeatCount = name2CountMap.get(nickName);
					repeatCount++;
					name2CountMap.put(nickName, repeatCount);
					if (repeatCount > 1) {
						updateSql = "update player set nickname='" + nickName
								+ repeatCount + "' where id=" + id;
					} else {
						updateSql = "update player set nickname='" + nickName
								+ "' where id=" + id;
					}
				} else {
					updateSql = "update player set nickname='" + nickName
							+ "' where id=" + id;
				}
				con.createStatement().executeUpdate(updateSql);
				dealnum++;
			}
			// ============================================先查出所有玩家==========================================
			// 关闭连接
			stmt.close();
			con.close();
			System.out
					.println("======================处理完毕dealNum============================="
							+ dealnum);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("sqlexception :" + ex.getMessage());
			System.err.println("sql :" + sql);
			System.err.println("dealnum :" + dealnum);
		}
	}
}
