package net.jplugin.extension.embed_tomcat.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.ExceptionKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.impl.WebDriver;

//public class PluginServlet4Embed extends HttpServlet{
//
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		PrintWriter w = resp.getWriter();
//		w.write("okokok");
//		
//		w.flush();
//		w.close();
//		System.out.println("do posting");
//	}
//}

public class PluginServlet4Embed  extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dohttp(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dohttp(req, resp);
	}
	
	public void dohttp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		WebDriver.INSTANCE.dohttp(req, res);
	}
//
//	@Override
//	public void destroy() {
//		PluginEnvirement.getInstance().stop();
//	}
}