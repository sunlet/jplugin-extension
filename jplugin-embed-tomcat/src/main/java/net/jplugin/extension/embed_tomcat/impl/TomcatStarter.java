package net.jplugin.extension.embed_tomcat.impl;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.jplugin.common.kits.XMLKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.impl.PluginServlet;

public class TomcatStarter {

	public static Tomcat start() throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(EmbedTomcatConfig.getTomcatPort());
		
		//创建Context
		Context context = null;
		String ctxName = EmbedTomcatConfig.getContextName();
		
		if (!EmbedTomcatConfig.isUseWebSupport()) {
			context = tomcat.addContext(ctxName,null);
		}else {
			//use web support = true
			String webdir = PluginEnvirement.getInstance().getWebRootPath();
			checkRootPathValid(webdir);
			
			File file = new File(webdir);
			if (!file.exists() || !file.isDirectory()) {
				PluginEnvirement.getInstance().getStartLogger().log("$$$ Web dir is not exists, or not directory, JPlugin home: "+PluginEnvirement.getInstance().getWorkDir());
				context = tomcat.addContext(ctxName,null);
			}else {
				PluginEnvirement.getInstance().getStartLogger().log("$$$ Start with static resource and jsp support, Web dir:" + webdir);
				context = tomcat.addWebapp(ctxName, webdir);
			}
		}
		//创建Servlet
		tomcat.addServlet(ctxName, "PluginServlet", new PluginServlet4Embed());
		context.addServletMappingDecoded("*.do", "PluginServlet");
		
		Connector connector = tomcat.getConnector();
		customConnector(connector);
		
		tomcat.start();
		
		return tomcat;
	}
/**
 * 
 * <Connector port="8080" protocol="org.apache.coyote.http11.Http11NioProtocol"
               connectionTimeout="20000"
               URIEncoding="UTF-8"
               useBodyEncodingForURI="false"
               maxThreads="1000"
               minSpareThreads="100"
               enableLookups="false"
               redirectPort="8443"
               acceptCount="800"
               compression="on"
               compressionMinSize="20480"
               noCompressionUserAgents="gozilla, traviata"
               maxPostSize="209715200"
               compressableMimeType="text/html,text/xml,text/plain,text/javascript,text/css,application/octet-stream" />
 * 
 * 
 * 
 * @param connector
 */
	private static void customConnector(Connector connector) {
	}

	/**
	 * 判断webdir下面的web.XML，一定不能包含JPlugin的非嵌入式Servlet,也不能包含 *.do的定义
	 * @param webdir
	 * @throws Exception 
	 */
	private static void checkRootPathValid(String webdir) throws Exception {
		String webxml = webdir +"/WEB-INF/web.xml";
		File file = new File(webxml);
		String servletName = PluginServlet.class.getName();
		
		if (file.exists()) {
			Document dom = XMLKit.parseFile(webxml);
			XMLKit.travelNode(dom.getDocumentElement(), (nd)->{
				if (nd.getNodeType()==Node.TEXT_NODE) {
					String temp = nd.getTextContent();
					if (servletName.equals(temp) || "*.do".contentEquals(temp)){
						throw new RuntimeException("[net.jplugin.ext.webasic.impl.PluginServlet] servlet and [*.do] mapping must removed when useing embed tomcat!");
					}
				}
			} );
		}
	}
}
