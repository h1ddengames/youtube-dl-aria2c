import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This script was created by Shahid Karim on 7/15/19.
 * Sample usage: java Downloader.java "C:/DL.txt" "C:/Video Downloads" 5
 * Sample usage: java Downloader.java -h
 * java - The executable: uses java to run this script.
 * Downloader.java - The name of this script. Tells Java to run this script.
 * "C:/DL.txt" - Place the " " (quotation marks) around the location that contains your txt file that contains the URLs to download.
 * "C:/Video Downloads" - Place the " " (quotation marks) around the location where you want to save the media files.
 * 5 - A number between 1 and the maximum number of threads your cpu allows.
 */
public class Downloader {
    private static String scriptVersion = "0.7a";
    // The .txt file containing a single URL on each line.
    // Look at DLExample.txt in the /resources folder for an example.
    private static String downloadFile;
    // The location where the media files should be downloaded.
    private static String downloadLocation;
    // How many threads should be used to speed up the download of all the media in the downloadFile.
    private static int numberOfThreads = 1;

    // The location of the executables required to download the media files.
    private static String YOUTUBE_DL_LOCATION;
    private static String PHANTOM_JS_LOCATION;
    private static String ARIA_2C_LOCATION;

    // Store the URLs from the downloadFile into an ArrayList in memory.
    private static List<String> urlsToDownload = new ArrayList<String>();

    /**
     * Print the arguments that the user should input from the command line.
     */
    private void printRequiredArguments() {
        System.out.println("downloadFile - The location of the file that contains URLS.");
        System.out.println("downloadLocation - The location where the files should be downloaded.");
        System.out.println("numberOfThreads - The number of threads to use in order to speed up the download process.");
        System.out.println("\nExample usage: java Downloader.java C:/DL.txt" + " " + "C:/Video Downloads" + " 10");
        System.exit(-1);
    }

    /**
     * Checks the arguments to make sure that there is the correct number of arguments and also supports using -v, --version, -h, and --help.
     * @param args The command line arguments: downloadFile downloadLocation numberOfThreads
     */
    private void checkArguments(String[] args) {
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
        if (args.length == 3) { checkForValidInputs(args); }
    }

    /**
     * Checks that the arguments themselves are correct for what they need to do.
     * @param args The command line arguments: downloadFile downloadLocation numberOfThreads
     */
    private void checkForValidInputs(String[] args) {
        // Set the variables to the given arguments.
        downloadFile = args[0];
        downloadLocation = args[1];

        // If the final argument is not an integer, default to 1 thread.
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

        // Checks that the argument corresponding to the downloadFile variable is a file and it exists.
        if(new File(downloadFile).exists()) {
            System.out.println("Download file: file exists.");
        } else{ System.err.println("Error! Download file: " + downloadFile + " does not exist. Exiting..."); System.exit(-1); }

        // Checks that the argument corresponding to the downloadLocation variable is a directory and it exists.
        if(new File(downloadLocation).isDirectory()) {
            System.out.println("Download location: directory exists.");
        } else{ System.err.println("Error! Download location: " + downloadLocation + " does not exist or is not a directory. Exiting..."); System.exit(-1);  }

        System.out.println("Inputs have been verified.");
        System.out.println("----------------------------------------------------------------------");

        System.out.println("Starting the download script with the following inputs: ");
        System.out.println("File location: " + downloadFile);
        System.out.println("Download location: " + downloadLocation);
        System.out.println("Number of threads: " + numberOfThreads);
        System.out.println("----------------------------------------------------------------------");
    }

    /**
     * 1. Checks the arguments to make sure that there is the correct number of arguments and also supports using -v, --version, -h, and --help.
     * 2. Once the proper number of arguments are found, make sure that the arguments themselves are correct for what they need to do:
     * ie. downloadFile should exist AND point to a file NOT a directory.
     * ie. downloadLocation should exist AND point to a directory NOT a file. (Note: this is the opposite of downloadFile)
     * ie. numberOfThreads is a number above 0 but less than or equal to the maximum number of threads your cpu supports.
     * @param args The command line arguments: downloadFile downloadLocation numberOfThreads
     */
    public void setup(String[] args) {
        checkArguments(args);

        YOUTUBE_DL_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\programs\\Youtubedl\\youtube-dl.exe";
        YOUTUBE_DL_LOCATION = YOUTUBE_DL_LOCATION.replace("\\", "/");

        PHANTOM_JS_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\programs\\PhantomJS\\bin\\phantomjs.exe";
        PHANTOM_JS_LOCATION = PHANTOM_JS_LOCATION.replace("\\", "/");

        ARIA_2C_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\programs\\Aria2c\\aria2c.exe";
        ARIA_2C_LOCATION = ARIA_2C_LOCATION.replace("\\", "/");
    }

    /**
     * Runs the youtube-dl executable in order to find the media url and the filename.
     * @param command The command to run (either --get-url or --get-filename)
     * @param url The url that contains the media that needs to be downloaded.
     * @return A string that contains either the media url or the filename based on which command was run.
     */
    private String youtubedl(String command, String url) {
        System.out.println("Running the command: youtube-dl" + command + " for url: " + url);
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
            while ( (line = br.readLine()) != null) { standardOutputStringBuilder.append(line); }
            while ( (line = errorReader.readLine()) != null) { errorStringBuilder.append(line); }

            //final Process p = Runtime.getRuntime().exec("cmd /c " + "\"" + YOUTUBE_DL_LOCATION + "\"" +" --get-url" + " " + url);
        } catch (IOException e) { System.out.println("Could not find the executable."); }

        return standardOutputStringBuilder.toString();
    }

    /**
     * Runs the aria2c executable in order download the media contained within the given url.
     * @param filename The filename obtained from running youtube-dl.
     * @param url The url obtained from running youtube-dl.
     * @return The output of running the aria2c executable.
     */
    private String aria2c(String filename, String url) {
        StringBuilder standardOutputStringBuilder = new StringBuilder();
        StringBuilder errorStringBuilder = new StringBuilder();

        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("cmd /c " + "\"" + ARIA_2C_LOCATION + "\"" + " --auto-file-renaming=false -c -x5 -j7 -s5 -o " + filename + " -d " + downloadLocation + url);
            InputStream stdin = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            InputStream stderr = process.getErrorStream();
            InputStreamReader esr = new InputStreamReader(stderr);
            BufferedReader errorReader = new BufferedReader(esr);

            String line = null;
            while ( (line = br.readLine()) != null) { standardOutputStringBuilder.append(line); }
            while ( (line = errorReader.readLine()) != null) { errorStringBuilder.append(line); }
        } catch (IOException e) { System.out.println("Could not find the executable."); }

        return standardOutputStringBuilder.toString();
    }

    /**
     * This method does the following:
     * 1. Uses youtube-dl to convert the given URL from the downloadFile and converts it into a URL that contains only the media file rather than the entire web page.
     * 2. Uses youtube-dl to find the human readable filename that is displayed on the web page then makes sure that it is in the format title-mediaID and restricts filenames to
     * only ASCII characters, and avoid "&" and spaces in filenames.
     * 3. Downloads the file using aria2c into the location specified by the command line argument downloadLocation.
     * @param unconvertedUrl
     */
    private void download(String unconvertedUrl) {
        String convertedUrl = youtubedl(" --get-url", unconvertedUrl);
        String filename = youtubedl(" --get-filename -o '%(title)s.%(ext)s' --restrict-filenames", unconvertedUrl).replace("\'", "");

        System.out.println("Downloading: " + filename + "\n\tFrom Unconverted URL: " + unconvertedUrl + "\n\tWith Converted URL: " + convertedUrl);
        //aria2c(filename, convertedUrl);
        //System.out.println(unconvertedUrl);
        //System.out.println(convertedUrl);
    }

    public static void main(String[] args) {
        Downloader downloader = new Downloader();
        SQLiteDriver sqliteDriver = new SQLiteDriver();

//        args = new String[3];
//        args[0] = System.getProperty("user.dir") + "\\src\\main\\resources\\DLExample.txt";
//        args[1] = System.getProperty("user.dir") + "\\src\\main\\resources\\downloads";
//        args[2] = String.valueOf(2);

        downloader.setup(args);
        downloader.download("https://www.youtube.com/watch?v=dY6jR52fFWo");
    }
}