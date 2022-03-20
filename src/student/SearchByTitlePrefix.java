/**
 * File: SearchByTitlePrefix.java
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 02/03/2022 - Brendon Butler - Creating Constructor
 *                             - Implementing search function & tests
 *****************************************************************************
 */
package student;

import java.util.Comparator;
import java.util.stream.Stream;

public class SearchByTitlePrefix {
    Comparator<Song> comparator;
    RaggedArrayList<Song> ral;

    /**
     * Constructor for SearchByTitlePrefix
     *
     * @author Brendon Butler
     * @param sc SongCollection instance containing all songs from input file
     */
    public SearchByTitlePrefix(SongCollection sc) {
        comparator = new Song.CmpTitle();
        ral = new RaggedArrayList<>(comparator);

        for (Song song : sc.getAllSongs()) {
            ral.add(song);
        }

        ral.stats();
    }

    /**
     * Search for songs that start with the titlePrefix (inclusive)
     * to the toPrefix (exclusive) ex: between "Angel" and "Angem"
     *
     * @param titlePrefix input prefix to search for
     * @return all songs that start with the prefix
     */
    public Song[] search(String titlePrefix) {
        ((CmpCnt) comparator).resetCmpCnt();

        // toPrefix = titlePrefix minus the last character, increment the last character and add it back
        String toPrefix = titlePrefix.substring(0, titlePrefix.length() - 1)
                            + (char) (titlePrefix.charAt(titlePrefix.length() - 1) + 1);

        // create Song instances for from and to search.
        Song fromSong = new Song("", titlePrefix, "");
        Song toSong = new Song("", toPrefix, "");

        // get the subList from the fromSong and toSong arguments
        RaggedArrayList<Song> resultRAL = ral.subList(fromSong, toSong);
        Song[] result = new Song[resultRAL.size()];

        System.out.println("Comparisons: " + ((CmpCnt) comparator).cmpCnt);
        resultRAL.stats();

        return resultRAL.toArray(result);
    }

    /**
     * testing method for this unit
     * @param args command line arguments set in Project Properties -
     * the first argument is the data file name and the second is the partial
     * title name, e.g. be which should return angel, etc.
     *
     * Adapted from SearchByArtistPrefix
     * Revised by: Brendon Butler
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);

        if (args.length > 1) {
            System.out.printf("searching for: %s%n", args[1]);
            Song[] byTitlePrefix = sbtp.search(args[1]);

            System.out.printf("Total matches: %d%n", byTitlePrefix.length);
            Stream.of(byTitlePrefix).limit(10).forEach(System.out::println);
        }
    }
}
