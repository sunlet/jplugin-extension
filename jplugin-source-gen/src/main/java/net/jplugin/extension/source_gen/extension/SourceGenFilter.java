package net.jplugin.extension.source_gen.extension;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.IpKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.api.InvocationContext;



public class SourceGenFilter implements  IInvocationFilter{

	public boolean before(InvocationContext ctx) {
		ThreadLocalContext tlCtx = ThreadLocalContextManager.getCurrentContext();
		HttpServletRequest req = (HttpServletRequest) tlCtx.getAttribute(ThreadLocalContext.ATTR_SERVLET_REQUEST);
		if (req==null) {
			return true;
		}
		
		
		if (containsPara(req,"_gen_rpc_client_")){
			HttpServletResponse res= (HttpServletResponse) tlCtx.getAttribute(ThreadLocalContext.ATTR_SERVLET_RESPONSE);
			
			try {
				String ipaddress = ctx.getRequestInfo().getCallerIpAddress();
				if (!IpKit.isOuterIpAddress(ipaddress)) {
					res.getWriter().write(SourceGenerator.generate(ctx));	
				}else {
					res.getWriter().write("Not support");
				}
				
				//阻止返回json结果
				tlCtx.setAttribute(net.jplugin.ext.webasic.impl.restm.Constants.NOT_WRITE_RESULT, Boolean.FALSE);
			} catch (IOException e) {
				throw new RuntimeException("Error:"+e.getMessage(),e);
			}
			
			//这里取一个巧，抛一个不用记录的异常
			RemoteExecuteException ex = new RemoteExecuteException(-1);
			ex.setNeedLog(false);
			throw ex;
		}
		
		return true;
	}
	
	private boolean containsPara(HttpServletRequest req, String nm) {
		Enumeration<String> paras = req.getParameterNames();
		if (paras==null) return false;
		while(paras.hasMoreElements()){
			if (nm.equals(paras.nextElement())){
				return true;
			}
		}
		return false;
	}

	public void after(InvocationContext ctx) {
		
	}


}
