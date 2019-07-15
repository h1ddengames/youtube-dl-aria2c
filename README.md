# youtube-dl-aria2c
A command line wrapper written in both Python and Java that uses youtube-dl and aria2c to download videos when given a file that contains URLs.

In order to use this script:
1. Add Python3, youtube-dl, and aria2c to your PATH.
2. Clone this repository.
3. Open a command prompt in the folder that was cloned.
4. Run the script by using the following command format:
   
    This command will use the txt file located at C:/DL.txt to know which URLs contain the media you wan to download. It will then save the media to C:/Video Downloads and will use 5 threads in order to do so.
    ```
    py downloader.py "C:/DL.txt" "C:/Video Downloads" 5
    ```

    The amount of threads can be as low as 1 and as high as the amount of threads your computer can handle.