package syriana.common;

/**
 * 
 * 2016-2-16 下午1:49:37
 *
 * @author Syriana
 */
public interface IHandler {
	public void disPatch(int subType, String path) throws Exception;
	public void init() throws Exception ;
}
