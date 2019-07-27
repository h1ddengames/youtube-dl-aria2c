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
    private static String scriptVersion = "1.0a";
    // The .txt file containing a single URL on each line.
    // Look at DLExample.txt in the /resources folder for an example.
    private static String downloadFile;
    // The location where the media files should be downloaded.
    private static String downloadLocation;
    // How many threads should be used to speed up the download of all the media in the downloadFile.
    private static int numberOfThreads = 1;

    private static String youtubeDLProcess = "";
    private static String aria2cProcess = "";
    private static String youtubeDLGetURLCommand = "";
    private static String youtubeDLGetFilenameCommand = "";

    // The location of the executables required to download the media files.
    private static String YOUTUBE_DL_LOCATION;
    private static String PHANTOM_JS_LOCATION;
    private static String ARIA_2C_LOCATION;

    // Store the URLs from the downloadFile into an ArrayList in memory.
    private static List<String> urlsToDownload = new ArrayList<String>();

    private CheckArguments checkArguments;
    private Setup setup;

    public static String getScriptVersion() { return scriptVersion; }
    public static String getDownloadFile() { return downloadFile; }
    public static void setDownloadFile(String downloadFile) { Downloader.downloadFile = downloadFile; }
    public static String getDownloadLocation() { return downloadLocation; }
    public static void setDownloadLocation(String downloadLocation) { Downloader.downloadLocation = downloadLocation; }
    public static int getNumberOfThreads() { return numberOfThreads; }
    public static void setNumberOfThreads(int numberOfThreads) { Downloader.numberOfThreads = numberOfThreads; }
    public static String getYoutubeDLLocation() { return YOUTUBE_DL_LOCATION; }
    public static void setYoutubeDLLocation(String youtubeDlLocation) { Downloader.YOUTUBE_DL_LOCATION = youtubeDlLocation; }
    public static String getPhantomJsLocation() { return PHANTOM_JS_LOCATION; }
    public static void setPhantomJsLocation(String phantomJsLocation) { Downloader.PHANTOM_JS_LOCATION = phantomJsLocation; }
    public static String getAria2cLocation() { return ARIA_2C_LOCATION; }
    public static void setAria2cLocation(String aria2cLocation) { Downloader.ARIA_2C_LOCATION = aria2cLocation; }
    public static List<String> getUrlsToDownload() { return urlsToDownload; }
    public void setCheckArguments(CheckArguments checkArguments) { this.checkArguments = checkArguments; }
    public void setSetup(Setup setup) { this.setup = setup; }
    public static String getYoutubeDLProcess() { return youtubeDLProcess; }
    public static void setYoutubeDLProcess(String youtubeDLProcess) { Downloader.youtubeDLProcess = youtubeDLProcess; }
    public static String getAria2cProcess() { return aria2cProcess; }
    public static void setAria2cProcess(String aria2cProcess) { Downloader.aria2cProcess = aria2cProcess; }
    public static String getYoutubeDLGetURLCommand() { return youtubeDLGetURLCommand; }
    public static void setYoutubeDLGetURLCommand(String youtubeDLGetURLCommand) { Downloader.youtubeDLGetURLCommand = youtubeDLGetURLCommand; }
    public static String getYoutubeDLGetFilenameCommand() { return youtubeDLGetFilenameCommand; }
    public static void setYoutubeDLGetFilenameCommand(String youtubeDLGetFilenameCommand) { Downloader.youtubeDLGetFilenameCommand = youtubeDLGetFilenameCommand; }

    public Downloader(String[] args) {
        setCheckArguments(new CheckArguments(args));
        setSetup(new Setup());
    }

//    /**
//     * Runs the aria2c executable in order download the media contained within the given url.
//     * @param filename The filename obtained from running youtube-dl.
//     * @param url The url obtained from running youtube-dl.
//     * @return The output of running the aria2c executable.
//     */
//    public String aria2c(String filename, String url) {
//        StringBuilder standardOutputStringBuilder = new StringBuilder();
//        StringBuilder errorStringBuilder = new StringBuilder();
//
//        try {
//            Runtime rt = Runtime.getRuntime();
//            // Have to wrap the whole cmd command with double quotes in order to be able to use a file path that contains spaces between words and to use arguments.
//            // aria2c -l "downloadLocation" --auto-file-renaming=false -c -x5 -j7 -s5 -o "filename" -d "downloadLocation" "url"
//            // cmd /c ""ARIA_2C_LOCATION" -l "youtube-dl-aria2c\src\main\resources\downloads\log.txt" --auto-file-renaming=false -c -x5 -j7 -s5 -o "Google Homepage.html" -d "youtube-dl-aria2c\src\main\resources\downloads" "https://google.com""
//            String command = "cmd /c " + "\"" + "\"" + Downloader.getAria2cLocation() + "\""
//                    + " -l " + "\"" + getDownloadLocation() + "\\" + "log.txt" + "\""
//                    + " --auto-file-renaming=false -c -x5 -j7 -s5 -o " + "\"" + filename + "\""
//                    + " -d " + "\"" + getDownloadLocation() + "\"" + " " + "\"" + url + "\"" + "\"";
//            System.out.println(command);
//            Process process = rt.exec(command);
//            InputStream stdin = process.getInputStream();
//            InputStreamReader isr = new InputStreamReader(stdin);
//            BufferedReader br = new BufferedReader(isr);
//
//            InputStream stderr = process.getErrorStream();
//            InputStreamReader esr = new InputStreamReader(stderr);
//            BufferedReader errorReader = new BufferedReader(esr);
//
//            String line = null;
//            while ( (line = br.readLine()) != null) {
//                if(line.contains("error")) { return "Failed"; }
//                System.out.println(line);
//                standardOutputStringBuilder.append(line);
//            }
//            while ( (line = errorReader.readLine()) != null) {
//                if(line.contains("error")) { return "Failed"; }
//                System.out.println(line);
//                errorStringBuilder.append(line);
//            }
//        } catch (IOException e) { System.out.println("Could not find the executable."); return "Failed"; }
//        return "Succeeded";
//    }
//
//    /**
//     * Runs the youtube-dl executable in order to find the media url and the filename.
//     * @param command The command to run (either --get-url or --get-filename)
//     * @param url The url that contains the media that needs to be downloaded.
//     * @return A string that contains either the media url or the filename based on which command was run.
//     */
//    public String youtubedl(String command, String url) {
//        System.out.println("Running the command: youtube-dl" + command + " for url: " + url);
//        StringBuilder standardOutputStringBuilder = new StringBuilder();
//        StringBuilder errorStringBuilder = new StringBuilder();
//
//        try {
//            Runtime rt = Runtime.getRuntime();
//            Process process = rt.exec("cmd /c " + "\"" + Downloader.getYoutubeDLLocation() + "\"" + command + " " + url);
//            InputStream stdin = process.getInputStream();
//            InputStreamReader isr = new InputStreamReader(stdin);
//            BufferedReader br = new BufferedReader(isr);
//
//            InputStream stderr = process.getErrorStream();
//            InputStreamReader esr = new InputStreamReader(stderr);
//            BufferedReader errorReader = new BufferedReader(esr);
//
//            String line = null;
//            while ( (line = br.readLine()) != null) { standardOutputStringBuilder.append(line); }
//            while ( (line = errorReader.readLine()) != null) { errorStringBuilder.append(line); }
//        } catch (IOException e) { System.out.println("Could not find the executable."); }
//
//        return standardOutputStringBuilder.toString();
//    }
//
//    /**
//     * This method does the following:
//     * 1. Uses youtube-dl to convert the given URL from the downloadFile and converts it into a URL that contains only the media file rather than the entire web page.
//     * 2. Uses youtube-dl to find the human readable filename that is displayed on the web page then makes sure that it is in the format title-mediaID and restricts filenames to
//     * only ASCII characters, and avoid "&" and spaces in filenames.
//     * 3. Downloads the file using aria2c into the location specified by the command line argument downloadLocation.
//     * @param unconvertedUrl This is the URL that the user copied into the downloadFile.
//     */
//    private void download(String unconvertedUrl) {
////        if(video exists in database) {
////            // Skip downloading.
////        }
//
//        String convertedUrl = youtubedl(" --get-url", unconvertedUrl);
//        String filename = youtubedl(" --get-filename -o '%(title)s.%(ext)s' --restrict-filenames", unconvertedUrl).replace("\'", "");
//        String success = aria2c(filename, convertedUrl);
//
//        System.out.println("Downloaded: " + filename + "\n\t- From Unconverted URL: " + unconvertedUrl + "\n\t- With Converted URL: " + convertedUrl + "\n\t- " + success);
//
////        if(success.contentEquals("Succeeded")) {
////            // Update the database with the unconvertedUrl, convertedUrl, filename, and success status
////        }
//    }

    public void download(String unconvertedURL) {
        System.out.println("Unconverted URL: " + unconvertedURL);
        String filename;
        String convertedURL;
        String success = "";

        try {
            // Get converted URL: youtube-dl --get-url url
            System.out.println("Running youtube-dl --get-url " + unconvertedURL);
            ProcessBuilder builder;
            if(setup.isRunningAsJar()) {
                builder = new ProcessBuilder(
                        "cmd.exe", "/c",
                        getYoutubeDLProcess()
                                .replace(" command", getYoutubeDLGetURLCommand())
                                .replace(" url", unconvertedURL)
                );
            } else {
                builder = new ProcessBuilder(
                        "cmd.exe", "/c",
                        getYoutubeDLProcess()
                                .replace(" command", getYoutubeDLGetURLCommand())
                                .replace(" url", unconvertedURL)
                                .replaceFirst("/", "")
                );
            }

            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (true) {
                line = reader.readLine();
                if(line != null && line.contains("http")) {
                    convertedURL = line;
                    System.out.println("Converted URL: " + convertedURL);
                    break;
                }
            }

            // Get filename: youtube-dl --get-filename -o '%(title)s.%(ext)s' --restrict-filenames url
            System.out.println("youtube-dl --get-filename -o '%(title)s.%(ext)s' --restrict-filenames " + unconvertedURL);
            if(setup.isRunningAsJar()) {
                builder = new ProcessBuilder(
                        "cmd.exe", "/c",
                        getYoutubeDLProcess()
                                .replace(" command", getYoutubeDLGetFilenameCommand())
                                .replace(" url", unconvertedURL)
                );
            } else {
                builder = new ProcessBuilder(
                        "cmd.exe", "/c",
                        getYoutubeDLProcess()
                                .replace(" command", getYoutubeDLGetFilenameCommand())
                                .replace(" url", unconvertedURL)
                                .replaceFirst("/", "")
                );
            }

            process = builder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                line = reader.readLine();
                if(line != null && (line.contains("mp3") || line.contains("mp4") || line.contains("3gp") ||
                        line.contains("ogg") || line.contains("wmv") || line.contains("webm") ||
                        line.contains("flv") || line.contains("avi") || line.contains("wav"))) {

                    filename = line.replace("\'", "");
                    System.out.println("Filename: " + filename);
                    break;
                }
            }

            // aria2c -l "downloadLocation" --auto-file-renaming=false -c -x5 -j7 -s5 -o "filename" -d "downloadLocation" "url"
            System.out.println("aria2c -l " + downloadLocation
                    + " --auto-file-renaming=false -c -x5 -j7 -s5 -o "
                    + filename + " -d " + downloadLocation +  convertedURL);
            if(setup.isRunningAsJar()) {
                builder = new ProcessBuilder(
                        "cmd.exe", "/c",
                        getAria2cProcess()
                                .replace("filename", filename)
                                .replace("url", convertedURL)
                );
            } else {
                builder = new ProcessBuilder(
                        "cmd.exe", "/c",
                        getAria2cProcess()
                                .replace("filename", filename)
                                .replace("url", convertedURL)
                                .replaceFirst("/", "")
                );
            }

            process = builder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ( (line = reader.readLine()) != null) {
                if(line.contains("error") || line.contains("already completed")) { success = "Failed"; break; }
            }

            if(!success.contentEquals("Failed")) {
                success = "Succeeded";
                // Update the database with the unconvertedUrl, convertedUrl, filename, and success status
            }

            System.out.println("Success Status: " + success);
            process.destroy();
        } catch (IOException e) {
            System.out.println("ERROR WITH DOWNLOAD");
            e.printStackTrace();
        }
    }

    public Downloader() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file;

        try {
            String url = classLoader.getResource("programs/Aria2c/aria2c.exe").getFile();
            url = url.replace("%20", " ");
            System.out.println(url);

            file = new File(url);

//            FileReader reader = new FileReader(file);
//            BufferedReader br = new BufferedReader(reader);
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir"));
//        Downloader downloader = new Downloader();
        if(args.length == 0) {
            args = new String[3];
            args[0] = System.getProperty("user.dir") + "\\src\\main\\resources\\DLExample.txt";
            args[1] = System.getProperty("user.dir") + "\\src\\main\\resources\\downloads";
            args[2] = String.valueOf(2);
        }

        Downloader downloader = new Downloader(args);
        SQLiteDriver sqliteDriver = new SQLiteDriver();

        // downloader.download("https://vimeo.com/70774457");

        System.out.println("----------------------------------------------------------------------");

        for(String url : getUrlsToDownload()) {
            new Thread(() -> {
                downloader.download(url);
            }).start();
            System.out.println("----------------------------------------------------------------------");
        }

//        try {
//            Setup setup = new Setup();
//            System.out.println("------------------------------------------------------");
//            System.out.println(getYoutubeDLProcess()
//                    .replace(" command", getYoutubeDLGetURLCommand())
//                    .replace(" url", "https://vimeo.com/70774457")
//                    .replaceFirst("/", ""));
//            ProcessBuilder builder = new ProcessBuilder(
//                    "cmd.exe", "/c",
//                    getYoutubeDLProcess()
//                            .replace(" command", getYoutubeDLGetURLCommand())
//                            .replace(" url", "https://vimeo.com/70774457")
//                            .replaceFirst("/", "")
//            );
//            builder.redirectErrorStream(true);
//            Process process = builder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while (true) {
//                line = reader.readLine();
//                if (line == null) { break; }
//                System.out.println(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}