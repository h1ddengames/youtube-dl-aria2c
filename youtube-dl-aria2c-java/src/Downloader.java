/**
 * This script was created by Shahid Karim on 7/15/19.
 */
public class Downloader {
    private static String downloadFile = "";
    private static String downloadLocation = "";
    private static int numberOfThreads = 1;

    private void download() {

    }

    private static void printRequiredArguments() {
        System.out.println("downloadFile - The location of the file that contains URLS.");
        System.out.println("downloadLocation - The location where the files should be downloaded.");
        System.out.println("numberOfThreads - The number of threads to use in order to speed up the download process.");
        System.out.println("\nExample usage: java Downloader.java C:/DL.txt" + " " + "C:/Video Downloads" + " 10");
    }

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Not enough arguments. This script requires the following: \n");
            printRequiredArguments();
        } else if (args.length > 3) {
            System.out.println("Too many arguments.\n");
            printRequiredArguments();
        } else if (args[0].contentEquals("-h")) {
            System.out.println("\nThis script is a command line wrapper written in Java that uses youtube-dl and aria2c " +
                    "to download videos when given a file that contains URLs. The required command line arguments are as follows: \n");
            printRequiredArguments();
        }
    }
}