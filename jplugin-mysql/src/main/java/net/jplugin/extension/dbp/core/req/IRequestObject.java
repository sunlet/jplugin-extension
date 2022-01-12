package net.jplugin.extension.dbp.core.req;


import io.netty.buffer.ByteBuf;

public interface IRequestObject {
  public void read(ByteBuf byteBuf);
}
