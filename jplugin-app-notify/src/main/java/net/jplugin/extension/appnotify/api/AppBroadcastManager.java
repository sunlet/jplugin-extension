package net.jplugin.extension.appnotify.api;

import java.net.InetAddress;
import java.util.List;


import net.jplugin.extension.appnotify.export.NotifyExport;


import net.jplugin.cloud.rpc.client.api.RpcContext;
import net.jplugin.cloud.rpc.client.api.RpcContextManager;
import net.jplugin.common.kits.IpKit;
import net.jplugin.core.config.api.CloudEnvironment;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;

public class AppBroadcastManager {

	public static AppBroadcastManager INSTANCE = new AppBroadcastManager();
	
	Logger logger = LogFactory.getLogger(AppBroadcastManager.class);

	private static String localIp;

	private static String esfPort;
	
	public AppBroadcastManager(){
		try{
			localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	/**
	 * 调用这个方法实现通知本应用集群的所有实例
	 * @param msg
	 * @return
	 */
	public Result sendMessage(BroadcastMessage msg){
		return sendMessage(msg,true);
	}
	
//	public Result sendMessage(BroadcastMessage msg,boolean includeSelf) {
//		int totalNum=0;
//		int successNum=0;
//		List<RPClientContext> clientList = ClientContextUtil.getClientContext(AppEnvirement.INSTANCE.getBasicConfiguration().getAppCode());
//
//		for (RPClientContext clientContext:clientList){
//			if (!includeSelf && isSelf(clientContext)){
//				continue;
//			}
//
//			totalNum ++;
//			try {
//				clientContext.invoke(NotifyExport.SERVICE_PATH,"handleMessage", new Object[]{msg});
//				successNum++;
//			} catch (Exception e) {
//				logger.error("notify gate chagne error.{}",msg,e);
//			}
//		}
//		Result result = new Result();
//		result.setTotalNum( totalNum);
//		result.setSuccessNum(successNum);
//		return result;
//	}

	public Result sendMessage(BroadcastMessage msg,boolean includeSelf) {
		int totalNum=0;
		int successNum=0;


		List<RpcContext> list = ServiceFactory.getService(RpcContextManager.class).getNodeContextList(CloudEnvironment.INSTANCE._composeAppCode());

		for (RpcContext context:list){
			if (!includeSelf && isSelf(context)){
				continue;
			}

			totalNum ++;
			try {
				context.invoke(NotifyExport.SERVICE_PATH,"handleMessage", new Object[]{msg});
				successNum++;
			} catch (Exception e) {
				logger.error("notify gate chagne error.{}",msg,e);
			}
		}
		Result result = new Result();
		result.setTotalNum( totalNum);
		result.setSuccessNum(successNum);
		return result;
	}
	static boolean init=false;
	public static void initLocalConfig(){
		if (!init){
			synchronized (AppBroadcastManager.class) {
				localIp = IpKit.getLocalIp();
				esfPort = CloudEnvironment.INSTANCE._getRpcPortWithEmbbedTomcat() +"";
				init = true;	
			}
		}
	}
	
	
	private boolean isSelf(RpcContext clientContext) {
		initLocalConfig();
		
		return (localIp.equals(clientContext.getRemoteHostIp())
				&& esfPort.equals(clientContext.getRemoteHostPort()));
	}

}
