import java.util.ArrayList;

public class httpc {
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

		public Options(String[] args) {
			switch (args[0]) {
			case "help":
				System.out.println("Help");
				if (args.length != 1) {
					helpline = args[1];
					chooseHelpDetails(args[1]);
				} else {
					chooseHelpDetails("default");
				}
				break;
			case "get":
				System.out.println("Get");
				String[] modifiedArgsGet = new String[args.length - 1];
				System.arraycopy(args, 1, modifiedArgsGet, 0, modifiedArgsGet.length);
				getOptions = new GetOptions(modifiedArgsGet);
				break;
			case "post":
				System.out.println("Post");
				String[] modifiedArgsPost = new String[args.length - 1];
				System.arraycopy(args, 1, modifiedArgsPost, 0, modifiedArgsPost.length);
				postOptions = new PostOptions(modifiedArgsPost);
				break;

			}
		}
	}

	private static class GetOptions {
		private boolean isVerbose;
		private ArrayList<String> headers = new ArrayList<>();
		private String URL;

		public GetOptions(String[] args) {
			URL = args[args.length - 1];
			System.out.println("URL :: " + URL);
			switch (args[0]) {
			case "-v":
				isVerbose = true;
				switch (args[1]) {
				case "-h": {
					headers.add(args[2]);
					switch (args[3]) {
					case "-h": {
						int i = 4;
						headers.add(args[i]);
						System.out.println(i);
						while (i + 1 < args.length && args[i + 1].equals("-h")) {
							headers.add(args[i + 2]);
							i++;
						}
						break;
					}
					}
					System.out.println(headers);
					break;
				}
				default: {
					System.out.println("Verbose mode with URL");
					break;
				}
				}
				isVerbose = true;
				break;
			case "-h":
				headers.add(args[1]);
				switch (args[2]) {
				case "-h": {
					int i = 3;
					headers.add(args[i]);
					while (i + 1 < args.length && args[i + 1].equals("-h")) {
						headers.add(args[i + 2]);
						i++;
					}
					break;
				}
				}
				System.out.println(headers);
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

		public PostOptions(String[] args) {
			URL = args[args.length - 1];
			System.out.println("URL :: " + URL);
			switch (args[0]) {
			case "-v":
				isVerbose = true;
				break;
			case "-h":
				break;
			case "-d":
				data = args[1];
				break;
			case "-f":
				inputFilePath = args[1];
				break;
			}
		}
	}
}