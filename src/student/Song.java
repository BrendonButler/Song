/**
 * File: Song.java
 * **********************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 * 02/01/2022 - Brendon Butler - implementing Aiden's comparator(s)
 *                              (CmpArtist)
 * 02/01/2022 - Aiden Bradley - creating CmpArtist comparator
 * 01/23/2022 - Brendon Butler - creating fields & building constructor &
 *                              methods
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 */
package student;

import java.util.Comparator;

/**
 * Song class to hold strings for a song's artist, title, and lyrics
 * Do not add any methods for part 1, just implement the ones that are 
 * here.
 * @author boothe
 */
public class Song implements Comparable<Song> {
    // private fields
    private final String ARTIST, TITLE, LYRICS;

    /**
     * Parameterized constructor
     * @param artist the author of the song
     * @param title the title of the song
     * @param lyrics the lyrics as a string with linefeeds embedded
     */
    public Song(String artist, String title, String lyrics) {
        this.ARTIST = artist;
        this.TITLE = title;
        this.LYRICS = lyrics;
    }

    /**
     * Gets the artist name for the song.
     *
     * @return the artist name
     */
    public String getArtist() {
        return ARTIST;
    }

    /**
     * Gets the lyrics for the song.
     *
     * @return the lyrics of the song
     */
    public String getLyrics() {
        return LYRICS;
    }

    /**
     * Gets the title for the song.
     *
     * @return the title of the song
     */
    public String getTitle() {
        return TITLE;
    }

    /**
     * returns name and title ONLY on one line in the form:
     * artist, "title"
     * @return a formatted string in the form ARTIST, "TITLE"
     */
    public String toString() {
        return String.format("%s, \"%s\"", ARTIST, TITLE);
    }

    /**
     * the default comparison of songs
     * primary key: artist, secondary key: title
     * used for sorting and searching the song array
     * if two songs have the same artist and title they are considered the same
     * @param that
     * @return a negative number, positive number or 0 depending on whether 
     *    this song should be  before, after or is the same.  Used for a
     *    "natural" sorting order.  In this case first by author then by 
     *    title so that the all of an artist's songs are together, 
     *    but in alpha order.  Follow the given example.
     */
    @Override
    public int compareTo(Song that) {
        int comparison = 0; // initialize the return value as "the same"

        /* if "that" equals null, it'll go to the end,
         * else if this & that are not equal check artist & title
         */
        if (that == null)
            comparison = -1;
        else if (this != that) {
            // compare artists, if they are different, this is the return value
            comparison = this.ARTIST.compareToIgnoreCase(that.ARTIST);

            // if the artist is the same, compare the song title, this will be the return value
            if (comparison == 0)
                comparison = this.TITLE.compareToIgnoreCase(that.TITLE);
        }

        return comparison;
    }

    /**
     * A subclass that compares by artist
     */
    public static class CmpArtist extends CmpCnt implements Comparator<Song> {
        /**
         * Compares song1 and song2 based on their artist fields
         * @param s1 input song
         * @param s2 compare song
         * @return negative if song1 is less than song2, otherwise positive
         */
        @Override
        public int compare(Song s1, Song s2) {
            cmpCnt++;
            return s1.getArtist().compareToIgnoreCase(s2.getArtist());
        }
    }

    /**
     * testing method to unit test this class
     * @param args
     */
    public static void main(String[] args) {
        Song s1 = new Song("Professor B",
                "Small Steps",
                "Write your programs in small steps\n"
                + "small steps, small steps\n"
                + "Write your programs in small steps\n"
                + "Test and debug every step of the way.\n");

        Song s2 = new Song("Brian Dill",
                "Ode to Bobby B",
                "Professor Bobby B., can't you see,\n"
                + "sometimes your data structures mystify me,\n"
                + "the biggest algorithm pro since Donald Knuth,\n"
                + "here he is, he's Robert Boothe!\n");

        Song s3 = new Song("Professor B",
                "Debugger Love",
                "I didn't used to like her\n"
                + "I stuck with what I knew\n"
                + "She was waiting there to help me,\n"
                + "but I always thought print would do\n\n"
                + "Debugger love .........\n"
                + "Now I'm so in love with you\n");

        System.out.println("testing getArtist: " + s1.getArtist());
        System.out.println("testing getTitle: " + s1.getTitle());
        System.out.println("testing getLyrics:\n" + s1.getLyrics());

        System.out.println("testing toString:\n");
        System.out.println("Song 1: " + s1);
        System.out.println("Song 2: " + s2);
        System.out.println("Song 3: " + s3 + "\n");

        System.out.println("testing compareTo:\n");
        System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
        System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
        System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
        System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
        System.out.println("Song1 vs Song1 = " + s1.compareTo(s1) + "\n");

        CmpArtist ca = new CmpArtist();

        System.out.println("testing compare:\n");
        System.out.println("Song1 vs Song2 = " + ca.compare(s1, s2));
        System.out.println("Song2 vs Song1 = " + ca.compare(s2, s1));
        System.out.println("Song1 vs Song3 = " + ca.compare(s1, s3));
        System.out.println("Song3 vs Song1 = " + ca.compare(s3, s1));
        System.out.println("Song1 vs Song1 = " + ca.compare(s1, s1));
    }
}
