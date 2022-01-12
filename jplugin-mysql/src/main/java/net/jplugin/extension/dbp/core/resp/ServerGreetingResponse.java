package net.jplugin.extension.dbp.core.resp;

import static net.jplugin.extension.dbp.core.consts.CapabilityFlags.*;

import io.netty.buffer.ByteBuf;
import lombok.Builder;
import net.jplugin.extension.dbp.core.utils.IOUtils;

@Builder
public class ServerGreetingResponse extends AbstractPackedResponse {

	public static byte[] salt1 = {1, 1, 1, 1, 1, 1, 1, 1};
    public static final String AUTHENCATION_PLUGIN = "mysql_native_password";
    private static final int SERVER_VERSION = 0x0a;
    private static final String MYSQL_SERVER_VERSION = "5.7.22";
    public static byte[] salt2 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	
	public static ServerGreetingResponse create() {
        int serverCapability = getServerCapality();
        byte[] sereverCapacility = IOUtils.getBytes(serverCapability);
        byte[] lower = new byte[] {sereverCapacility[0], sereverCapacility[1]};
        byte[] higher = new byte[] {sereverCapacility[2], sereverCapacility[3]};

        ServerGreetingResponse greetingPackage = ServerGreetingResponse.builder()
                .serverThreadId((int) Thread.currentThread().getId())
                .saltOne(salt1)
                .protocalVeriosn((byte) SERVER_VERSION)
                .serverVeriosnInfo(MYSQL_SERVER_VERSION)
                //origin is 0xff, cause only disable-ssl mysql -h127.0.0.1 -P3016 -uroot -p123456 --ssl-mode=disabled can connect
                .serverCapability((short) (serverCapability & 0x0000ffff))
                .extendServerCapabilities((short) ((serverCapability >> 16) & 0x0000ffff))
                //see https://dev.mysql.com/doc/internals/en/character-set.html#packet-Protocol::CharacterSet
                .charSet((byte) 33)
                .serverStatus((short) 2)
                .authencationPluginLength((byte) AUTHENCATION_PLUGIN.length())
                .saltTwo(salt2)
                .authencationPlugin(AUTHENCATION_PLUGIN)
                .build();

        return greetingPackage;

	}

    /**
     * Protocol version
     */
    private byte protocalVeriosn;

    /**
     * Server version info
     */
    private String serverVeriosnInfo;

    /**
     * Server thread id
     */
    private int serverThreadId;

    /**
     * Chanllenge randome
     */
    private byte[] saltOne;

    /**
     * Serveer Capability low 2 byte
     */
    private short serverCapability;

    /**
    *  see https://dev.mysql.com/doc/internals/en/character-set.html#packet-Protocol::CharacterSet
     */
    private byte charSet;

    /**
     * see https://dev.mysql.com/doc/internals/en/status-flags.html#packet-Protocol::StatusFlags
     */
    private short serverStatus;

    /**
     * Server Capability high 2 byte
     */
    private short extendServerCapabilities;

    /**
     * see AUTHENCATION_PLUGIN
     */
    private byte authencationPluginLength;

    /**
     * padding * 10
     */
    private byte padding = 0x00;

    /**
     * length 13
     */
    private byte[] saltTwo;

    /**
     * AUTHENCATION_PLUGIN
     */
    private String authencationPlugin;



    /**
     * TODO, try to restore value, this have bug
     *
     * @param lowByte
     * @param highByte
     * @return
     */
    private int getServerCapacility(byte[] lowByte, byte[] highByte) {
        int res = 0;

        res += lowByte[0];
        res += ((lowByte[1] & Integer.MAX_VALUE) << 8);
        res += ((highByte[0] & Integer.MAX_VALUE) << 16);
        res += ((highByte[1] & Integer.MAX_VALUE) << 24);

        return res;
    }
	
	
	
	@Override
	public void writeContent(ByteBuf byteBuf) {
        IOUtils.writeByte(protocalVeriosn, byteBuf);
        IOUtils.writeString(serverVeriosnInfo, byteBuf);
        IOUtils.writeInteger4((int) Thread.currentThread().getId(), byteBuf);
        IOUtils.writeBytes(saltOne, byteBuf);

        //filler, see https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#Payload
        // but, in fact, client can't recognize this,
        //IOUtils.writeByte((byte) 0x00, byteBuf);

        IOUtils.writeShort(serverCapability, byteBuf);

        IOUtils.writeByte(charSet, byteBuf);

        IOUtils.writeShort(serverStatus, byteBuf);

        IOUtils.writeShort(extendServerCapabilities, byteBuf);

        //int mergeCapacility = getServerCapacility(serverCapability, extendServerCapabilities);
        int mergeCapacility = getServerCapality();
        int serverStatus = getServerCapality();

        if ((mergeCapacility & CLIENT_PLUGIN_AUTH) != 0) {
            IOUtils.writeByte(authencationPluginLength, byteBuf);
        } else {
            IOUtils.writeByte(padding, byteBuf);
        }

        //10个字节的填充
        for (int i = 0; i < 10; i++) {
            IOUtils.writeByte(padding, byteBuf);
        }

        if ((mergeCapacility & CLIENT_SECURE_CONNECTION) != 0) {
            int len = Math.max(13, authencationPluginLength - 8);
            for (int i = 0;  i < len; i++) {
                IOUtils.writeByte(saltTwo[i], byteBuf);
            }
        }

        if ((mergeCapacility & CLIENT_PLUGIN_AUTH) != 0) {
            IOUtils.writeString(authencationPlugin, byteBuf);
        }

	}
	
	
    private static int getServerCapality() {
        int flags = 0;

        flags |= CLIENT_LONG_PASSWORD;
        flags |= CLIENT_FOUND_ROWS;
        flags |= CLIENT_LONG_FLAG;
        flags |= CLIENT_CONNECT_WITH_DB;
        flags |= CLIENT_ODBC;
        flags |= CLIENT_IGNORE_SPACE;
        flags |= CLIENT_PROTOCOL_41;
        flags |= CLIENT_INTERACTIVE;
        flags |= CLIENT_IGNORE_SIGPIPE;
        flags |= CLIENT_TRANSACTIONS;
        flags |= CLIENT_SECURE_CONNECTION;
        flags |= CLIENT_PLUGIN_AUTH;
        return flags;
    }


}
