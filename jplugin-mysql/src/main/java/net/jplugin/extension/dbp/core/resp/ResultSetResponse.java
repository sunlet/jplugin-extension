package net.jplugin.extension.dbp.core.resp;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.extension.dbp.core.api.IResponseObject;
import net.jplugin.extension.dbp.core.resp.rs.ColumnCountResponse;
import net.jplugin.extension.dbp.core.resp.rs.ColumnTypeResponse;
import net.jplugin.extension.dbp.core.resp.rs.ColumnValueResponse;
import net.jplugin.extension.dbp.core.resp.rs.EofResponse;



public class ResultSetResponse  implements IResponseObject{
	private static final int MAX_LIMIT = 100000000;
	private List<Integer> columnType;
	private List<List<String>> data;
	private List<String> columnName;
	private String schema;
	private String table;
	
	private static Logger logger = LogFactory.getLogger(ResultSetResponse.class);
	
	public static ResultSetResponse create(ResultSet rs) {
		return create(rs,MAX_LIMIT);
	}
	public static ResultSetResponse create(ResultSet rs ,int limit) {
		try {
			Tuple2<List<Integer>, List<String>> typeNames = getColumnMetas(rs);
			List<List<String>> datas = getDatas(rs , limit);
			ResultSetResponse rsr = ResultSetResponse.create(typeNames.first, datas, typeNames.second, "def", "tb");
			return rsr;
		}catch(Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
	

	
	public static ResultSetResponse create(List<Integer> columnType,List<List<String>> data,List<String> columnName,String schema , String table) {
		ResultSetResponse o = new ResultSetResponse();
		o.columnType = columnType;
		o.data = data;
		o.columnName = columnName;
		o.schema = schema;
		o.table = table;
		return o;
	}
	
	@Override
	public void write(ByteBuf byteBuf) {
		final List<IResponseObject> result = Lists.newArrayList();

		final int columnNum = columnType.size();

		// query seqNumber is 0 so result query number from 1
		byte seqNumber = 1;

		// first is the column count
		final ColumnCountResponse columnCountPackage = ColumnCountResponse.create(columnNum);
		columnCountPackage.setSeqNumber(seqNumber++);
		result.add(columnCountPackage);

		// second is the column detail
		List<ColumnTypeResponse> columnDetails = Lists.newArrayList();

		for (int i = 0; i < columnNum; i++) {
			final ColumnTypeResponse columnTypePackage = ColumnTypeResponse.builder().catalog("def").schema(schema).table(table)
					.orgTable("").name(columnName.get(i)).originalName("")
					// original 33
					.charSet(0).filler((byte) 0x0c)
					// original 84
					.columnLength(0).columnType((byte) columnType.get(i).intValue()).flags(0x00).dicimals((byte) 0x00)
					.build();

			columnTypePackage.setSeqNumber(seqNumber++);
			columnDetails.add(columnTypePackage);
		}
		result.addAll(columnDetails);

		// third is end of package
		final EofResponse eofPackage =  EofResponse.create((byte) 0xfe, 0, 0x0002);
		eofPackage.setSeqNumber(seqNumber++);
		result.add(eofPackage);

		// fourth is row value
		List<ColumnValueResponse> rows = Lists.newArrayList();
		for (List<String> row : data) {
			ColumnValueResponse columnValue = ColumnValueResponse.create(row);
			columnValue.setSeqNumber(seqNumber++);
			rows.add(columnValue);
		}
		result.addAll(rows);

		// eof package again
		final EofResponse eofPackageLast = EofResponse.create((byte) 0xfe, 0, 0x0002);
		eofPackageLast.setSeqNumber(seqNumber++);
		result.add(eofPackageLast);

		//write to buffer
		result.forEach(a -> a.write(byteBuf));
	}

	
	private static List<List<String>> getDatas(ResultSet rs , int limit) throws SQLException {
		List<List<String>> list = new ArrayList();
		int cnt = rs.getMetaData().getColumnCount();
		
		int rowPos=0;
		while(rs.next()) {
			List<String> results = new ArrayList();
			for (int i=1;i<=cnt;i++) {
				results.add(rs.getString(i));
			}
			list.add(results);
			
			if ( ++ rowPos >=limit) 
				break;
		}
		return list;
	}

	private static Tuple2<List<Integer>, List<String>> getColumnMetas(ResultSet rs) throws SQLException {
		List<Integer> listTypes = new ArrayList<Integer>();
		List<String> listNames = new ArrayList<String>();
		
		ResultSetMetaData meta = rs.getMetaData();
		int cnt = meta.getColumnCount();
		for (int i=1;i<=cnt;i++) {
			listTypes.add(meta.getColumnType(i));
			listNames.add(meta.getColumnName(i));
		}
		
		return Tuple2.with(listTypes,listNames);
	}

}
