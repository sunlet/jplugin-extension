package net.jplugin.extension.dbp.core.basichandler;

import java.util.regex.Matcher;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.extension.dbp.core.ConnectionContext;
import net.jplugin.extension.dbp.core.api.ICommandFilter;
import net.jplugin.extension.dbp.core.resp.SuccessResponse;
import net.jplugin.extension.dbp.core.utils.PatternUtils;
import net.jplugin.extension.dbp.core.utils.Util;

@BindExtension(pointTo = net.jplugin.extension.dbp.core.Plugin.EP_MYSQL_COMMAND_FILTER)
public class ServerSettingFilter implements ICommandFilter {

	@Override
	public Object filter(FilterChain fc, ConnectionContext ctx) throws Throwable {
		if (isSettingsQuery(ctx)) {
			handleSetting(ctx);
		}
		
		if (ctx.getResponseObject() == null) {
			return fc.next(ctx);
		}
		return null;

	}

	private boolean isSettingsQuery(ConnectionContext connCtx) {
		String query = Util.getCommandQuery(connCtx).toLowerCase();
		Matcher matcher = PatternUtils.SETTINGS_PATTERN.matcher(query);
		return matcher.find();
	}

	private void handleSetting(ConnectionContext context) {
		String query = Util.getCommandQuery(context).toLowerCase();
		Matcher matcher = PatternUtils.SETTINGS_PATTERN.matcher(query);
		while (matcher.find()) {
			if ("null".equalsIgnoreCase(matcher.group(7))) {
				continue;
			}
			context.getConnectionAttributes().put(matcher.group(3), matcher.group(7));
		}

		SuccessResponse resp = SuccessResponse.create(0, 0);
		context.setResponseObject(resp);
	}
}
