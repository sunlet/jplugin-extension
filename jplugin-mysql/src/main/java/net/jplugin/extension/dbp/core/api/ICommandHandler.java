package net.jplugin.extension.dbp.core.api;

import net.jplugin.extension.dbp.core.req.CommandRequest;
import net.jplugin.extension.dbp.core.resp.ResultSetResponse;


public interface ICommandHandler {
	/**
	 *  <PRE>
	   *   本方法实现中，调用 connCtx.setResponseObject(resObject) 来设置返回消息。
	 * resObject参数的构造方法：
	 *     ResultSetResponse.create(....)
	 *     SuccessResponse.create(...)
	 *     ErrorResponse.create(...)
	 * </PRE>
	 * 
	 * @param connCtx
	 * @param commandPackage
	 */
	public void executeCommand(net.jplugin.extension.dbp.core.ConnectionContext connCtx,CommandRequest commandPackage);

}
