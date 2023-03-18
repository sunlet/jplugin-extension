package net.jplugin.extension.appnotify;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

public class ExtensionAppNotifyHelper {

	public static void addAppBroadcastListenerExtension(AbstractPlugin p,String msgType,Class listenerClazz) {
		Plugin.assertEnabled();
		p.addExtension(Extension.create(Plugin.EP_APP_NOTIFY,msgType, listenerClazz));
	}
}
