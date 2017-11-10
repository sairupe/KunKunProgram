package syriana.common;

public enum MainType {
	Test(99999, "测试，请不要选择"),
	DinnerFee(10010,"夜餐费"),
	General(10020,"EXCEL通用"),
	General2(10030,"合并2006-2007餐补"),
	General3(10040,"合并5-9餐补");
	
	/**
	 * 类型
	 */
	int type;
	/**
	 * 名字
	 */
	String name;
	MainType(int type, String name){
		this.type = type;
		this.name = name;
	}
	
	/**
	 * 获取主类型的值
	 */
	public int getTypeValue(){
		return type;
	}
	/**
	 * 获取类型名字
	 */
	public String getTypeName(){
		return name;
	}
	
	/**
	 * 根据ID获取主类型
	 */
	public static MainType getMainTypeById(int id){
		MainType[] types = MainType.values();
		for(MainType type : types){
			if(type.getTypeValue() == id){
				return type;
			}
		}
		return Test;
	}
}
