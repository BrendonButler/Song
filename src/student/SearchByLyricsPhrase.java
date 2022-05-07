/**
 * File: SearchByLyricsPhrase.java
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 04/19/2022 - Brendon Butler -
 *****************************************************************************
 */
package student;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;

public class SearchByLyricsPhrase {

    private SongCollection sc;

    /**
     * Constructor for SearchByLyricsPhrase
     *
     * @author Brendon Butler
     * @param sc SongCollection instance containing all songs from input file
     */
    public SearchByLyricsPhrase(SongCollection sc) {
        this.sc = sc;
    }

    /**
     * Search method to find songs in which their lyrics match the input search phrase string
     *
     * @author Brendon Butler
     * @param lyricsPhrase input search string
     * @return an array of songs that have lyrics matching the input search phrase string
     */
    public Song[] search(String lyricsPhrase) {
        String[] phraseWords = lyricsPhrase.toLowerCase().split(" ");
        HashSet<PhraseRanking.RankedSong> results = new HashSet<>();

        for (Song song : sc.getAllSongs()) {
            int rank = -1;

            if (song.getLyrics().contains(phraseWords[0])
                    && song.getLyrics().contains(phraseWords[phraseWords.length - 1])) {
                rank = rankSong(song.getLyrics(), lyricsPhrase);
            }

            if (rank >= lyricsPhrase.length()) {
                results.add(new PhraseRanking.RankedSong(rank, song));
            }
        }

        return (Song[]) results.toArray();
    }

    private int rankSong(String lyrics, String lyricsPhrase) {
        String[] phraseWords = lyricsPhrase.toLowerCase().split(" ");
        lyrics = lyrics.toLowerCase().replaceAll("\\W+", " ");

        int foundIndex = lyrics.indexOf(phraseWords[0]);
        int bestRank = -1;

        // find all the starting points by the first search word
        while (foundIndex > -1) {
            Queue<String> words = new ArrayDeque<>(Arrays.asList(phraseWords));
            int rank = 0;
            int curIndex = 0;

            while (!words.isEmpty() && curIndex >= 0) {
                curIndex = getExactWordIndex(lyrics, words.peek(), curIndex);

                if (curIndex != -1) {
                    words.poll();
                    rank += curIndex;
                }
            }

            if ((rank < bestRank || bestRank < 0) && rank >= lyricsPhrase.length()) {
                bestRank = rank;
            }

            foundIndex = getExactWordIndex(lyrics, phraseWords[0], ++foundIndex);
        }

        return bestRank;
    }

    private int getExactWordIndex(String lyrics, String word, int adjust) {
        int curIndex = lyrics.indexOf(word, adjust);
        boolean foreChar = true, aftChar = true;

        while (curIndex >= 0) {
            foreChar = (curIndex != 0 && Character.isLetter(lyrics.charAt(curIndex - 1)));
            aftChar = (curIndex + word.length() != lyrics.length() && Character.isLetter(lyrics.charAt(curIndex + word.length())));

            if (foreChar && aftChar)
                curIndex = lyrics.indexOf(word, curIndex + 1);
            else curIndex *= -1;
        }

        return (!foreChar && !aftChar) ? curIndex : -1;
    }
}