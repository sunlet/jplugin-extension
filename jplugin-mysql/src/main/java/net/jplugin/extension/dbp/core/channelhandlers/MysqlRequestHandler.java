package net.jplugin.extension.dbp.core.channelhandlers;

import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.RefExtensionMap;
import net.jplugin.core.kernel.api.RefExtensions;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;
import net.jplugin.extension.dbp.core.ConnectionContext;
import net.jplugin.extension.dbp.core.api.ICommandHandler;
import net.jplugin.extension.dbp.core.api.ILoginRequestHandler;
import net.jplugin.extension.dbp.core.api.IResponseObject;
import net.jplugin.extension.dbp.core.req.AuthRequest;
import net.jplugin.extension.dbp.core.req.CommandRequest;
import net.jplugin.extension.dbp.core.req.IRequestObject;
import net.jplugin.extension.dbp.core.resp.ErrorResponse;
import net.jplugin.extension.dbp.core.utils.Util;

public class MysqlRequestHandler extends ChannelInboundHandlerAdapter{
	
	@RefExtensionMap(pointTo = net.jplugin.extension.dbp.core.Plugin.EP_MYSQL_COMMAND_HANDLER)
	private Map<String,ICommandHandler> handlerMap;
	
	@RefExtensions(pointTo = net.jplugin.extension.dbp.core.Plugin.EP_MYSQL_AUTH_CHECK_HANDLER)
	private List<ILoginRequestHandler> authCheckHandlers;
	
	@RefLogger
	Logger logger;
	
	public MysqlRequestHandler() {
		PluginEnvirement.INSTANCE.resolveRefAnnotation(this);
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ThreadLocalContextManager.instance.createContext();
			channelReadInner(ctx, msg);
		}finally {
			ThreadLocalContextManager.instance.releaseContext();
		}
	}
	private void channelReadInner(ChannelHandlerContext ctx, Object msg) throws Exception {
		  ConnectionContext connCtx = Util.getConnContext(ctx);
		  
		  //Get response
		  IRequestObject pkg = (IRequestObject) msg;
		  IResponseObject resultPacket;
		  try {
			  //获取返回结构
			  handleRequest(connCtx,pkg);
			  resultPacket = connCtx.getResponseObject();
			  if (resultPacket==null) {
				  String errMsg = "No result packet after handled";
				  resultPacket = ErrorResponse.create(11, errMsg);
				  logger.error(errMsg);
			  }
			  
			  //清理返回结构
			  connCtx.setResponseObject(null);
			  
		  }catch(Throwable th) {
			  //清理返回结构
			  connCtx.setResponseObject(null);
			  
			  //构造错误返回信息
//			  logger.error(th.getMessage(),th);
			  resultPacket = ErrorResponse.create(11,"Error Caught:"+th.getMessage());
		  }
		  
		  //Send the message
		  if (ctx.channel().isActive()) {
			  ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(128);
			  resultPacket.write(byteBuf);
			  ctx.channel().writeAndFlush(byteBuf);
		  }
	}

	private void handleRequest(ConnectionContext connCtx, IRequestObject msg) {
		// Do check
		if (msg instanceof AuthRequest) {
			handleLogin(connCtx, (AuthRequest) msg);
		} else if (msg instanceof CommandRequest) {
			handleCommand(connCtx, (CommandRequest) msg);
		}else {
			throw new RuntimeException("Command should not be  auth command now!");
		}
	}
	
	private void handleLogin(ConnectionContext connCtx, AuthRequest lr) {
		if (logger.isInfoEnabled()) {
			logger.info("Now handler Login Request."+ lr);
		}
		
		if (authCheckHandlers.size()!=0) {
			authCheckHandlers.get(0).checkUserAuth(connCtx, lr);
		}else {
			throw new RuntimeException("login handler not found!");
		}
	}

	private void handleCommand(ConnectionContext connCtx, CommandRequest commandPackage) {
		if (logger.isInfoEnabled()) {
			logger.info("Now handler Command."+commandPackage +" DB="+connCtx.getCurrentDb());
		}
		
		final byte type = commandPackage.getCommandType();
        ICommandHandler handler = handlerMap.get(type+"");
        handler.executeCommand(connCtx,commandPackage);
	}
	
	
}
