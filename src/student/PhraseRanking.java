/**
 * File: PhraseRanking.java
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
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
        String[] phraseWords = lyricsPhrase.toLowerCase().split(" ");
        lyrics = lyrics.toLowerCase().replaceAll("\\W+", " ");

        int foundIndex = lyrics.indexOf(phraseWords[0]);
        int bestRank = -1;

        if (lyrics.contains(lyricsPhrase)) {
            foundIndex = -1;
            bestRank = lyricsPhrase.length();
        }

        // find all the starting points by the first search word
        while (foundIndex != -1) {
            Queue<String> words = new ArrayDeque<>(Arrays.asList(phraseWords));
            int rank = 0;
            int curIndex = 0;

            while (!words.isEmpty() && curIndex >= 0) {
                curIndex = getExactWordIndex(lyrics, words.peek(), 1);

                if (curIndex >= 0) {
                    words.poll();
                    rank += curIndex;
                }
            }

            if (words.isEmpty() && (rank < bestRank || bestRank < 0) && rank >= lyricsPhrase.length()) {
                bestRank = rank;
            }

            foundIndex = getExactWordIndex(lyrics, phraseWords[0], ++foundIndex);
        }

        return bestRank;
    }

    private static int getExactWordIndex(String lyrics, String word, int adjust) {
        int curIndex = lyrics.indexOf(word, adjust);
        boolean foreChar = true, aftChar = true;

        while (curIndex >= 0) {
            foreChar = (curIndex != 0 && Character.isLetter(lyrics.charAt(curIndex - 1)));
            aftChar = (curIndex + word.length() != lyrics.length() && Character.isLetter(lyrics.charAt(curIndex + word.length())));

            if (foreChar && aftChar)
                curIndex = lyrics.indexOf(word, curIndex + 1);
            else break;
        }

        return (!foreChar && !aftChar) ? curIndex : -1;
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
            if (rank >= args[1].length())
              rankedSongs.add(new RankedSong(rank, song));
        }

        //rankedSongs.sort(RankedSong::compareTo);
        rankedSongs.stream().limit(10).forEach(System.out::println);
        System.out.printf("...total of %d songs%n", rankedSongs.size());
    }

    static class RankedSong implements Comparable<RankedSong> {
        private final int rank;
        private final Song song;

        public RankedSong(int rank, Song song) {
            this.rank = rank;
            this.song = song;
        }

        @Override
        public String toString() {
            return String.format("%-4d %19s \"%s\"", rank, song.getArtist(), song.getTitle());
        }

        @Override
        public int compareTo(RankedSong rankedSong) {
            return Integer.compare(rank, rankedSong.rank);
        }
    }
}