package com.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * The ProxyServer class will spawn new threads to deal with incoming 
 * requests from the client and by using a threadpool 
 * will be able to save on resources. Otherwise we would be 
 * creating a new thread for every connection or request.
 * */


@SuppressWarnings("restriction")
public class ProxyServer extends Thread {

	//The blocked list if IP addresses. See BlockedList for more info
	static BlockedList bl = new BlockedList();
	Proxy p;
	
	public static void main(String[] args) throws IOException {
		
		HttpServer hs = HttpServer.create(new InetSocketAddress(8080), 0);
		hs.createContext("/", new Handler());
		System.out.println(hs.getAddress());
		// cached threadpool will make the program more efficient instead of
		// just creating a thread for each request
		hs.setExecutor(Executors.newCachedThreadPool());
		hs.start();
	}

	

	// is there an easy way to make this non-static in its own file.
	@SuppressWarnings("restriction")
	public static class Handler implements HttpHandler {
		
		private HashMap<String, StringBuffer> cache = new HashMap<String, StringBuffer>(
				1);
		ManagementConsole console = new ManagementConsole();

		public void handle(HttpExchange exchange) throws IOException {
			//s will print to the console window so the admin can see 
			s("something\n");
			s(exchange.getLocalAddress() + " is connected" + '\n');
			String requestURL = "";
			StringBuffer responseBuffer = new StringBuffer();

			int code;
			// DataOutputStream output = null;
			OutputStream os = exchange.getResponseBody();
			Headers reqHeaders = exchange.getRequestHeaders();
			Headers respHeaders = exchange.getResponseHeaders();
			Set<String> keySet = reqHeaders.keySet();

			List<String> head = reqHeaders.get("Host");
			URL reqURL = new URL("http://".concat(head.toString().substring(1,
					head.toString().length() - 1)));

			s("Client requested url: " + reqURL.toString() + '\n');
			System.out.println("CHECKING: " + reqURL.toString());

			code = openingConnection(reqURL, reqHeaders, respHeaders,
					responseBuffer);
			// System.out.println("Opening connection now");
			exchange.sendResponseHeaders(code, responseBuffer.length());
			os.write(responseBuffer.toString().getBytes());

			Iterator<String> it = keySet.iterator();

			os.flush();
			os.close();
			exchange.close();
			System.out.println("Cache size: " + cache.size());
		}

		public int openingConnection(URL url, Headers reqHeaders,
				Headers respHeaders, StringBuffer responseBuffer)
				throws IOException {
			int return_code = 0;
			InetAddress address = InetAddress.getByName(url.getHost());
			String ip = address.getHostAddress(); // gets urls address as IP's

			System.out.println("IP is: " + ip);
			HttpURLConnection httpcon = null;

			if (!(bl.isBad(ip))) {
				System.out.println("Not Blocked");
				//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.87", 8080));//tcdproxy ip
				
				System.setProperty("http.proxyHost", "");
				System.setProperty("http.proxyPort", "8080");
				
				httpcon = (HttpURLConnection) url.openConnection();

				
				
				//				Authenticator authenticator = new Authenticator() {
//
//			        public PasswordAuthentication getPasswordAuthentication() {
//			            return (new PasswordAuthentication("mcdowesj",
//			                    "password".toCharArray()));
//			        }
//			    };
//			    Authenticator.setDefault(authenticator);
				httpcon.setUseCaches(false);
				httpcon.setDoInput(true);
				httpcon.setDoOutput(/*true*/false); // false means this is a get req
//				httpcon.setRequestProperty("Content-type", "text/xml");
//				httpcon.setRequestProperty("Accept", "text/xml, application/xml");
//				httpcon.setRequestMethod("GET");
//				
				String tmp;

				if (reqHeaders.containsKey("Cookie")) {
					
					List<String> cookie = reqHeaders.get("Cookie");
					Iterator<String> it = cookie.iterator();
					while (it.hasNext()) {
						String mycookie = it.next();
						System.out.println(mycookie);
						mycookie = mycookie.substring(0, mycookie.length());
						httpcon.setRequestProperty("Cookie", mycookie);
					}
				}
				if (cache.containsKey(ip)) {
					System.out.println("Website in cache!");
					responseBuffer.append(cache.get(ip));
					System.out.println("Website: " + ip
							+ "/tStringbuffer length: "
							+ responseBuffer.length());
					 return return_code = HttpURLConnection.HTTP_ACCEPTED;
				} else {
					BufferedReader input = new BufferedReader(
							new InputStreamReader(httpcon.getInputStream()));
					System.out.println(input.toString().length());
					while ((tmp = input.readLine()) != null) {
						responseBuffer.append(tmp);
					}
					cache.put(ip, responseBuffer);
					
					input.close();
				}
				Set<String> keySet = reqHeaders.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String h = it.next();
					List<String> values = reqHeaders.get(h);
					s("Request: " + h + " = " + values.toString() + "\n");
				}
				return_code = 200;

								httpcon.connect();
			}else{
				System.out.printf("\n***** THIS IS A BLOCKED DOMAIN *****\n");
			}
			respHeaders.set("Content-Type", "text/html");
			
			//un set the proxy
			//System.setProperty("http.proxyHost", null);

			return return_code;
		}

		//had problems with cookies so this seemed to be the only solution to "fix" the problem
		public static void cookieHandling(String requestURL, String cname,
				String cvalue) {

			try {
				URL url = new URL("http://" + requestURL);
				HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
				urlcon.setRequestProperty("Cookie", cname + '=' + cvalue);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

		private void s(String send) {
			console.writeToScreen(send);
		}
	}
}