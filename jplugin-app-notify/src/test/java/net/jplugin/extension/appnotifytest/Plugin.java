package net.jplugin.extension.appnotifytest;

import net.jplugin.extension.appnotify.ExtensionAppNotifyHelper;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin  extends AbstractPlugin{

	public Plugin() {
		this.searchAndBindExtensions();
		ExtensionAppNotifyHelper.addAppBroadcastListenerExtension(this, "type1", ListenerClass.class);
		ExtensionAppNotifyHelper.addAppBroadcastListenerExtension(this, "type2", ListenerClass.class);
	}

	@Override
	public void init() {
		
	}

	@Override
	public int getPrivority() {
		return 0;
	}
	
	
}
