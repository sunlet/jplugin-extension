package net.jplugin.extension.reqrecoder.api;

import net.jplugin.core.kernel.api.RefAnnotationSupport;

public abstract class AbstractReqLoggerHandler extends RefAnnotationSupport{
	/**
	 * 注意：如果抛出异常，会导致方法失败，所以此方法不要抛出异常！
	 * @param lr
	 */
	public abstract void doLog(LogRecorder lr);
}
