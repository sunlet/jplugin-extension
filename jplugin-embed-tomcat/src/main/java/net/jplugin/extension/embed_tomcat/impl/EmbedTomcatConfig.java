package net.jplugin.extension.embed_tomcat.impl;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class EmbedTomcatConfig {
	//是否支持Web
	private static final String USE_WEB_SUPPORT = "embed-tomcat.use-web-support";
	//web端口号
	private static final String TOMCAT_PORT="embed-tomcat.context-port";
	
	//context name。 http://aaaaa:8081/[context name]/aaa/bbb.do
	//默认为""
	private static final String CONTEXT_NAME="embed-tomcat.context-name";
	
	private static Integer tomcatPort;
	private static String contextName;
	private static boolean useWebSupport;
	
	public static void init() {
		tomcatPort = ConfigFactory.getIntConfig(TOMCAT_PORT, 8080);
		
		useWebSupport = !"false".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim(USE_WEB_SUPPORT));
		
		contextName = ConfigFactory.getStringConfigWithTrim(CONTEXT_NAME);
		if (StringKit.isNull(contextName)) contextName = "";
		
		StringBuffer sb = new StringBuffer();
		sb.append("$$$ Embed Tomcat config:\n tomcatPort="+tomcatPort)
		.append("\n useWebSupport:"+useWebSupport)
		.append("\n contextName:"+contextName);
		
		PluginEnvirement.getInstance().getStartLogger().log(sb.toString());
	}
	
	public static Integer getTomcatPort() {
		return tomcatPort;
	}
	
//	public static String getWebAppDir() {
//		return webAppDir;
//	}
	public static boolean isUseWebSupport() {
		return useWebSupport;
	}
	
	public static String getContextName() {
		return contextName;
	}
}
