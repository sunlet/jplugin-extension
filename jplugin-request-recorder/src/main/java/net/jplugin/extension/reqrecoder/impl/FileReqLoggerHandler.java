package net.jplugin.extension.reqrecoder.impl;


import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.extension.reqrecoder.api.AbstractReqLoggerHandler;
import net.jplugin.extension.reqrecoder.api.LogRecorder;

public class FileReqLoggerHandler extends AbstractReqLoggerHandler{

	Logger logger; 
	@Override
	public void doLog(LogRecorder lr) {
		if (logger==null){
			 logger = ServiceFactory.getService(ILogService.class).getSpecicalLogger("req-log-recorder.log");
		}
		logger.info(lr.toString());
	}

}
