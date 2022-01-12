package net.jplugin.extension.dbp.core.req;

import io.netty.buffer.ByteBuf;
import net.jplugin.extension.dbp.core.utils.IOUtils;

public class CommandRequest extends AbstractPackedRequest {

	private byte commandType;
	private String command;
	
	public String getCommand() {
		return command;
	}
	
	public byte getCommandType() {
		return commandType;
	}

	@Override
	public void readContent(ByteBuf byteBuf) {
		this.commandType = IOUtils.readByte(byteBuf);
		this.command = IOUtils.readEofString(byteBuf);
	}
	
	public String toString() {
		return "SQL:"+command+"  CommandType="+commandType;
	}

}
