package net.jplugin.extension.dbp.core;

import static java.nio.ByteOrder.LITTLE_ENDIAN;



import com.google.common.base.Throwables;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.extension.dbp.core.channelhandlers.ByteBufToRequestDecoder;
import net.jplugin.extension.dbp.core.channelhandlers.MysqlRequestHandler;
import net.jplugin.extension.dbp.core.channelhandlers.TcpConnectionHandler;

/**
 * @author Sunlet
 **/
public class MysqlStarter extends RefAnnotationSupport implements Runnable{

    public static final Logger LOGGER = LogFactory.getLogger(MysqlStarter.class);
	private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();
    
    @RefConfig(path = "mysql.read-time-out",defaultValue = "300000")
    private Integer READ_TIMEOUT;
    
    @RefConfig(path = "mysql.write-time-out",defaultValue = "300000")
    private Integer WRITE_TIMEOUT;
        
    @RefConfig(path = "mysql.socket-rev-buffer",defaultValue = "8196")
    private Integer RECEIVE_BUF;
    
    @RefConfig(path = "mysql.server-port",defaultValue = "3316")
    private Integer SERVER_PORT;


    @Override
    public void run() {
       //
    	PluginEnvirement.INSTANCE.getStartLogger().log("$$$ In mysql protocol thread.");
        try {
            final EventLoopGroup boss = new NioEventLoopGroup(1);
            final EventLoopGroup work = new NioEventLoopGroup(CPU_CORES * 2);
            final ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_RCVBUF, RECEIVE_BUF)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    final ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast("open_channler", new TcpConnectionHandler());

                    pipeline.addLast("read_timeout_handler", new ReadTimeoutHandler(READ_TIMEOUT));
                    pipeline.addLast("write_time_handler", new WriteTimeoutHandler(WRITE_TIMEOUT));

                    pipeline.addLast("byte_buf_spliter", new LengthFieldBasedFrameDecoder(
                            LITTLE_ENDIAN,
                            Integer.MAX_VALUE,
                            0,
                            3,
                            1,
                            0,
                            true
                    ));

                    pipeline.addLast("bytebuf_to_message", new ByteBufToRequestDecoder());
                    pipeline.addLast("mysql_message_handler", new MysqlRequestHandler());
                }
            });

            serverBootstrap.validate();
            ChannelFuture channelFuture = serverBootstrap.bind(SERVER_PORT).sync();

            channelFuture.channel().closeFuture().addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    //todo
                }
            });
            PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Mysql protocal start success");
        } catch (Exception e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
        }
    }
}
