package net.jplugin.extension.dbp.core.consts;

import io.netty.util.AttributeKey;
import net.jplugin.extension.dbp.core.ConnectionContext;

public class Constants {
	public static final AttributeKey<ConnectionContext> ATTR_KEY_CONNECTION_CTX = AttributeKey.valueOf("CONNECTION_CTX_ATTR");
	

	//CommandTypes
    public static final String COMMAND_USE_DB = 0x02 + "";
    public static final String COMMAND_QUERY = 0x03 + "";
    public static final String COMMAND_PING = 0x0e + "";
//    public static final byte COMMAND_CREATE_DB = 0x05;
//    public static final byte COMMAND_DROP_DB = 0x06;
    
    //mysql types
    public static final int MYSQL_TYPE_DECIMAL = 0x00;
    public static final int MYSQL_TYPE_TINY = 0x01;
    public static final int MYSQL_TYPE_SHORT = 0x02;
    public static final int MYSQL_TYPE_LONG = 0x03;
    public static final int MYSQL_TYPE_FLOAT = 0x04;
    public static final int MYSQL_TYPE_DOUBLE = 0x05;
    public static final int MYSQL_TYPE_NULL = 0x06;
    public static final int MYSQL_TYPE_TIMESTAMP = 0x07;
    public static final int MYSQL_TYPE_LONGLONG = 0x08;
    public static final int MYSQL_TYPE_INT24 = 0x09;
    public static final int MYSQL_TYPE_DATE = 0x0a;
    public static final int MYSQL_TYPE_TIME = 0x0b;
    public static final int MYSQL_TYPE_DATETIME = 0x0c;
    public static final int MYSQL_TYPE_YEAR = 0x0d;
    public static final int MYSQL_TYPE_NEWDATE = 0x0e;

    public static final int MYSQL_TYPE_VARCHAR = 0x0f;
    public static final int MYSQL_TYPE_BIT = 0x10;
    public static final int MYSQL_TYPE_TIMESTAMP2 = 0x11;
    public static final int MYSQL_TYPE_DATETIME2 = 0x12;
    public static final int MYSQL_TYPE_TIME2 = 0x13;

    public static final int MYSQL_TYPE_NEWDECIMAL = 0xf6;
    public static final int MYSQL_TYPE_ENUM = 0xf7;

    public static final int MYSQL_TYPE_SET = 0xf8;
    public static final int MYSQL_TYPE_TINY_BLOB = 0xf9;
    public static final int MYSQL_TYPE_MEDIUM_BLOB = 0xfa;
    public static final int MYSQL_TYPE_LONG_BLOB = 0xfb;
    public static final int MYSQL_TYPE_BLOB = 0xfc;

    public static final int MYSQL_TYPE_VAR_STRING = 0xfd;
    public static final int MYSQL_TYPE_STRING = 0xfe;
    public static final int MYSQL_TYPE_GEOMETRY = 0xff;
}
