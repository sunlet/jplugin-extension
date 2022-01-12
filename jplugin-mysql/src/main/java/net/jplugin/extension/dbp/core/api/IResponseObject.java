package net.jplugin.extension.dbp.core.api;

import io.netty.buffer.ByteBuf;

public interface IResponseObject {
	
    void write(ByteBuf byteBuf); 
}
