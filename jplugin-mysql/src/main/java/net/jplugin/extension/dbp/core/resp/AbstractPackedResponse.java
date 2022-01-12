package net.jplugin.extension.dbp.core.resp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.jplugin.extension.dbp.core.api.IResponseObject;
import net.jplugin.extension.dbp.core.utils.IOUtils;

public abstract class AbstractPackedResponse implements IResponseObject{
	byte seqNumber;
	
	public byte getSeqNumber() {
		return seqNumber;
	}
	public void setSeqNumber(byte seqNumber) {
		this.seqNumber = seqNumber;
	}
	
	/**
	  * 真正的返回内容实现在这里
	 * @param tmp
	 */
	public abstract void writeContent(ByteBuf tmp);

    @Override
    public final void write(ByteBuf byteBuf) {
    	ByteBuf tmp = PooledByteBufAllocator.DEFAULT.buffer(128);
        writeContent(tmp);

        int lengthOfMessage = tmp.readableBytes();
        IOUtils.writeInteger3(lengthOfMessage, byteBuf);
        IOUtils.writeByte(seqNumber, byteBuf);
        byte[] bytes = new byte[tmp.readableBytes()];
        tmp.readBytes(bytes);

        //change from writeBytes -->  writeBytesWithoutEndFlag
        IOUtils.writeBytesWithoutEndFlag(bytes, byteBuf);
    }
}
