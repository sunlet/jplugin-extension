package net.jplugin.extension.appnotify;


import net.jplugin.extension.appnotify.api.IAppBroadcastListener;
import net.jplugin.extension.appnotify.export.INotifyExport;
import net.jplugin.extension.appnotify.export.NotifyExport;
import net.jplugin.extension.appnotify.impl.NotifyServiceManager;

import net.jplugin.cloud.rpc.client.api.ExtensionESFHelper;
import net.jplugin.core.config.api.CloudEnvironment;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

	public static final String EP_APP_NOTIFY = "EP_APP_NOTIFY";

	public Plugin() {
		if (enabled()){
			PluginEnvirement.INSTANCE.getStartLogger().log("$$$ platform.app-notify-enabled is true.");
			this.addExtensionPoint(ExtensionPoint.create(EP_APP_NOTIFY,IAppBroadcastListener.class,true));
			
//			String appcode = AppEnvirement.INSTANCE.getBasicConfiguration().getAppCode();
			String appcode = CloudEnvironment.INSTANCE._composeAppCode();
			ExtensionESFHelper.addRpcJsonProxyExtension(this, INotifyExport.class, "esf://"+appcode+NotifyExport.SERVICE_PATH);
		}else{
			PluginEnvirement.INSTANCE.getStartLogger().log("$$$ platform.app-notify-enabled is false.");
		}
	}
	
	@Override
	public void onCreateServices() {
		if (enabled()){
			NotifyServiceManager.me.init();
		}
	}
	
	public static boolean enabled(){
		return "true".equalsIgnoreCase(ConfigFactory.getStringConfig("platform.app-notify-enabled")); 
	}
	public static void assertEnabled(){
		if (!enabled()){
			throw new RuntimeException("You want to use app notify feature, but [platform.app-notify-enabled] not configed");
		}
	}
	

	@Override
	public int getPrivority() {
		return -2;
	}

	public void init() {
		
	}

}
