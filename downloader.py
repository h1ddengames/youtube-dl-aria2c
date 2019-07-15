# This script was created by Shahid Karim on 7/14/19
# This script requires that python3, youtube-dl, and aria2c have been added to your PATH.
# This script has only been tested on Windows.
# Run this script through the command line: 
# py downloader.py downloadFile downloadLocation numberOfThreads
# py downloader.py "DL.txt" "C:/Transfer Files/Videos" 7

import subprocess
import argparse
from multiprocessing import Pool

downloadFile = ""
downloadLocation = ""
numberOfThreads = 5   
text = 'This script will use youtube-dl and aria2c to download any media when given a file with the URL(s) to the media you want to download. Example usage: py downloader.py "DL.txt" "C:/Transfer Files/Videos" 7'

# Stores the URLS from a given file into an array.
def load_urls(fileName):
    print("Loading URLS into an array.")
    fileData = []
    with open(fileName, 'r') as downloadFile:
        for line in downloadFile:
            fileData.append(line.rstrip())
    return fileData

# Uses youtube-dl to find the URL where the media is stored and gets the media's human-readable filename. Then it uses aria2c in order to download to the directory that was specified through the command line.
def download(unconvertedUrl):
    global downloadFile
    global downloadLocation
    global numberOfThreads

    # Get the URL of the media itself (mp4, jpg, png, gif, etc) rather than just the html page (index.html). 
    convertedUrl = subprocess.run(["youtube-dl", "--get-url", unconvertedUrl], stdout=subprocess.PIPE).stdout.decode('utf-8')

    # Get the actual name of the media file (butterfly.png) rather than the server's identifier name (some random string 2350wdf320423.png)
    filename = subprocess.run(["youtube-dl", "--get-filename", "-o '%(title)s.%(ext)s'", "--restrict-filenames", unconvertedUrl], stdout=subprocess.PIPE).stdout.decode('utf-8')
    filename = filename.replace("'", "")

    #print(downloadLocation + "/" + filename.strip())
    #location = downloadLocation + "/" + filename.strip()
    
    print("Starting download of " + filename.strip())
    # Make sure that the media url isn't equal to an empty string.
    if convertedUrl:
        # Make sure that the filename isn't equal to an empty string.
        if filename:
            # Start downloading the media file using aria2c
            # -x The maximum number of connections to one server for each download
            # -j Set the maximum number of parallel downloads for every queue item.
            # -s Download a file using the given number of connections
            # -o The file name of the downloaded file.
            # -c Will continue a download if it was stopped for any reason and will not redownload any files if it already exists.
            # --auto-file-renaming If a control file (.aria2) exists then the download will resume, otherwise if no control file exists then since --allow-overwrite is false by default the file will not be re-downloaded.
            subprocess.run(["aria2c", '--auto-file-renaming=false', '-c', '-x5', '-j7', '-s5', '-o', filename, '-d', downloadLocation, convertedUrl.strip()], stdout=subprocess.PIPE).stdout.decode('utf-8')
    
    print("Download of " + filename.strip() + " has finished.")
    
# Entry point for the program.    
def main(args):
    update_vars(args)
    
    # Store the URLs contained within the file given in the command line arguments into a local array variable.
    data = load_urls(args.downloadFile)
    
    # If this file is imported as a module then none of this will run.
    # But since this file will be run directly then the following lines will always run.
    # This line is required when doing anything with multiprocessing in order to avoid infinite recursive calls.
    if __name__ == "__main__":
        # Using multiprocessing, download all the media contained in the data array URLs. 
        with Pool(numberOfThreads) as p:
            p.map(download, data)

# Updates global variables based on command line arguments. 
def update_vars(args):
    global downloadFile
    global downloadLocation
    global numberOfThreads
    
    downloadFile = args.downloadFile
    downloadLocation = args.downloadLocation
    if(args.numberOfThreads <= 0):
        numberOfThreads = 1
    else:
        numberOfThreads = args.numberOfThreads
   
# The command line contains 4 arguments:
# 0. This script's name: downloader.py
# 1. File containing all the URLs to download: DL.txt
# 2. Download location: C:\Users
# 3. Number of threads to use for downloading: 10

parser = argparse.ArgumentParser(description = text)
parser.add_argument("downloadFile", help="The location of the file that contains links to your media downloads.")
parser.add_argument("downloadLocation", help="The location you want to store the downloaded media.")
parser.add_argument("numberOfThreads", help="The number threads do you want to use to speed up the download of your media.", type=int)

args = parser.parse_args()

main(args)