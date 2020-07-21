package net.jplugin.extension.embed_tomcat.impl;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class EmbedTomcatConfig {
    //是否支持Web
    private static final String USE_WEB_SUPPORT = "embed-tomcat.use-web-support";
    //web端口号
    private static final String TOMCAT_PORT = "embed-tomcat.context-port";

    //context name。 http://aaaaa:8081/[context name]/aaa/bbb.do
    //默认为""
    private static final String CONTEXT_NAME = "embed-tomcat.context-name";


    private static final String PROTOCOL_COMPRESSABLE_MIME_TYPE = "embed-tomcat.protocol.compressable-mime-type";


    private static final String PROTOCOL_MAX_THREADS = "embed-tomcat.protocol.max-threads";


    private static final String PROTOCOL_MAX_CONNECTIONS = "embed-tomcat.protocol.max-connections";

    private static final String PROTOCOL_CONNECTION_TIMEOUT = "embed-tomcat.protocol.connection-timeout";

    private static final String PROTOCOL_MIN_SPARE_THREADS = "embed-tomcat.protocol.min-spare-threads";

    private static final String PROTOCOL_KEEPALIVE_TIMEOUT = "embed-tomcat.protocol.keepalive-timeout";


    private static final String PROTOCOL_ACCEPTOR_THREADCOUNT = "embed-tomcat.protocol.acceptor-threadCount";

    private static final String REDIRECT_PORT = "embed-tomcat.redirect-port";

    private static final String URL_ENCODING = "embed-tomcat.uri-encoding";

    private static final String MAX_POST_SIZE = "embed-tomcat.max-post-size";

    private static final String PROTOCOL_COMPRESSION = "embed-tomcat.protocol.compression";


    private static Integer tomcatPort;
    private static String contextName;
    private static boolean useWebSupport;

    private static String uriEncoding;
    private static Integer maxThreads;
    private static Integer maxConnections;
    private static Integer connectionTimeout;
    private static Integer minSpareThreads;
    private static Integer keepaliveTimeout;
    private static Integer acceptorThreadCount;
    private static Integer redirectPort;
    private static String compressableMimeType;
    private static Integer maxPostSize;
    private static String compression;

    public static void init() {
        tomcatPort = ConfigFactory.getIntConfig(TOMCAT_PORT, 8080);

        useWebSupport = !"false".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim(USE_WEB_SUPPORT));

        contextName = ConfigFactory.getStringConfigWithTrim(CONTEXT_NAME);
        if (StringKit.isNull(contextName)) contextName = "";

        uriEncoding = ConfigFactory.getStringConfig(URL_ENCODING);
        maxThreads = ConfigFactory.getIntConfig(PROTOCOL_MAX_THREADS);

        maxConnections = ConfigFactory.getIntConfig(PROTOCOL_MAX_CONNECTIONS);
        connectionTimeout = ConfigFactory.getIntConfig(PROTOCOL_CONNECTION_TIMEOUT);
        minSpareThreads = ConfigFactory.getIntConfig(PROTOCOL_MIN_SPARE_THREADS);
        keepaliveTimeout = ConfigFactory.getIntConfig(PROTOCOL_KEEPALIVE_TIMEOUT);
        acceptorThreadCount = ConfigFactory.getIntConfig(PROTOCOL_ACCEPTOR_THREADCOUNT);

        redirectPort = ConfigFactory.getIntConfig(REDIRECT_PORT);

        compressableMimeType = ConfigFactory.getStringConfigWithTrim(PROTOCOL_COMPRESSABLE_MIME_TYPE);

        maxPostSize = ConfigFactory.getIntConfig(MAX_POST_SIZE);

        compression= ConfigFactory.getStringConfigWithTrim(PROTOCOL_COMPRESSION);

        StringBuffer sb = new StringBuffer();
        sb.append("$$$ Embed Tomcat config:\n tomcatPort=" + tomcatPort)
                .append("\n useWebSupport:" + useWebSupport)
                .append("\n contextName:" + contextName)
				.append("\n maxThreads:" + maxThreads)
				.append("\n maxConnections:" + maxConnections)
				.append("\n connectionTimeout:" + connectionTimeout);


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

    public static Integer getMaxThreads() {
        return maxThreads;
    }

    public static Integer getMaxConnections() {
        return maxConnections;
    }

    public static Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public static Integer getMinSpareThreads() {
        return minSpareThreads;
    }

    public static Integer getKeepaliveTimeout() {
        return keepaliveTimeout;
    }

    public static Integer getAcceptorThreadCount() {
        return acceptorThreadCount;
    }

    public static Integer getRedirectPort() {
        return redirectPort;
    }

    public static String getCompressableMimeType() {
        return compressableMimeType;
    }

    public static String getUriEncoding() {
        return uriEncoding;
    }

	public static Integer getMaxPostSize() {
		return maxPostSize;
	}

    public static String getCompression() {
        return compression;
    }
}
