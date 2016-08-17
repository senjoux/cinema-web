package utility;


import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

public class ServiceLocator {
	private static ServiceLocator instance;
	private Context context;
	private Map<String, Object> cache;
	
	private ServiceLocator(){
		cache = new HashMap<String,Object>();
		try {
			context = new InitialContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ServiceLocator getInstance(){
		if(instance==null){
			instance=new ServiceLocator();
		}
		return instance;
	}
	
	public Object getProxy(String jndi){
		Object proxy=null;
		proxy=cache.get(jndi);
		if(proxy!=null){
			return proxy;
		}else{
			try {
				proxy=context.lookup(jndi);
				cache.put(jndi, proxy);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return proxy;
		}
	}
}
