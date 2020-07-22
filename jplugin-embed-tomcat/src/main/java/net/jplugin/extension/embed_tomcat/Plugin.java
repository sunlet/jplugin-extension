package net.jplugin.extension.embed_tomcat;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
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
		return CoreServicePriority.OFFSET_FOR_EXTENSION +1;
	}
}
