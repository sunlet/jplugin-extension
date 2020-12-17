package net.jplugin.extension.source_gen.extension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;



import net.jplugin.common.kits.StringKit;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.api.Para;

public class SourceGenerator {

	private static Method method;

	public static String generate(InvocationContext ctx) {
		if (StringKit.isNotNull(ctx.getDynamicPath())){
			return "can't support dynamic service";
		}
		if (isPublicIp(ctx.getRequestInfo().getCallerIpAddress())) {
			return "NO";
		}
		
		
		Method method = ctx.getMethod();
		String spath = ctx.getServicePath();
		
		StringBuffer sb = new StringBuffer();
		HashSet<Class> imports=new HashSet();
//		sb.append("@BindRemoteServiceProxy(protocol = ProxyProtocol.rpc_json,url = \"esf://").append()/custmgr\"))
		
		String bindAnno = tryGetBindAnnoClause(ctx);
		if (StringKit.isNotNull(bindAnno)) {
			sb.append("import com.haiziwang.platform.esf.client.annotation.BindRemoteServiceProxy;\n");
			sb.append("import com.haiziwang.platform.esf.client.annotation.BindRemoteServiceProxy.ProxyProtocol;\n\n");
			sb.append(bindAnno);
		}
		
		sb.append("public interface ").append(getClassName(spath,method)).append( " {");
		sb.append("\n\t");
		sb.append("public ").append(getReturnType(ctx,imports)).append(" ").append(method.getName()).append("(");
		sb.append(getParameters(ctx,imports));
		sb.append(");");
		sb.append("\n}");
		
		sb.insert(0, getImports(imports));
		return sb.toString();
	}

	private static String tryGetBindAnnoClause(InvocationContext ctx) {
		try {
			Class<?> clazz = Class.forName("com.haiziwang.platform.appclient.api.AppEnvirement");
			Field field = clazz.getField("INSTANCE");
			Object inst = field.get(null);
			method = clazz.getMethod("getBasicConfiguration", new Class[] {});
			Object baseInfoInst = method.invoke(inst, new Class[] {});
			method = baseInfoInst.getClass().getMethod("getAppCode", new Class[] {});
			Object appCode = method.invoke(baseInfoInst, new Class[] {});
			
			return "@BindRemoteServiceProxy(protocol = ProxyProtocol.rpc_json,url = \"esf://"+ appCode+ctx.getServicePath()+"\")\n";
			
		}catch(Exception e) {
			return "";
		}
		
	}

	private static Object getImports(HashSet<Class> imports) {
		
		StringBuffer sb = new StringBuffer("\n");
		
		for (Class imp:imports) {
			
			String name = imp.getName();
			if (!"void".equals(name)){
				sb.append("import ").append(name).append(";");
				sb.append("\n");
			}
		}
		sb.append("\n");
		
		return sb.toString();
	}

	private static boolean isPublicIp(String callerIpAddress) {
		return false;
	}

	private static Object getParameters(InvocationContext ctx, HashSet<Class> imports) {
		Parameter[] params = ctx.getMethod().getParameters();
		StringBuffer sb = new StringBuffer();
		
		boolean first = true;
		for (Parameter p:params) {
			if (!first) {
				sb.append(" , ");	
			}else {
				first = false;
			}
			String type = p.getType().getSimpleName();
			imports.add(p.getType());
			String name = p.getName();
			
			Para anno = p.getAnnotation(Para.class);
			
			if (anno!=null) {
				name =  anno.name();
//				sb.append("@Para(name=\""+name+"\") ");
			}
			sb.append(type).append(" ").append(name);
		}
		return sb.toString();
	}

	private static Object getReturnType(InvocationContext ctx, HashSet<Class> imports) {
		Class<?> type = ctx.getMethod().getReturnType();
		imports.add(type);
		return type.getSimpleName();
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
