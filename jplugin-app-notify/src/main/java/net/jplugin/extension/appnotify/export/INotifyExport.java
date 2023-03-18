package net.jplugin.extension.appnotify.export;

import net.jplugin.extension.appnotify.api.BroadcastMessage;

public interface INotifyExport {

	void handleMessage(BroadcastMessage msg);

}