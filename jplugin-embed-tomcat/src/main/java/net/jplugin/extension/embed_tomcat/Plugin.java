package net.jplugin.extension.embed_tomcat;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.extension.embed_tomcat.impl.EmbedTomcatConfig;
import net.jplugin.extension.embed_tomcat.impl.TomcatController;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

	public Plugin() {
		this.searchAndBindExtensions();
	}
	
	@Override
	public void onCreateServices() {
		EmbedTomcatConfig.init();
	}
	
	@Override
	public void init() {
		TomcatController.startTomcat();
		for (int i=0;i<240;i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Boolean result = TomcatController.getStartResult();
			if (result !=null) {
				if (result) {
					PluginEnvirement.getInstance().getStartLogger().log("$$$ 内置Tomcat 启动完毕!");
					return;
				}else {
					throw new RuntimeException("内置Tomcat 启动失败！");
				}
			}
		}
		throw new RuntimeException("Tomcat 启动超时！");
		
//		try {
//			JPluginTomcat.start();
//		} catch (Exception e) {
//			throw new RuntimeException("启动内置Tomcat异常",e);
//		}
	}

	
	@Override
	public void onDestroy() {
		TomcatController.stopTomcat();
	}
	
	@Override
	public int getPrivority() {
		return -2;
	}
}
