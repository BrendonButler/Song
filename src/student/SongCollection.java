/**
 * File: SongCollection.java
 ************************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 * 01/23/2022 - Brendon Butler - Implementing constructor
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 */

package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * SongCollection.java 
 * Reads the specified data file and build an array of songs.
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore 
     * not portable. Java runs on a virtual machine that IS portable. So this 
     * is permissible because we are programming in Java and Java runs on a
     * virtual machine not directly on the hardware.
     *
     * @param fileName The path and fileName to the datafile that we are using
     * must be set in the Project Properties as an argument.
     */
    public SongCollection(String fileName) {
        String currLine, artist, title;
        StringBuilder lyrics = new StringBuilder();
        ArrayList<Song> songList = new ArrayList<>();
        // use a try catch block
        // read in the song file and build the songs array
        // there are several ways to read in the lyrics correctly.
        // the line feeds between lines and the blank lines between verses
        // must be retained.
        try {
            Scanner scanner = new Scanner(new File(fileName));

            while (scanner.hasNextLine()) {
                currLine = scanner.nextLine();
                artist = currLine.substring(8, currLine.length() - 1);

                currLine = scanner.nextLine();
                title = currLine.substring(7, currLine.length() - 1);

                currLine = scanner.nextLine();
                lyrics.append(currLine.substring(8));

                while (!(currLine = scanner.nextLine()).startsWith("\""))
                    lyrics.append("\n").append(currLine);

                songList.add(new Song(artist, title, lyrics.toString()));
                lyrics.setLength(0);
            }

            scanner.close();
        } catch (FileNotFoundException exception) {
            System.out.printf("File not found: %s", fileName);
        }

        if (!songList.isEmpty())
            songs = songList.toArray(new Song[songList.size() - 1]);
        // sort the songs array using Arrays.sort (see the Java API)
        // this will use the compareTo() in Song to do the job.
        Arrays.sort(songs);
    }
 
    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }
 
    /**
     * unit testing method
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        // show song count
        Stream.of(sc.songs).limit(10).forEach(System.out::println);
    }
}
