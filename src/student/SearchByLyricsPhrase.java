/**
 * File: SearchByLyricsPhrase.java
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 05/07/2022 - Brendon Butler - completed search function & testing
 * 04/19/2022 - Brendon Butler - implemented search function
 *****************************************************************************
 */
package student;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static student.PhraseRanking.RankedSong;
import static student.PhraseRanking.rankPhrase;

public class SearchByLyricsPhrase {

    private SongCollection sc;
    private SearchByLyricsWords sblw;

    /**
     * Constructor for SearchByLyricsPhrase
     *
     * @author Brendon Butler
     * @param sc SongCollection instance containing all songs from input file
     */
    public SearchByLyricsPhrase(SongCollection sc) {
        this.sc = sc;
        sblw = new SearchByLyricsWords(sc);
    }

    /**
     * Search method to find songs in which their lyrics match the input search phrase string
     *
     * @author Brendon Butler
     * @param lyricsPhrase input search string
     * @return an array of songs that have lyrics matching the input search phrase string
     */
    public Song[] search(String lyricsPhrase) {
        List<RankedSong> rankedSongs = new ArrayList<>();

        // add songs to the rankedSong list
        for (Song song : sblw.search(lyricsPhrase)) {
            int rank = rankPhrase(song.getLyrics(), lyricsPhrase);
            if (rank >= 0)
                rankedSongs.add(new RankedSong(rank, song));
        }

        // sort the rankedSongs by ranking (low to high)
        Collections.sort(rankedSongs);

        // add all the results to an array and return those results
        Song[] results = new Song[rankedSongs.size()];

        for (int i = 0; i < rankedSongs.size(); i++) {
            results[i] = rankedSongs.get(i).getSong();
        }

        return results;
    }

    /**
     * testing method for this unit
     * @param args command line arguments set in Project Properties -
     * the first argument is the data file name, the second being a
     * phrase to search for.
     *
     * Adapted from SearchByArtistPrefix
     * Revised by: Brendon Butler
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsPhrase sblp = new SearchByLyricsPhrase(sc);

        List<RankedSong> rankedSongs = new LinkedList<>();

        Instant startTime = Instant.now();
        for (Song song : sblp.search(args[1])) {
            int rank = rankPhrase(song.getLyrics(), args[1]);
            if (rank >= 0)
                rankedSongs.add(new RankedSong(rank, song));
        }
        Instant endTime = Instant.now();
        System.out.println("ELAPSED TIME: " + Duration.between(startTime, endTime).toMillis() + "ms");

        //rankedSongs.sort(RankedSong::compareTo);
        rankedSongs.stream().limit(10).forEach(System.out::println);
        System.out.printf("...total of %d songs%n", rankedSongs.size());
    }
}