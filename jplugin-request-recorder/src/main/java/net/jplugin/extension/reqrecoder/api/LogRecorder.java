package net.jplugin.extension.reqrecoder.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.http.ContentKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.RequesterInfo.Content;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.api.HttpFilterContext;

public class LogRecorder {
	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String PUT = "PUT";
	
	private String url;
	private String method;
	private String queryString;
	private boolean isJsonRequest;
	private String json;
	private String ip;
	private Map<String,String> cookies;
	private Map<String,String> params;
	private Map<String,String> headers;
	private int executeTime;
	private String  errorMsg;
	
	
	public int getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(int executeTime) {
		this.executeTime = executeTime;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public boolean getIsJsonRequest() {
		return isJsonRequest;
	}
	public void setIsJsonRequest(boolean isJsonRequest) {
		this.isJsonRequest = isJsonRequest;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String toString(){
		return JsonKit.object2Json(this);
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Map<String,String> getCookies() {
		return cookies;
	}
	public void setCookies(Map<String,String> cookies) {
		this.cookies = cookies;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	
}
