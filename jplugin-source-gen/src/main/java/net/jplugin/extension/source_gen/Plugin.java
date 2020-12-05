package net.jplugin.extension.source_gen;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import net.jplugin.extension.source_gen.extension.SourceGenFilter;

@PluginAnnotation
public class Plugin extends AbstractPlugin {
	
	public Plugin() {
		ExtensionWebHelper.addServiceFilterExtension(this, SourceGenFilter.class);
	}
	public void init() {
	}

	@Override
	public int getPrivority() {
		return -1;
	}

}
