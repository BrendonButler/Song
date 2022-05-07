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
        List<RankedSong> rankedSongs = new LinkedList<>();

        System.out.printf("Results for \"%s\"%n", lyricsPhrase);

        for (Song song : sblw.search(lyricsPhrase)) {
            int rank = rankPhrase(song.getLyrics(), lyricsPhrase);
            if (rank >= lyricsPhrase.length())
                rankedSongs.add(new RankedSong(rank, song));
        }

        rankedSongs.sort(RankedSong::compareTo);
        Song[] results = new Song[rankedSongs.size()];
        int i = 0;

        for (RankedSong song : rankedSongs) {
            results[i] = song.getSong();
            i++;
        }

        return results;
    }
}