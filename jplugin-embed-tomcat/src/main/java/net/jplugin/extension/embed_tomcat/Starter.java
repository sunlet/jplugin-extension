package net.jplugin.extension.embed_tomcat;

import java.util.List;

import net.jplugin.core.kernel.api.BindStartup;
import net.jplugin.core.kernel.api.IStartup;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.PluginError;
import net.jplugin.extension.embed_tomcat.impl.TomcatController;

@BindStartup
public class Starter implements IStartup{

	
	@Override
	public void startFailed(Throwable th, List<PluginError> errors) {
	}

	@Override
	public void startSuccess() {
		TomcatController.startTomcat();
		for (int i=0;i<240;i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Boolean result = TomcatController.getStartResult();
			if (result !=null) {
				if (result) {
					PluginEnvirement.getInstance().getStartLogger().log("$$$ 嵌入式 Tomcat 启动完毕!");
					return;
				}else {
					throw new RuntimeException("内置Tomcat 启动失败！");
				}
			}
		}
		throw new RuntimeException("Tomcat 启动超时！");

	}

}
