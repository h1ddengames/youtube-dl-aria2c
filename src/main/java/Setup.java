import java.io.BufferedReader;
import java.io.FileReader;

public class Setup {
    /**
     * Sets up the location of the executables that are needed to download the media.
     * Sets up the arrayList to contain the data stored within the downloadFile.
     */
    public Setup() {
        setupExecutablePath();
        setupUrlArray();
        setupCommands();
    }

    /**
     * Save the location of the executables into the respective variables.
     */
    private void setupExecutablePath() {
        System.out.println("Setting location of executables: setupExecutablePath()...");
        Downloader.setYoutubeDLLocation(System.getProperty("user.dir") + "\\src\\main\\resources\\programs\\Youtubedl\\youtube-dl.exe");
        Downloader.setYoutubeDLLocation(Downloader.getYoutubeDLLocation().replace("\\", "/"));

        Downloader.setPhantomJsLocation(System.getProperty("user.dir") + "\\src\\main\\resources\\programs\\PhantomJS\\bin\\phantomjs.exe");
        Downloader.setPhantomJsLocation(Downloader.getPhantomJsLocation().replace("\\", "/"));

        Downloader.setAria2cLocation(System.getProperty("user.dir") + "\\src\\main\\resources\\programs\\Aria2c\\aria2c.exe");
        Downloader.setAria2cLocation(Downloader.getAria2cLocation().replace("\\", "/"));
        System.out.println("\t- Executable path has been set.");
    }

    /**
     * Read the downloadFile line by line and add it to the arrayList.
     */
    private void setupUrlArray() {
        System.out.println("Reading downloadFile for URLs: setupUrlArray()...");
        try{
            BufferedReader br = new BufferedReader(new FileReader(Downloader.getDownloadFile()));
            String line;
            while ((line = br.readLine()) != null) { Downloader.getUrlsToDownload().add(line); }
        } catch(Exception e) { System.out.println("Unable to open the downloadFile for reading."); }

        System.out.println("\t- URLs have been added to the array.");
        for(String s : Downloader.getUrlsToDownload()) {
            System.out.println("Downloading: " + s);
        }
    }

    private void setupCommands() {
        System.out.println("Setting up commands: setupCommands()...");
        Downloader.setYoutubeDLGetURLCommand(" --get-url");
        Downloader.setYoutubeDLGetFilenameCommand(" --get-filename -o '%(title)s.%(ext)s' --restrict-filenames");
        // "cmd /c " + "\"" + Downloader.getYoutubeDLLocation() + "\"" + " command " + " url"
        Downloader.setYoutubeDLProcess("\"" + Downloader.getYoutubeDLLocation() + "\"" + " command " + " url");
        // "cmd /c " + "\"" + "\"" + Downloader.getAria2cLocation() + "\""
        //                + " -l " + "\"" + Downloader.getDownloadLocation() + "\\" + "log.txt" + "\""
        //                + " --auto-file-renaming=false -c -x5 -j7 -s5 -o " + "\"" + "filename" + "\""
        //                + " -d " + "\"" + Downloader.getDownloadLocation() + "\"" + " " + "\"" + "url" + "\"" + "\""
        Downloader.setAria2cProcess("\"" + "\"" + Downloader.getAria2cLocation() + "\""
                + " -l " + "\"" + Downloader.getDownloadLocation() + "\\" + "log.txt" + "\""
                + " --auto-file-renaming=false -c -x5 -j7 -s5 -o " + "\"" + "filename" + "\""
                + " -d " + "\"" + Downloader.getDownloadLocation() + "\"" + " " + "\"" + "url" + "\"" + "\"");
        System.out.println("\t- Commands have been setup.");
    }
}