package net.jplugin.extension.appnotifytest;

import net.jplugin.extension.appnotify.api.IAppBroadcastListener;
import net.jplugin.extension.appnotify.api.BroadcastMessage;

public class ListenerClass2 implements IAppBroadcastListener{

	@Override
	public void onMessage(BroadcastMessage msg) {
		System.out.println(ListenerClass2.class.getName()+" " +msg);	
	}

}
