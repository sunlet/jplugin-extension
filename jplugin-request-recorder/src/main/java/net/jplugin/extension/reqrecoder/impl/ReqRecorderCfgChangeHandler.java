package net.jplugin.extension.reqrecoder.impl;

import net.jplugin.core.config.api.IConfigChangeContext;
import net.jplugin.core.config.api.IConfigChangeHandler;

public class ReqRecorderCfgChangeHandler implements IConfigChangeHandler {

	@Override
	public void onChange(IConfigChangeContext ctx) {
		LogRecorderConfig.init();
	}

}
