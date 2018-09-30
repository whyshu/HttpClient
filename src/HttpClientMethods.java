import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class HttpClientMethods {
	private int port = 80;

	public HttpClientMethods() {

	}

	public void getMethod(String url, boolean isVerbose, ArrayList<String> headers) {
		try {
			Map<String, String> responseMap = new HashMap<>();
			URL url_Object = new URL(url);
			Socket socket_ = new Socket(InetAddress.getByName(url_Object.getHost()), port);
			PrintWriter Writer = new PrintWriter(socket_.getOutputStream());
			Writer.println("GET /" + url_Object.getFile() + " HTTP/1.0");
			Writer.println("Host: " + url_Object.getHost());

			if (!headers.isEmpty())
				headers.stream().forEach(x -> Writer.println(x));

			Writer.println("");
			Writer.flush();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			boolean headerDone = false;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line + "\n");
				if (line.isEmpty() && !headerDone) {
					responseMap.put("header", response.toString());
					headerDone = true;
					response = new StringBuilder();
				}
			}

			bufferedReader.close();
			Writer.close();
			socket_.close();

			responseMap.put("content", response.toString());
			if (isVerbose) {
				System.out.println(responseMap.get("header"));
				System.out.println(responseMap.get("content"));
			} else {
				System.out.println(responseMap.get("content"));
			}
		} catch (Exception e) {
			System.out.println("Exception in get method " + e.getMessage());
		}
	}

	public void postMethod(String url, boolean isVerbose, ArrayList<String> headers, String data,
			String inputFilePath) {
		try {
			Map<String, String> responseMap = new HashMap<>();
			URL url_Object = new URL(url);
			data+="}";
			Socket socket_ = new Socket(InetAddress.getByName(url_Object.getHost()), port);
			PrintWriter Writer = new PrintWriter(socket_.getOutputStream());
			Writer.println("POST /" + url_Object.getFile() + " HTTP/1.0");
			Writer.println("Host: " + url_Object.getHost());
			Writer.println("Content-Length: " + data.length());
			if (!headers.isEmpty())
				headers.stream().forEach(x -> Writer.println(x));
			JSONObject jsonObj = new JSONObject(data);
			Writer.println();
			Writer.println(jsonObj);
			Writer.println();
			Writer.flush();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			boolean headerDone = false;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line + "\n");
				if (line.isEmpty() && !headerDone) {
					headerDone = true;
					response = new StringBuilder();
				}
			}
			bufferedReader.close();
			Writer.close();
			socket_.close();
			responseMap.put("content", response.toString());
			System.out.println(responseMap.get("content"));
		} catch (Exception e) {
			System.out.println("Exception in POST Method :: " + e.getMessage());
		}
	}
}
