package net.jplugin.extension.embed_tomcat.impl;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.XMLKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.kits.ExecutorKit;
import net.jplugin.ext.webasic.impl.PluginServlet;

public class TomcatController {

	static Boolean startResult;
	static Thread tomcatThread;
	static Tomcat tomcat;
	public static void startTomcat() {
		startResult = null;
		tomcat = null;
		tomcatThread = null;
		
		tomcatThread = new Thread("Embed Tomcat Main") {
			@Override
			public void run() {
				try {
					TomcatController.startAsyn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		tomcatThread.start();
	}
	
	public static void stopTomcat(){
		if (tomcat!=null) {
			try {
				tomcat.stop();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		if (tomcatThread!=null) {
			try{
				tomcatThread.interrupt();
			}catch(Exception e) {e.printStackTrace();}
		}
		startResult = null;
		tomcat = null;
		startResult = null;
	}
	
	public static Boolean getStartResult() {
		return startResult;
	}
	

	
	
	private static void startAsyn(){
		if (startResult!=null) {
			throw new RuntimeException("start error!");
		}
		try{
			tomcat = TomcatStarter.start();
			
			//ok 线程挂住
			startResult = true;
			tomcat.getServer().await();
		}catch(Exception e) {
			e.printStackTrace();
			startResult = false;
		}
	}
	

}
