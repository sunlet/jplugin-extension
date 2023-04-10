package net.jplugin.extension.source_gen.extension;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.List;


import net.jplugin.cloud.rpc.client.annotation.BindRemoteService;
import net.jplugin.cloud.rpc.client.annotation.Protocol;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.CloudEnvironment;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.api.Para;

import javax.lang.model.type.IntersectionType;

public class SourceGenerator {


	public static String generate(InvocationContext ctx) {
		if (StringKit.isNotNull(ctx.getDynamicPath())){
			return "can't support dynamic service";
		}
		if (isPublicIp(ctx.getRequestInfo().getCallerIpAddress())) {
			return "NO";
		}

//		Method method = getMethod(ctx);
		Method method = ctx.getMethod();

		String spath = ctx.getServicePath();
		
		StringBuffer sb = new StringBuffer();
		HashSet<Class> imports=new HashSet();

		String bindAnno = tryGetBindAnnoClause(ctx);
		sb.append(bindAnno);
		imports.add(BindRemoteService.class);
		imports.add(Protocol.class);

		sb.append("public interface ").append(getClassName(spath,method)).append( " {");
		sb.append("\n\t");
		sb.append("public ").append(getReturnType(method,imports)).append(" ").append(method.getName()).append("(");
		sb.append(getParameters(method,imports));
		sb.append(");");
		sb.append("\n}");
		
		sb.insert(0, getImports(imports));
		return sb.toString();
	}

//	private static Method getMethod(InvocationContext ctx) {
//		return ctx.getMethod();
//		List<Extension> extList = PluginEnvirement.INSTANCE.getExtensionList("EP_SERVICE_EXPORT");
//		Extension target = extList.stream().filter(e -> {
//			return ctx.getServicePath().equals(e.getName());
//		}).findFirst().get();
//
//
//
//		return ReflactKit.findSingeMethodExactly(target.getFactory().getImplClass(), ctx.getMethod().getName());
//	}

	private static String tryGetBindAnnoClause(InvocationContext ctx) {
		String appCode = CloudEnvironment.INSTANCE._composeAppCode();
		return "@BindRemoteService(protocol = Protocol.rpc,url = \"esf://"+ appCode+ctx.getServicePath()+"\")\n";
	}

	private static Object getImports(HashSet<Class> imports) {
		
		StringBuffer sb = new StringBuffer("\n");
		
		for (Class imp:imports) {
			
			String name;
			if (imp.isArray()){
				name = getArrayCompName(imp);
			}else {
				name = imp.getName();
			}
			if (!"void".equals(name)){
				sb.append("import ").append(name).append(";");
				sb.append("\n");
			}
		}
		sb.append("\n");
		
		return sb.toString();
	}

	private static String getArrayCompName(Class imp) {
		if (imp.isArray()){
			return getArrayCompName(imp.getComponentType());
		}else{
			return imp.getName();
		}
	}

	private static boolean isPublicIp(String callerIpAddress) {
		return false;
	}

	private static Object getParameters(Method method, HashSet<Class> imports) {
		Parameter[] params = method.getParameters();
		StringBuffer sb = new StringBuffer();
		
		boolean first = true;
		for (Parameter p:params) {
			if (!first) {
				sb.append(" , ");	
			}else {
				first = false;
			}
//			String type = p.getType().getSimpleName();
			Type theType =p.getParameterizedType();

			imports.add(p.getType());
			String name = p.getName();

			sb.append(getTypeString(theType)).append(" ").append(name);
		}
		return sb.toString();
	}

	private static String getTypeString(Type gtype) {
		if (gtype instanceof ParameterizedType){
			return ((ParameterizedType)gtype).toString();
		}else if (gtype instanceof Class){
			return ((Class)gtype).getSimpleName();
		}else{
			//NOT SUPPORT
			return "";
		}
	}

	private static Object getReturnType(Method method, HashSet<Class> imports) {
		Type gtype = method.getGenericReturnType();
		if (gtype instanceof ParameterizedType){
			((ParameterizedType)gtype).getRawType();
			imports.add((Class)((ParameterizedType)gtype).getRawType());
			return gtype.toString();
		}else if (gtype instanceof Class){
			imports.add((Class)gtype);
			return ((Class)gtype).getSimpleName();
		}else{
			//NOT SUPPORT
			return "";
		}
	}

	private static String getClassName(String spath, Method method) {
		
		String preName = StringKit.isNull(spath)? "Service":spath;
		
		//根据路径获取前面一段的名字
		int pos = preName.lastIndexOf('/');
		if (pos>=0) {
			preName = preName.substring(pos+1);
		}
		preName = StringKit.replaceStr(preName,"-", "_");
		preName = preName.substring(0,1).toUpperCase()+preName.substring(1).toLowerCase();
		
		//根据方法名获取后面一段的名字
		String postName = method.getName();
		postName = postName.substring(0,1).toUpperCase()+ postName.substring(1);
		
		//拼接结果
		String result = "I"+preName+postName;
		
		//处理_, 首字母大写，_去掉，每一个_后面的字母大写
		String[] splits = StringKit.splitStr(result, "_");
		StringBuffer sb = new StringBuffer();
		for (String s:splits) {
			sb.append(s.substring(0,1).toUpperCase()).append(s.substring(1));
		}
		return sb.toString();

	}

}
