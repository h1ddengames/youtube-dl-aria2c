import java.io.File;

public class CheckArguments {

    public CheckArguments(String[] args) { checkArguments(args); }

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
        System.out.println("Checking the number of arguments: checkArguments()...");
        if(args.length > 0) {
            // Options - The usual command line options such as -h, --help for help and -v, --version for version.
            if (args[0].contentEquals("-h") || args[0].contentEquals("--help")) {
                System.out.println("\nThis script is a command line wrapper written in Java that uses youtube-dl and aria2c " +
                        "to download videos when given a file that contains URLs. The required command line arguments are as follows: \n");
                printRequiredArguments();
            } else if (args[0].contentEquals("-v") || args[0].contentEquals("--version")) {
                System.out.println("Version " + Downloader.getScriptVersion());
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
        if (args.length == 3) { System.out.println("\t- Given 3 arguments. Continuing execution."); checkForValidInputs(args); }
    }

    /**
     * Checks that the arguments themselves are correct for what they need to do.
     * ie. downloadFile should exist AND point to a file NOT a directory.
     * ie. downloadLocation should exist AND point to a directory NOT a file. (Note: this is the opposite of downloadFile)
     * ie. numberOfThreads is a number above 0 but less than or equal to the maximum number of threads your cpu supports.
     * @param args The command line arguments: downloadFile downloadLocation numberOfThreads
     */
    private void checkForValidInputs(String[] args) {
        System.out.println("Checking that the arguments given are valid inputs: checkForValidInputs()...");
        // Set the variables to the given arguments.
        Downloader.setDownloadFile(args[0]);
        Downloader.setDownloadLocation(args[1]);

        // If the final argument is not an integer, default to 1 thread.
        try {
            Downloader.setNumberOfThreads(Integer.parseInt(args[2]));
        } catch (NumberFormatException e) {
            System.out.println("An integer is required as the third argument: numberOfThreads." + " Defaulting to 1 thread.");
            Downloader.setNumberOfThreads(1);
        }

        // Do not allow the user to set numberOfThreads greater than the threads available on their CPU.
        // Dow not allow the user to set numberOfThreads less than or equal to 0.
        if(Downloader.getNumberOfThreads() > Runtime.getRuntime().availableProcessors()) {
            Downloader.setNumberOfThreads(Runtime.getRuntime().availableProcessors());
            System.out.println("The numberOfThreads specified was above the number of threads your CPU supports. " +
                    "Defaulting to the max amount possible on your CPU: " + Downloader.getNumberOfThreads());
        } else if(Downloader.getNumberOfThreads() <= 0) {
            Downloader.setNumberOfThreads(1);
        }

        System.out.println("\t- Number of threads: has been validated.");

        // Checks that the argument corresponding to the downloadFile variable is a file and it exists.
        if(new File(Downloader.getDownloadFile()).exists()) {
            System.out.println("\t- Download file: file exists.");
        } else { System.err.println("Error! Download file: " + Downloader.getDownloadFile() + " does not exist. Exiting..."); System.exit(-1); }

        // Checks that the argument corresponding to the downloadLocation variable is a directory and it exists.
        if(new File(Downloader.getDownloadLocation()).isDirectory()) {
            System.out.println("\t- Download location: directory exists.");
        } else {
            System.err.println("Error! Download location: " + Downloader.getDownloadLocation() + " does not exist or is not a directory. Exiting...");
            System.exit(-1);
        }

        System.out.println("\t- Inputs have been verified.");
        System.out.println("----------------------------------------------------------------------");

        System.out.println("Starting the download script with the following inputs: ");
        System.out.println("\t- File location: " + Downloader.getDownloadFile());
        System.out.println("\t- Download location: " + Downloader.getDownloadLocation());
        System.out.println("\t- Number of threads: " + Downloader.getNumberOfThreads());
        System.out.println("----------------------------------------------------------------------");
    }
}