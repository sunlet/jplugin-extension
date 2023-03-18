package net.jplugin.extension.appnotify.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BroadcastMessage {
	
	private BroadcastMessage() {
	}
	
	public static BroadcastMessage create(String type) {
		BroadcastMessage o = new BroadcastMessage();
		o.setType(type);
		return o;
	}
	
	String type;
	Map<String,String> attributes = new HashMap<String, String>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("msg-type:").append(this.type).append(" {");
		
		for (Entry<String, String> att:attributes.entrySet()) {
			sb.append(att.getKey()).append(",").append(att.getValue());
		}
		sb.append("}");
		return sb.toString();
		
	}
	
}
