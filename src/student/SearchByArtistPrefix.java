/**
 * File: SearchByArtistPrefix.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 02/03/2022 - Brendon Butler - Updating text output from search method
 * 02/02/2022 - Brendon Butler - Implemented, fixed, and optimized search
 *                                  method
 * 02/02/2022 - Aidan Bradley - Created first revision of the search method
 * 8/2015 Anne Applin - Added formatting and JavaDoc 
 * 2015 - Bob Boothe - starting code  
 *****************************************************************************

 */

package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;
/**
 * Search by Artist Prefix searches the artists in the song database 
 * for artists that begin with the input String
 * @author Bob Booth 
 */

public class SearchByArtistPrefix {
    // keep a local direct reference to the song array
    private Song[] songs;  

    /**
     * constructor initializes the property. [Done]
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * converts artistPrefix to lowercase and creates a Song object with 
     * artist prefix as the artist in order to have a Song to compare.
     * walks back to find the first "beginsWith" match, then walks forward
     * adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match 
     *    the prefix
     */
    public Song[] search(String artistPrefix) {
        Song[] result;
        // create a faux song to use as the compare key.
        Song key = new Song(artistPrefix.toLowerCase(), "", "");
        Comparator<Song> comparator = new Song.CmpArtist();
        ((CmpCnt) comparator).resetCmpCnt(); // zero the count.
        int prefixLength = artistPrefix.length(); // get the length of the search key to compare
        // returns the index of where a song that matches the prefix is/should be found (not necessarily the beginning)
        int index = Arrays.binarySearch(songs, key, comparator);

        System.out.printf("Index from binary search: %11d%n", index);
        System.out.printf("Binary search comparisons: %10d%n", ((CmpCnt) comparator).getCmpCnt());

        // invert the index value if negative and subtract 1.
        if (index < 0)
            index = -index - 1;

        System.out.printf("Front found at: %21d%n", index);

        ArrayList<Song> list = new ArrayList<>();

        // find the first value that equals
        while (index > 0 &&
                songs[index-1].getArtist().length() >= prefixLength &&
                songs[index-1].getArtist().substring(0, prefixLength)
                        .compareToIgnoreCase(artistPrefix) == 0) {
            index--;
        }

        // build the list of songs found from the search string
        while (index < songs.length &&
                songs[index].getArtist().length() >= prefixLength &&
                songs[index].getArtist().substring(0, prefixLength)
                        .compareToIgnoreCase(artistPrefix) == 0) {
            list.add(songs[index]);
            index++;
        }

        System.out.printf("Comparisons to build the list: %6d%n", list.size());
        // calculate the complexity based on the O(K+log_2(n)), where K = num of matches & n = quantity of total songs
        int actualComplexity = (int) (list.size() + (Math.log(songs.length) / Math.log(2)));
        System.out.printf("Actual complexity is: %15d%n%n", actualComplexity);

        // move the song list to the Song array
        result = new Song[list.size()];
        result = list.toArray(result);
        return result;
    }

    /**
     * testing method for this unit
     * @param args  command line arguments set in Project Properties - 
     * the first argument is the data file name and the second is the partial 
     * artist name, e.g. be which should return beatles, beach boys, bee gees,
     * etc.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        if (args.length > 1) {
            System.out.printf("searching for: %s%n", args[1]);
            Song[] byArtistResult = sbap.search(args[1]);

            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
        }
    }
}
