package syriana.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

public class ReNameAll {

	private static Map<String, Integer> name2CountMap = new LinkedHashMap<String, Integer>();
	private static List<String> nameList = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		// int rowLength = 0;
		// int rowIndex = 1;
		// try {
		// // 读取EXCEL
		// Workbook name9 = ExcelUtil.createWorkBook(new File(
		// "D://12315/NAME9.xls"));
		// Workbook name10 = ExcelUtil.createWorkBook(new File(
		// "D://12315/NAME10.xls"));
		//
		// Sheet sheet = name9.getSheetAt(0);
		// rowLength = sheet.getLastRowNum();
		// for (rowIndex = 1; rowIndex <= rowLength; rowIndex++) {
		// Row row = sheet.getRow(rowIndex);
		// if (row != null) {
		// Cell cellName = row.getCell(1);
		// String roleName = ExcelUtil.getCellStringValue(cellName);
		// name2CountMap.put(roleName, 0);
		// nameList.add(roleName);
		// }
		// }
		// System.out.println("==============表格分割线===============");
		// sheet = name10.getSheetAt(0);
		// rowLength = sheet.getLastRowNum();
		// for (rowIndex = 1; rowIndex <= rowLength; rowIndex++) {
		// Row row = sheet.getRow(rowIndex);
		// if (row != null) {
		// Cell cellName = row.getCell(1);
		// String roleName = ExcelUtil.getCellStringValue(cellName);
		// name2CountMap.put(roleName, 0);
		// nameList.add(roleName);
		// }
		// }
		// System.out.println("===========>>>>name2CountMap" +
		// name2CountMap.size());
		// System.out.println("===========>>>>nameList" + nameList.size());
		// dealmysql();
		//
		// } catch (Exception e) {
		// System.out.println("===========>>>>rowLength" + rowIndex + " 出错了");
		// e.printStackTrace();
		// }

		// randRobot();
		// randomUpdateOnline();

		//insertSdkCode();
		dealOutterCustomer();
	}
	
	public static void dealOutterCustomer() {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://172.26.1.26:3306/gwdb_xb_manage";
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
			con = DriverManager.getConnection(urlstr, "root", "123456");
			stmt = con.createStatement();
			sql = "select * from usr_business_customer";
			ResultSet rs0 = stmt.executeQuery(sql);
			
			// 解析出来
			Map<Integer, CustomerVo> id2CustomerMap = new HashMap<Integer, CustomerVo>();
			while (rs0.next()) {
				int id = rs0.getInt("ID");
				int totalRobotNum = rs0.getInt("ROBOT_TOTAL_NUM");
				String upIds = rs0.getString("ROUTE");
				List<Integer> idList = new ArrayList<Integer>();
				if(upIds != null && !upIds.equals("")){
					String[] ids = upIds.split("-");
					for(String splitId : ids){
						if(splitId != null && !splitId.equals("")){
							idList.add(Integer.parseInt(splitId));
						}
					}
				}
				CustomerVo cus = new CustomerVo();
				cus.id = id;
				cus.totalRobotNum = totalRobotNum;
				cus.upIds = idList;
				id2CustomerMap.put(cus.id, cus);
			}
			
			// 加起来
			for(CustomerVo cuVo : id2CustomerMap.values()){
				List<Integer> idl = cuVo.upIds;
				int lastId = cuVo.id;
				for(int id : idl){
					CustomerVo lastVo = id2CustomerMap.get(lastId);
					CustomerVo upVo = id2CustomerMap.get(id);
					upVo.totalRobotNum = lastVo.totalRobotNum + upVo.totalRobotNum;
					String sql2 = "UPDATE usr_business_customer t1 SET t1.ROBOT_TOTAL_NUM=? WHERE t1.ID=?";
					
					PreparedStatement st = con.prepareStatement(sql2);
					st.setInt(1, upVo.totalRobotNum);
					st.setInt(2, upVo.id);
					st.execute();
					lastId = lastVo.id; 
				}
			}
			// ============================================先查出所有玩家==========================================
			// 关闭连接
			stmt.close();
			con.close();
			System.out.println("======================处理完毕dealOutter=============================" + dealnum);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("sqlexception :" + ex.getMessage());
			System.err.println("sql :" + sql);
			System.err.println("dealnum :" + dealnum);
		}
	}
	
	static class CustomerVo{
		int id;
		List<Integer> upIds = new ArrayList();
		int totalRobotNum;
	}
	

	public static void dealmysql() {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://localhost:3306/gwdb_fake";
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
			con = DriverManager.getConnection(urlstr, "root", "123456");
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
						updateSql = "update player set nickname='" + nickName + repeatCount + "' where id=" + id;
					} else {
						updateSql = "update player set nickname='" + nickName + "' where id=" + id;
					}
				} else {
					updateSql = "update player set nickname='" + nickName + "' where id=" + id;
				}
				con.createStatement().executeUpdate(updateSql);
				dealnum++;
			}
			// ============================================先查出所有玩家==========================================
			// 关闭连接
			stmt.close();
			con.close();
			System.out.println("======================处理完毕dealNum=============================" + dealnum);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("sqlexception :" + ex.getMessage());
			System.err.println("sql :" + sql);
			System.err.println("dealnum :" + dealnum);
		}
	}

	public static void insertCityCode() throws Exception {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://localhost:3306/gwdb_fake";
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
		File file = new File("D://citycode.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temString = null;
		try {
			TreeMap<Integer, String> map = new TreeMap<Integer, String>();
			con = DriverManager.getConnection(urlstr, "root", "123456");
			while ((temString = reader.readLine()) != null) {
				// 显示行号
				String[] code2name = temString.split(" ");
				String code = code2name[0];
				String name = code2name[1];
				map.put(Integer.parseInt(code), name);
				// ============================================先查出所有玩家==========================================
				// 关闭连接
			}
			for (Entry<Integer, String> entry : map.entrySet()) {
				sql = "insert into city_code(code,name,province_code) values(" + entry.getKey() + ",'"
						+ entry.getValue() + "',0)";
				Statement st = con.createStatement();
				st.execute(sql);
			}
			con.close();
			System.out.println("======================处理完毕dealNum=============================" + dealnum);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("sqlexception :" + ex.getMessage());
			System.err.println("sql :" + sql);
			System.err.println("dealnum :" + dealnum);
		}
	}

	public static void setCity2ProvinceCode() throws Exception {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://localhost:3306/gwdb_fake";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(urlstr, "root", "123456");
		String sql = "select city.name,province.name from t_m_city city, t_m_provincial province where city.pid = province.id";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		// 查询城市映射
		Map<String, List<String>> province2cityList = new HashMap<String, List<String>>();
		Map<String, String> city2Province = new HashMap<String, String>();
		while (rs.next()) {
			String city = rs.getString(1);
			String province = rs.getString(2);
			List<String> cityList = province2cityList.get(province);
			if (cityList == null) {
				cityList = new ArrayList();
				province2cityList.put(province, cityList);
			}
			if (!cityList.contains(city)) {
				cityList.add(city);
			}
			city2Province.put(city, province);
		}
		// 查询省code;
		sql = "select * from province_code";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		// 查询城市映射
		Map<String, Integer> provinceName2Id = new HashMap<String, Integer>();
		while (rs.next()) {
			int provinceId = rs.getInt(1);
			String name = rs.getString(3);
			provinceName2Id.put(name, provinceId);
		}
		// 遍历赋值
		for (Entry<String, String> entry : city2Province.entrySet()) {
			String city = entry.getKey();
			String province = entry.getValue();
			if (provinceName2Id.containsKey(province)) {
				int provinceId = provinceName2Id.get(province);
				String updateSql = "update city_code set province_code = " + provinceId + " where name='" + city + "';";
				stmt.execute(updateSql);
			}
		}
		con.close();
		System.out.println("======================处理完毕dealNum=============================" + dealnum);
	}

	public static void insertCityLevel() throws Exception {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://localhost:3306/gwdb_fake";
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
		File file = new File("D://city_level.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temString = null;
		try {
			con = DriverManager.getConnection(urlstr, "root", "123456");
			int currentLevel = 0;
			while ((temString = reader.readLine()) != null) {
				// 显示行号
				try {
					currentLevel = Integer.parseInt(temString);
				} catch (Exception e) {
					sql = "update city_code set city_level = " + currentLevel + " where city_name='" + temString + "'";
					Statement st = con.createStatement();
					st.execute(sql);
				}
			}
			con.close();
			System.out.println("======================处理完毕dealNum=============================" + dealnum);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("sqlexception :" + ex.getMessage());
			System.err.println("sql :" + sql);
			System.err.println("dealnum :" + dealnum);
		}
	}

	public static void randRobot() throws Exception {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://localhost:3306/gwdb_fake";
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
		File file = new File("D://province_num.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temString = null;

		Random rand = new Random();
		float dscendRate = 0.56f;
		try {
			// ===================================检查省份是否存在=========================================
			Map<Integer, Integer> provinceId2Num = new HashMap();
			con = DriverManager.getConnection(urlstr, "root", "123456");
			int currentLevel = 0;
			int line = 0;
			int total = 0;
			while ((temString = reader.readLine()) != null) {
				String[] provinceName2Num = temString.split(" ");
				String provinceName = provinceName2Num[0];
				int num = (int) (Integer.parseInt(provinceName2Num[1]) * dscendRate);
				sql = "select id,province_name from province_code where province_name = '" + provinceName + "'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				String name = "";
				while (rs.next()) {
					int id = rs.getInt(1);
					name = rs.getString(2);
					provinceId2Num.put(id, num);
				}
				if (name == null || name.equals("")) {
					System.err.println("========>>>" + provinceName + "数据库为空line" + line);
					System.exit(0);
				}
				line++;
				total += num;
			}
			System.err.println("总机器数  =======>>>>>> " + total);
			// ================================ 生成【省份-城市列表】
			// =================================
			Map<Integer, List<City>> province2CitysMap = new HashMap<Integer, List<City>>();
			Map<Integer, String> provinceId2Name = new HashMap<Integer, String>();
			Map<Integer, String> cityId2Name = new HashMap<Integer, String>();
			sql = "select province.id, city.id, city.city_level,province.province_name,city.city_name,city.city_code from city_code city, province_code province where city.province_id = province.id";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				int provinceId = rs.getInt(1);
				int cityId = rs.getInt(2);
				int cityLevel = rs.getInt(3);
				String provinceName = rs.getString(4);
				String cityName = rs.getString(5);
				int cityCode = rs.getInt(6);
				List<City> cityList = province2CitysMap.get(provinceId);
				if (cityList == null) {
					cityList = new ArrayList();
					province2CitysMap.put(provinceId, cityList);
				}
				City city = new City();
				city.id = cityId;
				city.level = cityLevel;
				city.cityCode = cityCode;
				cityList.add(city);

				provinceId2Name.put(provinceId, provinceName);
				cityId2Name.put(cityId, cityName);
			}
			// ================================ 生成机器人
			// =========================================
			String mac = "";
			String serialNo = mac;
			final int status = 1;
			final String robotVersion = "";
			final long updateTime = 0;
			final long createTime = 0;
			final int sex = 0;
			final int brandId = 0;
			final String brandIame = "";
			final String ip = "";
			String province = "";
			int provinceCode = 0;
			String city = "";
			int cityCode = 0;
			final String stree = "";
			final String streetNumber = "";
			int onlineStatus = 0;
			final int restPower = 0;
			// ================================= 按城市等级生成
			// ===================================
			for (Entry<Integer, Integer> entry : provinceId2Num.entrySet()) {
				int provinceId = entry.getKey();
				if (provinceId == 22) {
					System.out.println();
				}
				int num = entry.getValue();
				// 拿出20%的浮动区间
				float floatRangeRate = 0.2f;
				int floatNum = (int) (num * floatRangeRate);
				num = num - floatNum;
				List<City> cityList = province2CitysMap.get(provinceId);
				// 至少3个城市吧
				int cityCount = rand.nextInt(5) + 3;
				if (cityCount > cityList.size()) {
					cityCount = cityList.size();
				}
				// 从城市等级大到小排序
				Collections.sort(cityList, City.copare);
				int totalLevel = 0;
				for (int i = 0; i < cityCount; i++) {
					totalLevel += (6 - cityList.get(i).level);
				}
				// 给城市赋值数量,现在就按占比比例吧
				for (int i = 0; i < cityCount; i++) {
					City cityTmp = cityList.get(i);
					// level越低，占有数应该越高, 【1-5】级区间范围
					int countLevel = 6 - cityTmp.level;
					float rate = countLevel * 1.0f / totalLevel;
					int insertNum = (int) (num * rate);
					// 加上浮动区间
					if (floatNum > 0) {
						int addNum = rand.nextInt(floatNum);
						insertNum += addNum;
						floatNum -= addNum;
					}
					// 剩下的全加上去
					if (i == cityCount - 1) {
						insertNum += floatNum;
					}
					// 插入N台机器
					for (int j = 0; j < insertNum; j++) {

						mac = System.currentTimeMillis() + "" + rand.nextInt(9999);
						serialNo = mac;
						province = provinceId2Name.get(provinceId);
						provinceCode = provinceId;
						city = cityId2Name.get(cityTmp.id);
						cityCode = cityTmp.cityCode;

						System.out.println("====province:" + province + "|city:" + city + "|insertNum:" + insertNum
								+ "|total:" + num);
						String sql2 = "insert into fake_robot_info_xb(mac,serial_no,status,robot_version,version_update_time,create_time,sex,brand_id,brand_name,ip,province,province_id,city,city_code,street,street_number,online_status,rest_power) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						sql2 = "";
						sql2 += "insert into fake_robot_info_xb(mac,serial_no,status,robot_version,version_update_time,create_time,sex,brand_id,brand_name,ip,province,province_id,city,city_code,street,street_number,online_status,rest_power) values(";
						sql2 += "'" + mac + "',";
						sql2 += "'" + serialNo + "',";
						sql2 += "" + status + ",";
						sql2 += "'" + robotVersion + "',";
						sql2 += "" + updateTime + ",";
						sql2 += "" + createTime + ",";
						sql2 += "" + sex + ",";
						sql2 += "" + brandId + ",";
						sql2 += "'" + brandIame + "',";
						sql2 += "'" + ip + "',";
						sql2 += "'" + province + "',";
						sql2 += "" + provinceCode + ",";
						sql2 += "'" + city + "',";
						sql2 += "" + cityCode + ",";
						sql2 += "'" + stree + "',";
						sql2 += "'" + streetNumber + "',";
						sql2 += "" + onlineStatus + ",";
						sql2 += "" + restPower + ")";

						Statement ps = con.createStatement();
						// System.err.println(sql2);
						// ps.setString(1, mac);
						// ps.setString(2, serialNo);
						// ps.setInt(3, status);
						// ps.setString(4, robotVersion);
						// ps.setLong(5, updateTime);
						// ps.setLong(6, createTime);
						// ps.setInt(7, sex);
						// ps.setInt(8, brandId);
						// ps.setString(9, brandIame);
						// ps.setString(10, ip);
						// ps.setString(11, province);
						// ps.setInt(12, provinceCode);
						// ps.setString(13, city);
						// ps.setInt(14, cityCode);
						// ps.setString(15, stree);
						// ps.setString(16, streetNumber);
						// ps.setInt(17, onlineStatus);
						// ps.setInt(18, restPower);
						ps.execute(sql2);
					}
				}
			}
			con.close();
			System.out.println("======================处理完毕dealNum=============================" + dealnum);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("sqlexception :" + ex.getMessage());
			System.err.println("sql :" + sql);
			System.err.println("dealnum :" + dealnum);
		}
	}

	public static void randomUpdateOnline() throws Exception {
		// update fake_robot_info_xb set online_status = (CASE (RAND() * 100) >
		// 80 then 0 else 1 end);
	}

	public static void insertSdkCode() throws Exception {
		int dealnum = 0;
		String urlstr = "jdbc:mysql://localhost:3306/gwsdk_config";
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
		File file = new File("D://sdk/");
		File[] files = file.listFiles();
		for (File txt : files) {
			BufferedReader reader = new BufferedReader(new FileReader(txt));
			String temString = null;
			try {
				con = DriverManager.getConnection(urlstr, "root", "123456");
				int currentLevel = 0;
				while ((temString = reader.readLine()) != null) {
					// 显示行号
					try {
						if (temString == null || temString.trim().equals("")) {
							continue;
						}
						String[] infos = temString.split(",");
						String code = infos[0];
						String names = infos[1];
						int codeId = Integer.parseInt(infos[2]);

						if (txt.getName().contains("Action_Light")) {
							sql = "INSERT INTO act_config(act_id,act_code,act_name,create_time,update_time)"
									+ " VALUE(?, ?, ?, now(), now()) ON DUPLICATE KEY UPDATE act_code=?,act_name = ?";

						} else if (txt.getName().contains("Attr_Light")) {
							sql = "INSERT INTO attr_config(attr_id,attr_code,attr_name,create_time,update_time)"
									+ " VALUE(?, ?, ?, now(), now()) ON DUPLICATE KEY UPDATE attr_code=?,attr_name = ?";
						} else if (txt.getName().contains("AttrValue_Light")) {
							sql = "INSERT INTO attr_value_config(attr_value_id,attr_value_code,attr_value_name,create_time,update_time)"
									+ " VALUE(?, ?, ?, now(), now()) ON DUPLICATE KEY UPDATE attr_value_code=?,attr_value_name = ?";
						} else if (txt.getName().contains("AttrValueType_Light")) {
							sql = "INSERT INTO attr_value_type_config(attr_value_type_id,attr_value_type_code,attr_value_type_name,create_time,update_time)"
									+ " VALUE(?, ?, ?, now(), now()) ON DUPLICATE KEY UPDATE attr_value_type_code=?,attr_value_type_name = ?";
						} else if (txt.getName().contains("Mode_Light")) {
							sql = "INSERT INTO mode_config(mode_id,mode_code,mode_name,create_time,update_time)"
									+ " VALUE(?, ?, ?, now(), now()) ON DUPLICATE KEY UPDATE mode_code=?,mode_name = ?";
						}
						PreparedStatement st = con.prepareStatement(sql);
						st.setInt(1, codeId);
						st.setString(2, code);
						st.setString(3, names);
						st.setString(4, code);
						st.setString(5, names);
						st.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				con.close();
				System.out.println("======================处理完毕dealNum=============================" + dealnum);
			} catch (SQLException ex) {
				ex.printStackTrace();
				System.err.println("sqlexception :" + ex.getMessage());
				System.err.println("sql :" + sql);
				System.err.println("dealnum :" + dealnum);
			}
		}
	}
}

class City {

	public static Comparator<City> copare = new Comparator<City>() {

		@Override
		public int compare(City o1, City o2) {
			return o1.level - o2.level;
		}
	};

	public int id;
	public int level;
	public int cityCode;

	@Override
	public String toString() {
		return "id:" + id + " | level:" + level + " | cityCode : " + cityCode;
	}
}
