package net.jplugin.extension.dbp.core.resp;

import io.netty.buffer.ByteBuf;
import lombok.Builder;
import net.jplugin.extension.dbp.core.utils.IOUtils;

@Builder
public class SuccessResponse extends AbstractPackedResponse {
	/**
	 * 0x00 is ok 0xfe is EOF
	 */
	private byte header;

	// add later
	private int affectedRows;

	// add later
	private int lastInsertId;

	private int serverStatus;

	// add later
	private int numberOfWarning;

	private String info;
	
	public static SuccessResponse create(int affectedRows, int lastInsertId) {
		return create(affectedRows,lastInsertId,1);
	}
	
	public static SuccessResponse create(int affectedRows, int lastInsertId , int seqNumber) {
		SuccessResponse okPackage = SuccessResponse.builder().header((byte) 0x00).serverStatus(0x0002).affectedRows(affectedRows)
				.lastInsertId(lastInsertId).build();
		okPackage.setSeqNumber((byte) seqNumber);
		
		return okPackage;
	}

	

	@Override
	public void writeContent(ByteBuf byteBuf) {
		IOUtils.writeByte(header, byteBuf);
		IOUtils.writeInteger(affectedRows, byteBuf, 1);
		IOUtils.writeInteger(lastInsertId, byteBuf, 1);
		IOUtils.writeInteger(serverStatus, byteBuf, 2);
		IOUtils.writeInteger(numberOfWarning, byteBuf, 2);

		if (info != null) {
			IOUtils.writeLengthEncodedString(byteBuf, info);
		}
	}
}
