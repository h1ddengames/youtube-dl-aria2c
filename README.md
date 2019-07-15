# youtube-dl-aria2c
A command line wrapper written in both Python and Java that uses Youtube-dl and Aria2c to download videos when given a file that contains URLs.

The script will download the best version available based on what Youtube-dl can find. The filename will be restricted to ASCII only and be below 260 characters in length. The filename will also be the title of the video + the number identifier given by the website. The options used for Aria2c are listed below:

    # Start downloading the media file using aria2c
    # -x The maximum number of connections to one server for each download
    # -j Set the maximum number of parallel downloads for every queue item.
    # -s Download a file using the given number of connections
    # -o The file name of the downloaded file.
    # -c Will continue a download if it was stopped for any reason and will not redownload any files if it already exists.
    # --auto-file-renaming If a control file (.aria2) exists then the download will resume, otherwise if no control file exists then since --allow-overwrite is false by default the file will not be re-downloaded.

<b> Please note: The Python3 version has NOT been thoroughly tested and does NOT properly handle unusual inputs. I am new to Python so it doesn't have the same level of polish that my other repositories have. If you want a properly tested version please wait for the Java version to be finished. </b>

In order to use the Python script:
1. Add Python3, Youtube-dl, and Aria2c to your PATH.
2. Clone this repository.
3. Open a command prompt in the folder that was cloned.
4. Run the script by using the following command format:
   
    This command will use the txt file located at C:/DL.txt to know which URLs contain the media you wan to download. It will then save the media to C:/Video Downloads and will use 5 threads in order to do so.
    ```
    py downloader.py "C:/DL.txt" "C:/Video Downloads" 5
    ```

    The amount of threads can be as low as 1 and as high as the amount of threads your computer can handle.

In order to use the Java script:
1. Add Java 8 or Java 11 to your PATH. Youtube-dl and Aria2c have been added to the repository so you will not need to have it in your PATH.
2. Clone this repository.
3. Open a command prompt in the youtube-dl-aria2c-java/src folder.
4. Run the script using the following command format:

    This command will use the txt file located at C:/DL.txt to know which URLs contain the media you wan to download. It will then save the media to C:/Video Downloads and will use 5 threads in order to do so.
    ```
    py downloader.py "C:/DL.txt" "C:/Video Downloads" 5
    ```

    The amount of threads can be as low as 1 and as high as the amount of threads your computer can handle.