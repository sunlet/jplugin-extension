package net.jplugin.extension.reqrecoder;


import net.jplugin.core.config.ExtensionConfigHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import net.jplugin.extension.reqrecoder.impl.LogRecorderConfig;
import net.jplugin.extension.reqrecoder.impl.ReqLogFilter;
import net.jplugin.extension.reqrecoder.impl.ReqRecorderCfgChangeHandler;

@PluginAnnotation
public class Plugin extends AbstractPlugin {
	public Plugin() {
		this.searchAndBindExtensions();
		ExtensionWebHelper.addHttpFilterExtension(this, ReqLogFilter.class);
		
		ExtensionConfigHelper.addConfigChangeHandlerExtension(this, "request-recorder", ReqRecorderCfgChangeHandler.class);
	}

	@Override
	public void onCreateServices() {
		LogRecorderConfig.init();
	}
	@Override
	public void init() {
		
	}

	@Override
	public int getPrivority() {
		return -1;
	}

}
