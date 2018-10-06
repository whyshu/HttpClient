import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class HttpClientMethods {
	private int port = 80;
	private int redirectCount = 0;

	public HttpClientMethods() {

	}

	public void getMethod(String url, boolean isVerbose, ArrayList<String> headers, String outputFileName) {
		try {
			int redirectCount = 0;
			HashMap<String, ArrayList<String>> headersMap = new HashMap<>();
			Map<String, String> responseMap = new HashMap<>();
			URL url_Object = new URL(url);
			Socket socket_ = new Socket(InetAddress.getByName(url_Object.getHost()), port);
			PrintWriter Writer = new PrintWriter(socket_.getOutputStream());
			Writer.println("GET /" + url_Object.getFile() + " HTTP/1.0");
			Writer.println("Host: " + url_Object.getHost());
			if (!headers.isEmpty()) {
				headers.stream().forEach(x -> Writer.println(x));
			}
			// if (!headers.isEmpty()) {
			// for (String header : headers) {
			// ArrayList<String> headerValues = new ArrayList<>();
			// if (headersMap.containsKey(header.split(":")[0]))
			// headersMap.get(header.split(":")[0]).add(header.split(":")[1]);
			// else {
			// headerValues.add(header.split(":")[1]);
			// headersMap.put(header.split(":")[0], headerValues);
			// }
			// }
			// }
			// for (Map.Entry<String, ArrayList<String>> entry :
			// headersMap.entrySet()) {
			// String currentHeaderValue = "";
			// for (String value : entry.getValue()) {
			// if (currentHeaderValue == "")
			// currentHeaderValue += value;
			// else
			// currentHeaderValue += "," + value;
			// }
			// Writer.println(entry.getKey() + ":" + currentHeaderValue);
			// }
			Writer.println();
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
			String returnedUrl = isUrlRedirect(responseMap.get("header"), url, responseMap.get("content"),isVerbose,outputFileName);
			if (returnedUrl.equals(url)) {
				if (isVerbose) {
					if (outputFileName == null) {
						System.out.println(responseMap.get("header"));
						System.out.println(responseMap.get("content"));
					} else {
						writeToFile(outputFileName, responseMap.get("header"));
						writeToFile(outputFileName, responseMap.get("content"));
					}
				} else {
					if (outputFileName == null)
						System.out.println(responseMap.get("content"));
					else
						writeToFile(outputFileName, responseMap.get("content"));
				}
			} else {
				if (returnedUrl.equals("")) {
					System.out.println(responseMap.get("header"));
					String[] headerLines = responseMap.get("header").split("\n");
					List<String> lines = Arrays.asList(headerLines);
					lines = lines.stream().filter(x -> x.contains("Location:")).collect(Collectors.toList());
					if (lines.size() == 1)
						getMethod(lines.get(0).split("Location:")[1], isVerbose, headers, outputFileName);
				} else {
					getMethod(returnedUrl, isVerbose, headers, outputFileName);
				}
			}

		} catch (Exception e) {
			System.out.println("Exception in get method " + e.getMessage());
		}
	}

	private String isUrlRedirect(String header, String url, String content, boolean isVerbose, String outputFileName) {
		if (redirectCount >= 5) {
			System.out.println("Redirected more than 5 times!");
			if (isVerbose) {
				if (outputFileName == null) {
					System.out.println(header);
					System.out.println(content);
				} else {
					writeToFile(outputFileName, header);
					writeToFile(outputFileName, content);
				}
			} else {
				if (outputFileName == null)
					System.out.println(content);
				else
					writeToFile(outputFileName, content);
			}
			System.exit(1);
		}
		String[] headerLines = header.split("\n");
		redirectCount++;
//		try {
//			if (headerLines[0].contains("3")) {
//				URLConnection con = new URL(url).openConnection();
//				// System.out.println("orignal url: " + con.getURL());
//				con.connect();
//				// System.out.println("connected url: " + con.getURL());
//				InputStream is = con.getInputStream();
//				//System.out.println("redirected url: " + con.getURL());
//				is.close();
//				return con.getURL().toString();
//			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			return "";
	//	}
		return "";
	}

	public void postMethod(String url, boolean isVerbose, ArrayList<String> headers, String data, String inputFilePath,
			String outputFileName) {
		HashMap<String, ArrayList<String>> headersMap = new HashMap<>();
		try {
			Map<String, String> responseMap = new HashMap<>();
			URL url_Object = new URL(url);
			if (data == null) {
				data = "";
				File file = new File(inputFilePath);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String st;
				while ((st = br.readLine()) != null) {
					data += st;
				}
			}
			JSONObject jsonObj = new JSONObject(data);
			Socket socket_ = new Socket(InetAddress.getByName(url_Object.getHost()), port);
			PrintWriter Writer = new PrintWriter(socket_.getOutputStream());
			Writer.println("POST /" + url_Object.getFile() + " HTTP/1.0");
			Writer.println("Host: " + url_Object.getHost());
			Writer.println("Content-Length: " + jsonObj.toString().length());
			if (!headers.isEmpty()) {
				headers.stream().forEach(x -> Writer.println(x));
			}
			// if (!headers.isEmpty()) {
			// for (String header : headers) {
			// ArrayList<String> headerValues = new ArrayList<>();
			// if (headersMap.containsKey(header.split(":")[0]))
			// headersMap.get(header.split(":")[0]).add(header.split(":")[1]);
			// else {
			// headerValues.add(header.split(":")[1]);
			// headersMap.put(header.split(":")[0], headerValues);
			// }
			// }
			// }
			// for (Map.Entry<String, ArrayList<String>> entry :
			// headersMap.entrySet()) {
			// String currentHeaderValue = "";
			// for (String value : entry.getValue()) {
			// if (currentHeaderValue == "")
			// currentHeaderValue += value;
			// else
			// currentHeaderValue += "," + value;
			// }
			// Writer.println(entry.getKey() + ":" + currentHeaderValue);
			// }

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
					responseMap.put("header", response.toString());
					headerDone = true;
					response = new StringBuilder();
				}
			}
			bufferedReader.close();
			Writer.close();
			socket_.close();
			responseMap.put("content", response.toString());
			String returnedUrl = isUrlRedirect(responseMap.get("header"), url,responseMap.get("content"),isVerbose,outputFileName);
			if (returnedUrl.equals(url)) {
				if (isVerbose) {
					if (outputFileName == null) {
						System.out.println(responseMap.get("header"));
						System.out.println(responseMap.get("content"));
					} else {
						writeToFile(outputFileName, responseMap.get("header"));
						writeToFile(outputFileName, responseMap.get("content"));
					}
				} else {
					if (outputFileName == null)
						System.out.println(responseMap.get("content"));
					else
						writeToFile(outputFileName, responseMap.get("content"));
				}
			} else {
				if (returnedUrl.equals("")) {
					System.out.println(responseMap.get("header"));
					String[] headerLines = responseMap.get("header").split("\n");
					List<String> lines = Arrays.asList(headerLines);
					lines = lines.stream().filter(x -> x.contains("Location:")).collect(Collectors.toList());
					if (lines.size() == 1)
						getMethod(lines.get(0).split("Location:")[1], isVerbose, headers, outputFileName);
				} else {
					getMethod(returnedUrl, isVerbose, headers, outputFileName);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in POST Method :: " + e.getMessage());
		}
	}

	public static void writeToFile(String filename, String content) {
		try {
			File file = new File(filename);
			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.newLine();
				bw.close();
			} else {
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.newLine();
				bw.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
