package test.net.jplugin.extension.reqrecoder;


import net.jplugin.ext.webasic.api.BindServiceExport;

@BindServiceExport(path = "/call")
public class TestExport {
	public String type1(String p) {
		System.out.println("@@@@@"+p);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	public String type2(String p) {
		System.out.println("@@@@@"+p);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

}
