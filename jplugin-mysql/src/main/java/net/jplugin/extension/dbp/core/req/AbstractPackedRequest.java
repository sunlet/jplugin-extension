package net.jplugin.extension.dbp.core.req;

import io.netty.buffer.ByteBuf;
import net.jplugin.extension.dbp.core.utils.IOUtils;

public abstract class AbstractPackedRequest implements IRequestObject {
	byte seqNumber;
	int lengthOfMessage;

	public byte getSeqNumber() {
		return seqNumber;
	}
	
	public int getLengthOfMessage() {
		return lengthOfMessage;
	}

	/**
	 * 真正的读取内容实现在这里
	 * 
	 * @param tmp
	 */
	public abstract void readContent(ByteBuf bf);

	@Override
	public final void read(ByteBuf byteBuf) {
		// read and then
		this.lengthOfMessage = IOUtils.readInteger(byteBuf, 3);
		this.seqNumber = IOUtils.readByte(byteBuf);
		readContent(byteBuf);
	}
}
