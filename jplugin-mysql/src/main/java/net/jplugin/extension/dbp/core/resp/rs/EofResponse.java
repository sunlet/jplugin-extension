package net.jplugin.extension.dbp.core.resp.rs;

import io.netty.buffer.ByteBuf;
import net.jplugin.extension.dbp.core.resp.AbstractPackedResponse;
import net.jplugin.extension.dbp.core.utils.IOUtils;

public class EofResponse extends AbstractPackedResponse {

	public static EofResponse create(byte eof,int warnCnt,int stat) {
		EofResponse o = new EofResponse();
		o.eof = eof;
		o.warningCount = warnCnt;
		o.status = stat;
		return o;
	}
	
    private byte eof = (byte) 0xfe;

    //2 byte
    private int warningCount;

    //2 byte
    private int status;


//    @Override
//    public void read(ByteBuf byteBuf) {
//        this.eof = byteBuf.readByte();
//
//        if (byteBuf.isReadable()) {
//            this.warningCount = IOUtils.readInteger(byteBuf, 2);
//            this.status = IOUtils.readInteger(byteBuf, 2);
//        }
//    }

    @Override
    public void writeContent(ByteBuf byteBuf) {
        byteBuf.writeByte(eof);

        IOUtils.writeInteger(warningCount, byteBuf, 2);
        IOUtils.writeInteger(status, byteBuf, 2);
    }
	

}
