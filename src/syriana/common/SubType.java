package syriana.common;

public enum SubType {
	Test(99999, "测试，请不要选择"),
	DinnerFeeDefault(10011, "各科室夜餐费");
	/**
	 * 子类型
	 */
	int subType;
	/**
	 * 名字
	 */
	String name;
	/**
	 * 是否为目录
	 */
	boolean isChooseDirector;
	
	SubType(int subType, String name){
		this.subType = subType;
		this.name = name;
	}
	
	/**
	 * 获取主类型的值
	 */
	public int getTypeValue(){
		return subType;
	}
	/**
	 * 获取类型名字
	 */
	public String getTypeName(){
		return name;
	}
	/**
	 * 获取是否为目录
	 */
	public boolean isChooseDirector(){
		return isChooseDirector;
	}
	/**
	 * 设置是否为目录
	 */
	public void setIsChooseDirector(boolean isDirector){
		this.isChooseDirector = isDirector;
	}
	
	/**
	 * 根据ID获取主类型
	 */
	public static SubType getSubTypeById(int id){
		SubType[] types = SubType.values();
		for(SubType type : types){
			if(type.getTypeValue() == id){
				return type;
			}
		}
		return Test;
	}
}
