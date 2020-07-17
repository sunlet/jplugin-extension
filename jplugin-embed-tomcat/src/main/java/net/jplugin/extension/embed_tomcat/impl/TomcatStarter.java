package net.jplugin.extension.embed_tomcat.impl;

import net.jplugin.common.kits.XMLKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.impl.PluginServlet;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;

public class TomcatStarter {

    public static Tomcat start() throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(EmbedTomcatConfig.getTomcatPort());

        //创建Context
        Context context = null;
        String ctxName = EmbedTomcatConfig.getContextName();

        if (!EmbedTomcatConfig.isUseWebSupport()) {
            context = tomcat.addContext(ctxName, null);
        } else {
            //use web support = true
            String webdir = PluginEnvirement.getInstance().getWebRootPath();
            checkRootPathValid(webdir);

            File file = new File(webdir);
            if (!file.exists() || !file.isDirectory()) {
                PluginEnvirement.getInstance().getStartLogger().log("$$$ Web dir is not exists, or not directory, JPlugin home: " + PluginEnvirement.getInstance().getWorkDir());
                context = tomcat.addContext(ctxName, null);
            } else {
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
     * <Connector port="8080" protocol="org.apache.coyote.http11.Http11NioProtocol"
     * connectionTimeout="20000" ok
     * URIEncoding="UTF-8"
     * useBodyEncodingForURI="false"
     * maxThreads="1000"  ok
     * minSpareThreads="100" ok
     * enableLookups="false"
     * redirectPort="8443"
     * acceptCount="800"
     * compression="on"
     * compressionMinSize="20480"
     * noCompressionUserAgents="gozilla, traviata"
     * maxPostSize="209715200"
     * compressableMimeType="text/html,text/xml,text/plain,text/javascript,text/css,application/octet-stream" />
     *
     * @param connector
     */
    private static void customConnector(Connector connector) {
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();


        //当用户用http请求某个资源，而该资源本身又被设置了必须要https方式访问，此时Tomcat会自动重定向到这个redirectPort设置的https端口。
        if (EmbedTomcatConfig.getRedirectPort() != null) {
            connector.setRedirectPort(EmbedTomcatConfig.getRedirectPort());
        }

        if (EmbedTomcatConfig.getUriEncoding() != null && !"".equals(EmbedTomcatConfig.getUriEncoding())) {
            connector.setURIEncoding(EmbedTomcatConfig.getUriEncoding());
        }


        // 设置最大线程数
        if (EmbedTomcatConfig.getMaxThreads() != null) {
            protocol.setMaxThreads(EmbedTomcatConfig.getMaxThreads());
        }
        //线程池中，保持活跃的线程的最小数量，默认为10。
        if (EmbedTomcatConfig.getMinSpareThreads() != null) {
            protocol.setMinSpareThreads(EmbedTomcatConfig.getMinSpareThreads());
        }


        // 设置超时时间  ,当client与tomcat建立连接之后,在"connectionTimeout"时间之内,仍然没有得到client的请求数据,此时连接将会被断开
        if (EmbedTomcatConfig.getConnectionTimeout() != null) {
            protocol.setConnectionTimeout(EmbedTomcatConfig.getConnectionTimeout());
        }

        //  tomcat允许接收和处理的最大链接数
        if (EmbedTomcatConfig.getMaxConnections() != null) {
            protocol.setMaxConnections(EmbedTomcatConfig.getMaxConnections());
        }
        //当无实际数据交互时，链接被保持的时间，单位：毫秒
        if (EmbedTomcatConfig.getKeepaliveTimeout() != null) {
            protocol.setKeepAliveTimeout(EmbedTomcatConfig.getKeepaliveTimeout());
        }

        //默认为1，表示用于accept新链接的线程个数，如果在多核CPU架构下，此值可以设置为2，官方不建议设定超过2个的值。
        if (EmbedTomcatConfig.getAcceptorThreadCount() != null) {
            protocol.setAcceptorThreadCount(EmbedTomcatConfig.getAcceptorThreadCount());
        }

        if(EmbedTomcatConfig.getMaxPostSize()!=null) {
			connector.setMaxPostSize(EmbedTomcatConfig.getMaxPostSize());
		}

        if (EmbedTomcatConfig.getCompressableMimeType() != null && !"".equals(EmbedTomcatConfig.getCompressableMimeType())) {
            protocol.setCompressibleMimeType(EmbedTomcatConfig.getCompressableMimeType());
        }

        //protocol.setNoCompressionUserAgents("gozilla, traviata");

    }

    /**
     * 判断webdir下面的web.XML，一定不能包含JPlugin的非嵌入式Servlet,也不能包含 *.do的定义
     *
     * @param webdir
     * @throws Exception
     */
    private static void checkRootPathValid(String webdir) throws Exception {
        String webxml = webdir + "/WEB-INF/web.xml";
        File file = new File(webxml);
        String servletName = PluginServlet.class.getName();

        if (file.exists()) {
            Document dom = XMLKit.parseFile(webxml);
            XMLKit.travelNode(dom.getDocumentElement(), (nd) -> {
                if (nd.getNodeType() == Node.TEXT_NODE) {
                    String temp = nd.getTextContent();
                    if (servletName.equals(temp) || "*.do".contentEquals(temp)) {
                        throw new RuntimeException("[net.jplugin.ext.webasic.impl.PluginServlet] servlet and [*.do] mapping must removed when useing embed tomcat!");
                    }
                }
            });
        }
    }
}
