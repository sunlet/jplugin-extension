package net.jplugin.extension.dbp.core;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.extension.dbp.core.api.ICommandHandler;
import net.jplugin.extension.dbp.core.api.ILoginRequestHandler;


/**
 * <PRE>
 * 	xxx.apo包
	 	放置 应用程序对外的对象(apo，英文全称 application object,其他的一些文档称为VO)。
	 	Apo为系统对外发布的服务当中用到的复杂类型对象。具体是都是一些简单的POJO类。
	 	
	xxx.controller包
	 
	 	放置 应用程序的Controller,Controller为保障Web页面开发而进行的动态展现、跳转等。
	 	虽然也可以基于Controller的方式开发服务，但这是违反开发规范的不推荐的做法！
	 	服务开发建议使用 ServiceExport，更方便，对应的代码放入export包当中。
	 
	xxx.dbo包
		放置数据访问用到的数据库业务对象（DatabaseObject),这些对象一般在mapper接口当中会用到。
	
	xxx.export包 
	 	本package放置对外发布的服务。 
	
	xxx.mapper包
	 	提供Mybatis的 Mapper
	
	xxx.service.api包
	 	本package放置设计的内部服务接口 
	
	xxx.service.impl包 
	 	放置设计的内部的实现。
	 	接口可以通过@BindService方式作为普通的服务，也可以用@BindRuleService方式发布为支持事务的服务。 
	
	xxx.util包
		放置工具类
		
	xxx.extension包
		放置本插件提供的除了controller、export、mapper等基础扩展之外的其他扩展类		
	</PRE>
 */

@PluginAnnotation
public class Plugin extends AbstractPlugin{

	public static final String EP_MYSQL_COMMAND_HANDLER = "EP_MYSQL_COMMAND";
	public static final String EP_MYSQL_AUTH_CHECK_HANDLER = "EP_MYSQL_AUTH_CHECK_HANDLER";

	public Plugin() {
		this.addExtensionPoint(ExtensionPoint.createNamed(EP_MYSQL_COMMAND_HANDLER, ICommandHandler.class));
		this.addExtensionPoint(ExtensionPoint.createUnique(EP_MYSQL_AUTH_CHECK_HANDLER, ILoginRequestHandler.class));
	}
	
	MysqlStarter thread;
	@Override
	public void init() {
		MysqlStarter starter = new MysqlStarter();
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Now to start mysql protocol service.");
		new Thread(starter).start();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.OFFSET_FOR_EXTENSION +100;
	}

}
