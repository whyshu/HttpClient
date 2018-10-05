import java.util.ArrayList;
import java.util.Arrays;

public class HttpClientMain {
	private static void chooseHelpDetails(String helpMethod) {
		switch (helpMethod) {
		case "default":
			System.out.println("httpc is a curl-like application but supports "
					+ "HTTP protocol only. \n Usage:\n\t\t httpc command [arguments] "
					+ "\nThe commands are: \n\t\tget executes a HTTP GET request "
					+ "and prints the response.\n\t\tpost executes a HTTP " + "POST request and prints the response. "
					+ "\n\t\thelp prints this screen. Use httpc "
					+ "\n\nhelp [command] for more information about a command.");
			break;
		case "get":
			System.out.println("\nusage: httpc get [-v] [-h key:value]" + " URL"
					+ "\n\nGet executes a HTTP GET request " + "for a given URL. \n\t\t-v Prints the detail "
					+ "of the response such as protocol, status, and headers."
					+ "\n\t\t-h key:value Associates headers to HTTP Request with the format 'key:value'.");
			break;
		case "post":
			System.out.println("\nusage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL"
					+ "\n\nPost executes a HTTP POST request for a given URL with inline data or from file."
					+ "for a given URL. \n\t\t-v -v Prints the detail of the response such as protocol, status, and headers. "
					+ "\n\t\t-h key:value Associates headers to HTTP Request with the format 'key:value'."
					+ "\n\t\t-d string Associates an inline data to the body HTTP POST request. "
					+ "\n\t\t-f file Associates the content of a file to the body HTTP POST request."
					+ "\nEither [-d] or [-f] can be used but not both.");
			break;
		}
	}

	public static void main(String[] args) {
		Options options = new Options(args);
	}

	/**
	 * This is the main container which will be populated by picocli with values
	 * from the arguments.
	 */
	private static class Options {

		private String helpline = "default";
		private GetOptions getOptions = null;
		private PostOptions postOptions = null;
		HttpClientMethods httpClientMethods = new HttpClientMethods();

		public Options(String[] args) {
			switch (args[0]) {
			case "help":
				// System.out.println("Help");
				if (args.length != 1) {
					helpline = args[1];
					chooseHelpDetails(args[1]);
				} else {
					chooseHelpDetails("default");
				}
				break;
			case "get":
				String[] modifiedArgsGet = new String[args.length - 1];
				System.arraycopy(args, 1, modifiedArgsGet, 0, modifiedArgsGet.length);
				getOptions = new GetOptions(modifiedArgsGet);
				getOptions.headers.add("User-Agent: Concordia-HTTP/1.0");
				httpClientMethods.getMethod(getOptions.URL, getOptions.isVerbose, getOptions.headers, getOptions.outputFileName);
				break;
			case "post":
				String[] modifiedArgsPost = new String[args.length - 1];
				System.arraycopy(args, 1, modifiedArgsPost, 0, modifiedArgsPost.length);
				postOptions = new PostOptions(modifiedArgsPost);
				postOptions.headers.add("User-Agent: Concordia-HTTP/1.0");
				httpClientMethods.postMethod(postOptions.URL, postOptions.isVerbose, postOptions.headers,
						postOptions.data, postOptions.inputFilePath,postOptions.outputFileName);
				break;

			}
		}
	}

	private static class GetOptions {
		private boolean isVerbose;
		private ArrayList<String> headers = new ArrayList<>();
		private String URL;
		private String outputFileName;

		private void addToHeaders(int i, String[] args) {
			headers.add(args[i]);
			while (i + 1 < args.length && args[i + 1].equals("-h")) {
				headers.add(args[i + 2]);
				i+=2;
			}
		}

		public GetOptions(String[] args) {
			if (Arrays.asList(args).contains("-o")) {
				outputFileName = args[args.length - 1];
				URL = args[args.length - 3];
			} else {
				URL = args[args.length - 1];
			}
			// System.out.println("URL :: " + URL);
			switch (args[0]) {
			case "-v":
				isVerbose = true;
				if (args.length > 1) {
					if (args[1].equals("-h")) {
						headers.add(args[2]);
					}
					if (args.length > 3) {
						// Check if there are more headers
						if (args[3].equals("-h")) {
							addToHeaders(4, args);
						}
					}
				}
				break;
			case "-h":
				headers.add(args[1]);
				if (args.length > 2) {
					// Check if there are more headers
					if (args[2].equals("-h")) {
						addToHeaders(3, args);
					}
				}
				break;
			}
		}
	}

	private static class PostOptions {
		private String URL;
		private boolean isVerbose;
		private ArrayList<String> headers = new ArrayList<>();
		private String data;
		private String inputFilePath;
		private String  outputFileName;
		
		private void addToHeaders(int i, String[] args) {
			headers.add(args[i]);
			// System.out.println(i);
			while (i + 1 < args.length && args[i + 1].equals("-h")) {
				headers.add(args[i + 2]);
				i+=2;
			}
		}

		private void checkForDataOrFileInput(String[] args) {
			if (Arrays.asList(args).contains("--d")) {
				int dataPos = Arrays.asList(args).indexOf("--d");
				data = args[dataPos + 1];
			} else if (Arrays.asList(args).contains("-f")) {
				int filePos = Arrays.asList(args).indexOf("-f");
				inputFilePath = args[filePos + 1];
			}
		}

		public PostOptions(String[] args) {
			if (Arrays.asList(args).contains("-o")) {
				outputFileName = args[args.length - 1];
				URL = args[args.length - 3];
			} else {
				URL = args[args.length - 1];
			}
			switch (args[0]) {
			case "-v":
				isVerbose = true;
				if (args.length > 1) {
					if (args[1].equals("-h")) {
						headers.add(args[2]);
					}
					if (args.length > 3) {
						// Check if there are more headers
						if (args[3].equals("-h")) {
							addToHeaders(4, args);
						}
					}
					checkForDataOrFileInput(args);
				}
				break;
			case "-h":
				headers.add(args[1]);
				if (args.length > 2) {
					// Check if there are more headers
					if (args[2].equals("-h")) {
						addToHeaders(3, args);
					}
					checkForDataOrFileInput(args);
				}
				break;
			case "--d":
				data = args[1];
				break;
			case "-f":
				inputFilePath = args[1];
				break;
			}
			// System.out.println(headers);
			// System.out.println(data);
			// System.out.println(inputFilePath);
		}
	}
}