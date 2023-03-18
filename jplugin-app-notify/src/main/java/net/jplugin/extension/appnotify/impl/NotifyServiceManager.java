package net.jplugin.extension.appnotify.impl;

import java.util.Map;

import net.jplugin.extension.appnotify.Plugin;
import net.jplugin.extension.appnotify.api.IAppBroadcastListener;
import net.jplugin.extension.appnotify.api.BroadcastMessage;

import net.jplugin.core.kernel.api.PluginEnvirement;

public class NotifyServiceManager {

	public static NotifyServiceManager me = new NotifyServiceManager();
	private Map<String, IAppBroadcastListener> listenerMap;

	public void init() {
		this.listenerMap = PluginEnvirement.INSTANCE.getExtensionMap(Plugin.EP_APP_NOTIFY,IAppBroadcastListener.class);
	}
	
	public void fireMessage(BroadcastMessage msg) {
		String type = msg.getType();
		IAppBroadcastListener listener = listenerMap.get(type);
		if (listener==null) {
			throw new RuntimeException("can't find type:"+type);
		}else {
			listener.onMessage(msg);
		}
	}
}
