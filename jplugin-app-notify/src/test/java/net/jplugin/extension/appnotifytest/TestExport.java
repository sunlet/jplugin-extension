package net.jplugin.extension.appnotifytest;

import net.jplugin.extension.appnotify.api.AppBroadcastManager;
import net.jplugin.extension.appnotify.api.BroadcastMessage;
import net.jplugin.extension.appnotify.api.Result;
import net.jplugin.core.service.api.BindServiceExport;


@BindServiceExport(path = "/call")
public class TestExport {
	public Result type1(String p) {
		BroadcastMessage msg = BroadcastMessage.create("type1");
		msg.getAttributes().put("vvv", p);
		Result result = AppBroadcastManager.INSTANCE.sendMessage(msg);
		return result;
	}
	
	public Result type2(String p) {
		BroadcastMessage msg = BroadcastMessage.create("type2");
		msg.getAttributes().put("vvv", p);
		Result result = AppBroadcastManager.INSTANCE.sendMessage(msg,false);
		return result;
	}

}
