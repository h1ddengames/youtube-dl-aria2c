import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This script was created by Shahid Karim on 7/15/19.
 */
public class Downloader {
    private static String scriptVersion = "0.4a";
    private static String downloadFile = "";
    private static String downloadLocation = "";
    private static int numberOfThreads = 1;

    private static String YOUTUBE_DL_LOCATION = "";
    private static String PHANTOM_JS_LOCATION = "";
    private static String ARIA_2C_LOCATION = "";

    private static void printRequiredArguments() {
        System.out.println("downloadFile - The location of the file that contains URLS.");
        System.out.println("downloadLocation - The location where the files should be downloaded.");
        System.out.println("numberOfThreads - The number of threads to use in order to speed up the download process.");
        System.out.println("\nExample usage: java Downloader.java C:/DL.txt" + " " + "C:/Video Downloads" + " 10");
    }

    private static void checkArguments(String[] args) {
        if(args.length > 0) {
            // Options - The usual command line options such as -h, --help for help and -v, --version for version.
            if (args[0].contentEquals("-h") || args[0].contentEquals("--help")) {
                System.out.println("\nThis script is a command line wrapper written in Java that uses youtube-dl and aria2c " +
                        "to download videos when given a file that contains URLs. The required command line arguments are as follows: \n");
                printRequiredArguments();
            } else if (args[0].contentEquals("-v") || args[0].contentEquals("--version")) {
                System.out.println("Version " + scriptVersion);
            }
        }

        // Edge cases where there are either no inputs (too few arguments) or 4 or more inputs (too many arguments)
        if(args.length <= 0) {
            System.out.println("Not enough arguments. This script requires the following: \n");
            printRequiredArguments();
        } else if (args.length > 3) {
            System.out.println("Too many arguments.\n");
            printRequiredArguments();
        }

        // Normal case where there is more than 0 inputs but less than the 3 required inputs:
        if(args.length > 0 && args.length < 3) {
            System.out.println("Not enough arguments. This script requires the following: \n");
            printRequiredArguments();
        }

        // Normal case where there is exactly 3 arguments.
        if (args.length == 3) {
            downloadFile = args[0];
            downloadLocation = args[1];
            try {
                numberOfThreads = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("An integer is required as the third argument: numberOfThreads." + " Defaulting to 1 thread.");
            }

            // Do not allow the user to set numberOfThreads higher than the threads available on their CPU.
            if(numberOfThreads > Runtime.getRuntime().availableProcessors()) {
                numberOfThreads = Runtime.getRuntime().availableProcessors();
                System.out.println("The numberOfThreads specified was above the number of threads your CPU supports. " +
                        "Defaulting to the max amount possible on your CPU: " + numberOfThreads);
            }

            System.out.println("Starting the download script with the following inputs: ");
            System.out.println(downloadFile);
            System.out.println(downloadLocation);
            System.out.println(numberOfThreads);
        }
    }

    public static void setup() {
        YOUTUBE_DL_LOCATION = System.getProperty("user.dir") + "\\Youtubedl\\youtube-dl.exe";
        YOUTUBE_DL_LOCATION = YOUTUBE_DL_LOCATION.replace("\\", "/");

        PHANTOM_JS_LOCATION = System.getProperty("user.dir") + "\\PhantomJS\\bin\\phantomjs.exe";
        PHANTOM_JS_LOCATION = PHANTOM_JS_LOCATION.replace("\\", "/");

        ARIA_2C_LOCATION = System.getProperty("user.dir") + "\\Aria2c\\aria2c.exe";
        ARIA_2C_LOCATION = ARIA_2C_LOCATION.replace("\\", "/");
    }

    private static String youtubedl(String command, String url) {
        StringBuilder standardOutputStringBuilder = new StringBuilder();
        StringBuilder errorStringBuilder = new StringBuilder();

        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("cmd /c " + "\"" + YOUTUBE_DL_LOCATION + "\"" + command + " " + url);
            InputStream stdin = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            InputStream stderr = process.getErrorStream();
            InputStreamReader esr = new InputStreamReader(stderr);
            BufferedReader errorReader = new BufferedReader(esr);

            String line = null;
            while ( (line = br.readLine()) != null) {
                standardOutputStringBuilder.append(line);
            }
            while ( (line = errorReader.readLine()) != null) {
                errorStringBuilder.append(line);
            }

            //final Process p = Runtime.getRuntime().exec("cmd /c " + "\"" + YOUTUBE_DL_LOCATION + "\"" +" --get-url" + " " + url);
        } catch (IOException e) {
            System.out.println("Could not find the executable.");
        }

        return standardOutputStringBuilder.toString();
    }

    private static void download(String unconvertedUrl) {
        String convertedUrl = youtubedl(" --get-url", unconvertedUrl);
        String name = youtubedl(" --get-filename -o '%(title)s.%(ext)s' --restrict-filenames", unconvertedUrl);
        System.out.println(unconvertedUrl);
        System.out.println(name.replace("\'", ""));
        System.out.println(convertedUrl);
    }

    public static void main(String[] args) {
        checkArguments(args);
        setup();
        download("https://www.youtube.com/watch?v=dY6jR52fFWo");
    }
}