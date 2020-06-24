package net.jplugin.extension.reqrecoder.impl;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.StringMatcher;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.extension.reqrecoder.api.AbstractReqLoggerHandler;

public class LogRecorderConfig {
	//是否启用该记录服务：默认false
	private static final String ENABLE = "request-recorder.enable";
	
	//记录哪些URL格式的日志。不配置表示记录所有，配置了只记录匹配的
	private static final String URL_PATTERN = "request-recorder.url-pattern";
	
	//抽样记录比例，默认全部记录
	private static final String SAMPLING = "request-recorder.sampling";
	
	//使用哪个日志记录处理器。默认值：com.haiziwang.platform.reqrecoder.impl.FileReqLoggerHandler，记录到req-log-recorder.log
	//配置了则使用配置值
	private  static final String REQ_LOG_HANDLER_CLAZZ = "request-recorder.log-handler-clazz";
	
	//是否记录Header信息，默认值为false
	private static final String IS_LOG_HEADER = "request-recorder.is-log-header";
	//记录哪些Header,默认记录所有
	private static final String HEANDERS_INCLUDE = "request-recorder.headers-include";
	//排除哪些Header,默认不做排除
	private static final String HEADERS_EXCLUDE = "request-recorder.headers-exclude";
	
	//是否记录Cookie，默认值为false
	private static final String IS_LOG_COOKIES = "request-recorder.is-log-cookie";
	//记录那些Cookie,默认记录所有
	private static final String COOKIES_INCLUDE = "request-recorder.cookies-include";
	//排除哪些Cookie,默认不做排除
	private static final String COOKIES_EXCLUDE = "request-recorder.cookies-exclude";
	//执行时间下限
	private static final String EXECUTE_TIME_BOTTOM = "request-recorder.execute_time_bottom";
	
	private static final String ERROR_MESSAGE_SIZE = "request-recorder.error_message_size";
	
	
	public static boolean enable;
	public static StringMatcher urlMatcher;
	
	public static boolean logHeader;
	public static StringMatcher headerIncludeMatcher;
	public static StringMatcher headerExcludeMatcher;

	public static boolean logCookie;
	public static StringMatcher cookieIncludeMatcher;
	public static StringMatcher cookieExcludeMatcher;
	
	public static AbstractReqLoggerHandler reqLogHandler;

	public static Double simpling;
	public static Long executeTimeBottom;
	
	public static Integer errorMsgSize;
	
	

	private static Object getConfigString() {
		StringBuffer sb = new StringBuffer("$$$ LogRecorderConfig :");
		if(enable){
		sb.append("\n enable:"+enable)
		.append("\n urlMatcher:"+urlMatcher)
		.append("\n logHeader:"+logHeader)
		.append("\n headerIncludeMatcher:"+headerIncludeMatcher)
		.append("\n logCookie:").append(logCookie)
		.append("\n cookieIncludeMatcher:").append(cookieIncludeMatcher)
		.append("\n cookieExcludeMatcher:").append(cookieExcludeMatcher)
		.append("\n simpling:").append(simpling)
		.append("\n executeTimeBottom:").append(executeTimeBottom)
		.append("\n errorMsgSize:").append(errorMsgSize);
		}else {
			sb.append("enable:"+enable);
		}
		return sb.toString();
	}
	
	/**
	 * 启动时调用一次，config变化一次，就会调用一次
	 */
	public static void init(){
		enable = "true".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim(ENABLE));
		if (!enable){
			PluginEnvirement.INSTANCE.getStartLogger().log(getConfigString());
			return;
		}
		try {
			String temp = ConfigFactory.getStringConfigWithTrim(SAMPLING);
			if (!StringKit.isNull(temp)) {
				simpling = Double.parseDouble(temp);
			}else {
				simpling = null;
			}
			
			temp = ConfigFactory.getStringConfigWithTrim(EXECUTE_TIME_BOTTOM);
			if (!StringKit.isNull(temp)) {
				executeTimeBottom = Long.parseLong(temp);
			}else {
				executeTimeBottom = null;
			}
			
			temp = ConfigFactory.getStringConfigWithTrim(ERROR_MESSAGE_SIZE);
			if (!StringKit.isNull(temp)) {
				errorMsgSize = Integer.valueOf(temp);
			}else {
				errorMsgSize = null;
			}
			
			String clazz = ConfigFactory.getStringConfigWithTrim(REQ_LOG_HANDLER_CLAZZ);
			if (StringKit.isNull(clazz)){
				clazz = FileReqLoggerHandler.class.getName();
			}
			try {
				reqLogHandler = (AbstractReqLoggerHandler) Class.forName(clazz).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("can't init logg handler:"+clazz,e);
			}
			
			String url_patterns = ConfigFactory.getStringConfigWithTrim(URL_PATTERN);
			urlMatcher  = StringKit.isNotNull(url_patterns)?new StringMatcher(url_patterns):null; 
			
			String isLogHeader = ConfigFactory.getStringConfigWithTrim(IS_LOG_HEADER);
			String headersInclude = ConfigFactory.getStringConfigWithTrim(HEANDERS_INCLUDE);
			String headersExclude = ConfigFactory.getStringConfigWithTrim(HEADERS_EXCLUDE);
			String isLogCookie = ConfigFactory.getStringConfigWithTrim(IS_LOG_COOKIES);
			String cookiesInclude = ConfigFactory.getStringConfigWithTrim(COOKIES_INCLUDE);
			String cookiesExclude = ConfigFactory.getStringConfigWithTrim(COOKIES_EXCLUDE);
	
			logHeader = "true".equalsIgnoreCase(isLogHeader);
			headerIncludeMatcher =StringKit.isNotNull(headersInclude)? new StringMatcher(headersInclude): null;  
			headerExcludeMatcher = StringKit.isNotNull(headersExclude)? new StringMatcher(headersExclude) : null;
	
			logCookie = "true".equalsIgnoreCase(isLogCookie);
			cookieIncludeMatcher =StringKit.isNotNull(cookiesInclude)? new StringMatcher(cookiesInclude): null;  
			cookieExcludeMatcher = StringKit.isNotNull(cookiesExclude)? new StringMatcher(cookiesExclude) : null;
			
			PluginEnvirement.INSTANCE.getStartLogger().log(getConfigString());
		}catch(Exception e) {
			enable = false;
			PluginEnvirement.INSTANCE.getStartLogger().log("Exception when init req log config",e);
		}
	}
}
