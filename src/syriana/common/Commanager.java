package syriana.common;

import java.util.concurrent.LinkedBlockingQueue;

import syriana.window.Main;

/**
 * 
 * 2016-2-18 下午7:26:33
 *
 * @author Syriana
 */
public class Commanager {
	
	private static Thread loggerThread;
	private static LinkedBlockingQueue<String> loggerQueue = new LinkedBlockingQueue<String>();
	private static String RES_ROOT_PATH = "syriana/resource";
	static {
		Runnable loggerAction = new Runnable() {
			@Override
			public void run() {
				try {
					while(true){
						String log = loggerQueue.take();
						Main.addLogger(log);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		loggerThread = new Thread(loggerAction);
		loggerThread.start();
	}

	public static String getRootPath() {
		return RES_ROOT_PATH;
	}
	
	public static void addLogger2Queue(String logger){
		loggerQueue.offer(logger);
	}
}
