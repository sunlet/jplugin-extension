package net.jplugin.extension.dbp.core.api;


import net.jplugin.extension.dbp.core.ConnectionContext;
import net.jplugin.extension.dbp.core.req.AuthRequest;

/**
 * 
 * 本接口用来校验权限是否通过，不要做其他任何多余的设置
 * @author LiuHang
 *
 */
public interface ILoginRequestHandler {
	/**
	 * /**
	 *  <PRE>
	   *   本方法实现中，调用 connCtx.setResponseObject(resObject) 来设置返回消息。
	 * resObject参数的构造方法：
	 *     ResultSetResponse.create(....)
	 *     SuccessResponse.create(...)
	 *     ErrorResponse.create(...)
	 *     
	 *    另外，除了验证权限，本方法一般还需要调用connectionContext.setCurrentDb(request.getDatabase())来设置当前的数据库。
	 * </PRE>
	 * 
	 * @param connCtx
	 * @param commandPackage
	 */
	public void checkUserAuth(ConnectionContext connectionContext,AuthRequest request);
}
