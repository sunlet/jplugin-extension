package net.jplugin.extension.dbp.core.resp.rs;

import io.netty.buffer.ByteBuf;
import net.jplugin.extension.dbp.core.resp.AbstractPackedResponse;
import net.jplugin.extension.dbp.core.utils.IOUtils;

public class ColumnCountResponse extends AbstractPackedResponse{

    private int columnCount;

    public static ColumnCountResponse create(int columnCount) {
    	ColumnCountResponse o = new ColumnCountResponse();
        o.columnCount = columnCount;
        return o;
    }

    public int getColumnCount() {
        return columnCount;
    }

    
//    @Override
//    public void read(ByteBuf byteBuf) {
//        columnCount = IOUtils.readLengthEncodedInteger(byteBuf);
//    }

//    @Override
//    public void write(ByteBuf byteBuf) {
//        
//    }
	
	
	@Override
	public void writeContent(ByteBuf byteBuf) {
		IOUtils.writeLengthEncodedInteger(columnCount, byteBuf);
	}

}
