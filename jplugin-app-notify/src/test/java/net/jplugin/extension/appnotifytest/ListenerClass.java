package net.jplugin.extension.appnotifytest;

import net.jplugin.extension.appnotify.api.IAppBroadcastListener;
import net.jplugin.extension.appnotify.api.BroadcastMessage;

public class ListenerClass implements IAppBroadcastListener {

	@Override
	public void onMessage(BroadcastMessage msg) {
		System.out.println(ListenerClass.class.getName()+" " +msg);
	}

}
