/**
 * File: PhraseRanking.java
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 05/07/2022 - Brendon Butler - Fixed rankPhrase method to get expected results
 *                               and optimized implementation
 * 04/18/2022 - Adarsha Dangi & Brendon Butler - cleanup & testing of rankPhrase
 * 04/17/2022 - Brendon Butler - Created PhraseRanking class & RankedPhrase
                                 subclass
 *****************************************************************************
 */
package student;

import java.util.*;

public class PhraseRanking {

    /**
     * Phrase ranker for songs that contain all words in a phrase in order
     *
     * @author Brendon Butler
     * @param lyrics       song lyrics to be parsed
     * @param lyricsPhrase phrase used for ranking lyrics
     * @return rank value determined by ranking algorithm
     */
    public static int rankPhrase(String lyrics, String lyricsPhrase) {
        String[] phraseWords = lyricsPhrase.toLowerCase().split("[^a-zA-Z]+");
        lyrics = lyrics.toLowerCase().replaceAll("(\\n|\\r)", "  ");

        int foundIndex = getExactWordIndex(lyrics, phraseWords[0], 0);
        int bestRank = -1;

        int fullMatchIndex = lyrics.indexOf(lyricsPhrase.toLowerCase());
        int exactMatchIndex = getExactWordIndex(lyrics, lyricsPhrase.toLowerCase(), 0);

        // check for exact match
        if (exactMatchIndex >= 0 && fullMatchIndex == exactMatchIndex) {
            foundIndex = -1;
            bestRank = lyricsPhrase.length();
        }

        // while the foundIndex is not negative and the absolute best rank has not been achieved, loop
        while (foundIndex >= 0 && bestRank != lyricsPhrase.length()) {
            Queue<String> words = new ArrayDeque<>(Arrays.asList(phraseWords));
            int curIndex = foundIndex;
            int lastIndex = -1;
            words.poll(); // remove first word as it's index has already been identified

            // while the words queue is not empty and the current index is greater than the previous index, loop
            while (!words.isEmpty() && curIndex >= lastIndex) {
                lastIndex = curIndex;
                curIndex = getExactWordIndex(lyrics, words.peek(), lastIndex + 1);

                // if the current index is greater than the last index, poll the next value from the queue
                if (curIndex > lastIndex) {
                    words.poll();
                }
            }

            // if the words queue is empty, all the words have been found, calculate the best rank.
            if (words.isEmpty()) {
                int rank = curIndex - foundIndex + phraseWords[phraseWords.length - 1].length();

                if (bestRank > rank || (bestRank == -1 && rank > 0)) {
                    bestRank = rank;
                }
            }

            // find the next starting index for the initial search term
            foundIndex = getExactWordIndex(lyrics, phraseWords[0], foundIndex + 1);
        }

        return bestRank;
    }

    /**
     * Helper class to identify if a word is found with no other letters surrounding it, if not
     * return the next index of that word without surrounding characters or a negative for no match.
     *
     * @author Brendon Butler
     * @param lyrics    lyrics to be searched
     * @param word      word to search for
     * @param adjust    index adjustment for forward searching
     * @return either the identified exact match index or a negative for no match
     */
    private static int getExactWordIndex(String lyrics, String word, int adjust) {
        int curIndex = lyrics.indexOf(word, adjust);
        boolean foreChar = true, aftChar = true;

        while (curIndex >= 0 && !(!foreChar && !aftChar)) {
            foreChar = (curIndex != 0 && Character.isLetter(lyrics.charAt(curIndex - 1)));
            aftChar = (curIndex + word.length() != lyrics.length() && Character.isLetter(lyrics.charAt(curIndex + word.length())));

            // if the character before or after the word is a character, find the next match index
            if (foreChar || aftChar)
                curIndex = lyrics.indexOf(word, curIndex + 1);
        }

        return curIndex;
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
            System.err.println("usage: prog songfile [-top10words]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);

        List<RankedSong> rankedSongs = new LinkedList<>();

        System.out.printf("Results for \"%s\"%n", args[1]);

        for (Song song : sblw.search(args[1])) {
            int rank = rankPhrase(song.getLyrics(), args[1]);
            if (rank >= 0)
              rankedSongs.add(new RankedSong(rank, song));
        }

        //rankedSongs.sort(RankedSong::compareTo);
        rankedSongs.stream().limit(10).forEach(System.out::println);
        System.out.printf("...total of %d songs%n", rankedSongs.size());
    }

    /**
     * RankedSong class for songs that have been ranked
     * @author Brendon Butler
     */
    static class RankedSong implements Comparable<RankedSong> {
        private final int rank;
        private final Song song;

        public RankedSong(int rank, Song song) {
            this.rank = rank;
            this.song = song;
        }

        /**
         * @return formatted string in the form [rankValue artist, "title"] ex: 13 Aerosmith, "Beyond Beautiful"
         */
        @Override
        public String toString() {
            return String.format("%-4d %19s \"%s\"", rank, song.getArtist(), song.getTitle());
        }

        /**
         * Get the Song value associated with this instance
         *
         * @return Song class associated with this instance
         */
        public Song getSong() {
            return song;
        }

        /**
         * Comparable implementation allows you to compare two RankedSongs for sorting
         *
         * @param rankedSong song for this instance to be compared to
         * @return the comparison result integer
         */
        @Override
        public int compareTo(RankedSong rankedSong) {
            return Integer.compare(rank, rankedSong.rank);
        }
    }
}