package net.jplugin.extension.reqrecoder.kit;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.StringMatcher;
import net.jplugin.common.kits.http.ContentKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.api.ctx.RequesterInfo.Content;
import net.jplugin.ext.webasic.api.HttpFilterContext;
import net.jplugin.extension.reqrecoder.api.LogRecorder;
import net.jplugin.extension.reqrecoder.impl.LogRecorderConfig;

public class LogRecorderHelper{

	public static LogRecorderHelper me = new LogRecorderHelper();
	
	
	public LogRecorder create(HttpFilterContext ctx, Throwable exception, long timeStart, long timeEnd) {
		LogRecorder o = new LogRecorder();
		HttpServletRequest req = ctx.getRequest();
		o.setUrl(req.getRequestURL().toString());
		
		String method = req.getMethod();
		o.setMethod(method);
		RequesterInfo reqinfo = ThreadLocalContextManager.getRequestInfo();
		if (LogRecorder.POST.equals(method) || LogRecorder.PUT.equals(method)) {
			//method 为 POST PUT 
			Content reqContent = reqinfo.getContent();
			if (ContentKit.isApplicationJson(reqContent.getContentType())) {
				o.setIsJsonRequest(true);
				o.setJson(reqContent.getJsonContent());
			}else {
				o.setIsJsonRequest (false);
				o.setParams(reqContent.getParamContent());
			}
		}else {
			//method不为 POST,PUT 时, 一般为GET
			o.setQueryString (req.getQueryString());
		}
		
		o.setIp(reqinfo.getCallerIpAddress());
		o.setCookies(LogRecorderHelper.me.getCookies(req.getCookies()));
		o.setHeaders(LogRecorderHelper.me.getHeaders(req));
		o.setExecuteTime( (int)(timeEnd - timeStart));
		
		if (exception!=null) {
			String msg = exception.getMessage();
			//防止空指针
			if (StringKit.isNull(msg)) 
				msg = "ERROR";
			if (LogRecorderConfig.errorMsgSize!=null) {
				msg = msg.substring(0,LogRecorderConfig.errorMsgSize);
			}
			o.setErrorMsg(msg);
		}
		
		return o;
	}
	
	private Map<String, String> getHeaders(HttpServletRequest req) {
		if (!LogRecorderConfig.logHeader)
			return null;
		Enumeration<String> names = req.getHeaderNames();
		if (names==null)
			return null;
		
		Map<String,String> m = new HashMap<String,String>();
		while(names.hasMoreElements()){
			String name = names.nextElement();
			if (checkMatch(LogRecorderConfig.headerIncludeMatcher, LogRecorderConfig.headerExcludeMatcher, name))
				m.put(name, req.getHeader(name));
		}
		return m;
	}
	
	private  Map<String,String> getCookies(Cookie[] ck) {
		if (ck==null) return null;
		if (!LogRecorderConfig.logCookie) return null;
		
		Map<String,String> m = new HashMap<String,String>();
		for (Cookie c : ck) {
			if (checkMatch(LogRecorderConfig.cookieIncludeMatcher, LogRecorderConfig.cookieExcludeMatcher, c.getName()))
				m.put(c.getName(), c.getValue());
		}
		return m;
	}

	private boolean checkMatch(StringMatcher inc, StringMatcher exc,String s) {
		return (inc==null || (inc!=null && inc.match(s))
				&&
				(exc==null || (exc!=null && !exc.match(s))));

	}
}
