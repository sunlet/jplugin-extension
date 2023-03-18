package net.jplugin.extension.appnotify.export;

import net.jplugin.extension.appnotify.api.BroadcastMessage;
import net.jplugin.extension.appnotify.impl.NotifyServiceManager;

import net.jplugin.core.service.api.BindServiceExport;



@BindServiceExport(path = NotifyExport.SERVICE_PATH)
public class NotifyExport implements INotifyExport {
	public static final String SERVICE_PATH="/_platform_/TheAppNotifyService";
	
	
	public void handleMessage(BroadcastMessage msg) {
		NotifyServiceManager.me.fireMessage(msg);
	}
}
