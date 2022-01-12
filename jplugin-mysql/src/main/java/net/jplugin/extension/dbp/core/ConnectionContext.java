package net.jplugin.extension.dbp.core;

import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import net.jplugin.extension.dbp.core.api.IResponseObject;

import java.util.Map;

public class ConnectionContext {

	/**
	 * 结果package
	 */
	IResponseObject responseObject;

	/**
     * Connection
     */
    private ChannelHandlerContext channelHandlerContext;

    /**
     * if db is not null, this context may use db;
     */
    private String currentDb;

    /**
     * Store the property
     */
    private Map<String, String> properties = Maps.newHashMap();
    /**
     * Store the current query string
     */
    private ThreadLocal<String> queryString = new ThreadLocal<>();

    public ConnectionContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public String getCurrentDb() {
        return currentDb;
    }

    public void setCurrentDb(String db) {
        this.currentDb = db;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void write(ByteBuf byteBuf) {
        channelHandlerContext.writeAndFlush(byteBuf);
        if (ReferenceCountUtil.refCnt(byteBuf) > 0) {
            ReferenceCountUtil.release(byteBuf);
        }
    }

    public void setQueryString(String sql) {
        queryString.set(sql);
    }

    public String getQueryString() {
        return queryString.get();
    }

    boolean authed = false;
	public boolean isAuthrized() {
		return authed;
	}
	
	public void setAuthizeSuccess() {
		this.authed = true;
	}

    public IResponseObject getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(IResponseObject resultObject) {
		this.responseObject = resultObject;
	}
}
