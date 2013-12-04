package com.proxy;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//had a problem with cookies fix this!!!!
public class Blah extends  CookieHandler{

	@Override
	public Map<String, List<String>> get(URI x,Map<String, List<String>> y) throws IOException {
		// TODO Auto-generated method stub
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> l = retrieveCookies(x, y);
		map.put("Cookie",l);
        return Collections.unmodifiableMap(map);
	}

	@Override
	public void put(URI arg0, Map<String, List<String>> arg1)
			throws IOException {
		// TODO Auto-generated method stub
		
	}
	private List<String> retrieveCookies(URI uri, Map<String, List<String>> requestHeaders) {

		return null;
	}
}
