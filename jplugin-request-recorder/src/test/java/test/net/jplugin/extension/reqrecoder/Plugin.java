package test.net.jplugin.extension.reqrecoder;



import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin  extends AbstractPlugin{

	public Plugin() {
		this.searchAndBindExtensions();
	}

	@Override
	public void init() {
		
	}

	@Override
	public int getPrivority() {
		return 0;
	}
	
	
}
