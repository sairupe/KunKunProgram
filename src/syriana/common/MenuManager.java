package syriana.common;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import syriana.util.XmlUtils;

public class MenuManager {
	
	public static boolean isDebug = false;
	
	private static MenuManager instance = new MenuManager();
	/**
	 * 主类型对应子类型列表
	 */
	private Map<MainType, List<SubType>> mainType2SubType = new LinkedHashMap<MainType, List<SubType>>();
	/**
	 * 子类型是否是选择目录
	 */
	private Map<SubType, Boolean> subType2ChooseFile = new LinkedHashMap<SubType, Boolean>();
	/**
	 * 主类型对应的Handler
	 */
	private Map<Integer, AbstractHandler> mainType2Handler = new HashMap<Integer, AbstractHandler>();
	public void initMenu() throws Exception{
		
		Document doc;
		if(!isDebug){
			// 加载科室配置
			InputStream stream = MenuManager.class.getClassLoader().getResourceAsStream(Commanager.getRootPath() + "/menu/menu.xml");
			doc = XmlUtils.load(stream);
		}
		else{
			InputStream stream = MenuManager.class.getClassLoader().getResourceAsStream("syriana/resource/menu/menu.xml");
			doc = XmlUtils.load(stream);
		}
		
		Element config = doc.getDocumentElement();
		Element[] mainNodeList = XmlUtils.getChildrenByName(config, "main");
		for(Element mainNode : mainNodeList){
			Element[] subNodeList = XmlUtils.getChildrenByName(mainNode, "sub");
			int mainId = Integer.parseInt(mainNode.getAttribute("id"));
			String clazz = mainNode.getAttribute("clazz");
			MainType main = MainType.getMainTypeById(mainId);
			AbstractHandler handler = (AbstractHandler) Class.forName(clazz).newInstance();
			handler.setMainType(main);
			handler.init();
			mainType2Handler.put(mainId, handler);
			List<SubType> subList= new ArrayList<SubType>();
			mainType2SubType.put(main, subList);
			for(Element subNode : subNodeList){
				int subId = Integer.parseInt(subNode.getAttribute("id"));
				boolean isDirector = Boolean.parseBoolean(subNode.getAttribute("isDirector"));
				SubType subType = SubType.getSubTypeById(subId);
				subType.setIsChooseDirector(isDirector);
				subList.add(subType);
			}
		}
		System.out.println();
	}
	
	public static MenuManager getInstance(){
		return instance;
	}
	
	public List<SubType> getSubListByMainType(MainType mainType){
		return mainType2SubType.get(mainType);
	}
	
	public List<MainType> getMainTypeList(){
		List<MainType> arrayList = new ArrayList<MainType>();
		arrayList.addAll(mainType2SubType.keySet());
		return arrayList;
	}
	
	public boolean isChooseDirectorBySubType(SubType subType){
		return subType2ChooseFile.get(subType);
	}
	
	public AbstractHandler getHandlerByMainId(int mainId){
		return mainType2Handler.get(mainId);
	}
}
