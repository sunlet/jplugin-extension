package net.jplugin.extension.dbp.core.resp.rs;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.jplugin.extension.dbp.core.resp.AbstractPackedResponse;
import net.jplugin.extension.dbp.core.utils.IOUtils;

public class ColumnValueResponse extends AbstractPackedResponse {

	
	  //all value encode with LengthEncodedString
    private List<String> texts;

    public static ColumnValueResponse create(List<String> texts) {
    	ColumnValueResponse o = new ColumnValueResponse();
        o.texts = texts;
        return o;
    }

    @Override
    public void writeContent(ByteBuf byteBuf) {
        texts.forEach(t -> IOUtils.writeLengthEncodedString(byteBuf, t));
    }


}
