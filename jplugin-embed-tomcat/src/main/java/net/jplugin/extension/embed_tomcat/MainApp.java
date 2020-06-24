package net.jplugin.extension.embed_tomcat;

import net.jplugin.core.kernel.PluginApp;
import net.jplugin.core.kernel.api.PluginAutoDetect;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class MainApp {

	public static void main(String[] args) {
		PluginAutoDetect.addAutoDetectPackage("net.jplugin.extension");
		PluginApp.main(null);
	}

}
