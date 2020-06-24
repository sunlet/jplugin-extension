package net.jplugin.extension.reqrecoder.impl;

import java.util.Random;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.Logger;
import net.jplugin.ext.webasic.api.HttpFilterContext;
import net.jplugin.ext.webasic.api.IHttpFilter;
import net.jplugin.extension.reqrecoder.api.LogRecorder;
import net.jplugin.extension.reqrecoder.kit.LogRecorderHelper;

public class ReqLogFilter extends RefAnnotationSupport implements IHttpFilter{

	/**
	 * 目前不需要配置log4j，因为是从specifalLog获取的
	 * 
	 * <pre>
	 * #req-log
		log4j.logger.ReqRecorder=INFO,ReqRecorder
		log4j.additivity.ReqRecorder=false
		log4j.appender.ReqRecorder=org.apache.log4j.RollingFileAppender
		log4j.appender.ReqRecorder.file=${work-dir}/logs/req-recorder.log
		log4j.appender.ReqRecorder.maxFileSize=20MB
		log4j.appender.ReqRecorder.maxBackupIndex=50
		log4j.appender.ReqRecorder.Threshold=info 
		log4j.appender.ReqRecorder.layout=org.apache.log4j.PatternLayout
		log4j.appender.ReqRecorder.layout.ConversionPattern=%d %m %n
	 * </pre>
	 */
	private static Logger logger;
	
	Random rand = new Random();
	
	
	public static void main(String[] args) {
		Random r = new Random();
		for (int i=0;i<100;i++) {
			System.out.println(r.nextDouble());
		}
	}
	
	@Override
	public Object filter(FilterChain fc, HttpFilterContext ctx) throws Throwable {
		//如果没有开启，直接略过
		if (!LogRecorderConfig.enable) 
			return fc.next(ctx);
		
		//判断比例，比例外直接返回
		if (LogRecorderConfig.simpling!=null && rand.nextDouble()>LogRecorderConfig.simpling) {
			return fc.next(ctx);
		}
		
		//规则：没有配置则通过，如果配置了则必须匹配上才记录。
		//不为null，并且没有匹配上，表示略过
		if (LogRecorderConfig.urlMatcher != null
				&& !LogRecorderConfig.urlMatcher.match(ctx.getRequest().getServletPath()))
			return fc.next(ctx);
		
		//记录日志
		long timeStart =System.currentTimeMillis();
		long timeEnd;
		Throwable exception=null;
		Object result=null;
		
		//获取信息
		try {
			result = fc.next(ctx);
		}catch (Throwable e) {
			exception = e;
		}finally {
			timeEnd = System.currentTimeMillis();
		}
		
		//判断响应时间
		if (LogRecorderConfig.executeTimeBottom==null || (timeEnd-timeStart>=LogRecorderConfig.executeTimeBottom)) {
			//默认记录日志如果发生异常，也会影响请求！！！！！
			LogRecorder msg = LogRecorderHelper.me.create(ctx,exception,timeStart,timeEnd);
			LogRecorderConfig.reqLogHandler.doLog(msg);
		}
		
		
		if (exception !=null) {
			throw exception;	
		}else {
			return result;
		}
	}
	

	
	
	
}
