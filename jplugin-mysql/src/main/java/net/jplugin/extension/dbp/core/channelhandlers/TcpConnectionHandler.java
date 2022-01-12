package net.jplugin.extension.dbp.core.channelhandlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;
import net.jplugin.extension.dbp.core.ConnectionContext;
import net.jplugin.extension.dbp.core.resp.ServerGreetingResponse;
import net.jplugin.extension.dbp.core.utils.Util;

public class TcpConnectionHandler extends ChannelInboundHandlerAdapter {

    public static final TcpConnectionHandler INSTANCE = new TcpConnectionHandler();
    
    @RefLogger
    Logger logger;
    
    public TcpConnectionHandler() {
    	PluginEnvirement.INSTANCE.resolveRefAnnotation(this);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        
        //init ConnectionContext
        ConnectionContext connCtx = Util.createConnContext(ctx);
        
        if (logger.isInfoEnabled()) 
        	logger.info("Channel Active:"+ctx.channel().id());
        
        //todo, do some log
        Channel channel = ctx.channel();
        sendAuthencationPackage(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	if (logger.isInfoEnabled()) 
    		logger.info("Channel InActive:"+ctx.channel().id());
    }
    
    private boolean sendAuthencationPackage(Channel channel) {
        ServerGreetingResponse resp = ServerGreetingResponse.create();
        
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(128);
        resp.write(byteBuf);
            	
    	if (logger.isInfoEnabled()) 
    		logger.info("sendAuthencationPackage :"+channel.id());
    	
        channel.writeAndFlush(byteBuf);
        return true;
    }


}
