package net.jplugin.extension.dbp.core.basichandler;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.extension.dbp.core.ConnectionContext;
import net.jplugin.extension.dbp.core.api.ICommandHandler;
import net.jplugin.extension.dbp.core.consts.Command;
import net.jplugin.extension.dbp.core.consts.Constants;
import net.jplugin.extension.dbp.core.req.CommandRequest;
import net.jplugin.extension.dbp.core.resp.SuccessResponse;

@BindExtension(pointTo = net.jplugin.extension.dbp.core.Plugin.EP_MYSQL_COMMAND_HANDLER, name = Constants.COMMAND_PING)
public class PingCommandHandler implements ICommandHandler {

	@Override
	public void handleCommand(ConnectionContext connCtx, CommandRequest commandPackage) {
	      SuccessResponse resp = SuccessResponse.create(0, 0);
	      connCtx.setResponseObject(resp);
	}


}
