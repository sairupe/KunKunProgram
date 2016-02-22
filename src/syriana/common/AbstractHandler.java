package syriana.common;

public abstract class AbstractHandler implements IHandler{
	
	/**
	 * 主类型
	 */
	protected MainType mainType;
	
	public AbstractHandler(){
		
	}
	
	public void setMainType(MainType mainType){
		this.mainType = mainType;
	}
	
	public MainType getMainType() {
		return mainType;
	}
	/**
	 * 获取主类型的值
	 */
	public int getMainTypeValue(){
		return mainType.getTypeValue();
	}
}
