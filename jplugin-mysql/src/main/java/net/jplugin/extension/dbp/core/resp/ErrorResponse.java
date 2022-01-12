package net.jplugin.extension.dbp.core.resp;

import io.netty.buffer.ByteBuf;
import lombok.Builder;
import net.jplugin.extension.dbp.core.utils.IOUtils;

@Builder
public class ErrorResponse extends AbstractPackedResponse {
	private byte header;

	private short errorCode;

	private String errorMessage;

	public static ErrorResponse create(int errorCode, String errorMessage) {
		return create(errorCode,errorMessage,1);
	}
	public static ErrorResponse create(int errorCode, String errorMessage, int seqNumber) {
		ErrorResponse errPackage = ErrorResponse.builder().header((byte) 0xff).errorCode((short) errorCode)
				.errorMessage(errorMessage).build();
		errPackage.setSeqNumber((byte) seqNumber);
		return errPackage;
	}

	@Override
	public void writeContent(ByteBuf byteBuf) {
		IOUtils.writeByte(header, byteBuf);
		IOUtils.writeShort(errorCode, byteBuf);
		IOUtils.writeString(errorMessage, byteBuf);
	}
}
