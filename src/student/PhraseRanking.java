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

import java.util.LinkedList;
import java.util.List;

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
        String selection = lyrics.toLowerCase().replaceAll("[\\n|\\r]", "  ")
                .replaceAll("[,.]", " ");
        String[] lyricsWord = lyricsPhrase.toLowerCase().split(" ");

        if (selection.contains(lyricsWord[0]))
            selection = selection.substring(selection.indexOf(lyricsWord[0]));

        int bestRank = -1;
        int rank;

        while (selection.contains(lyricsWord[0])) {
            int start = selection.indexOf(lyricsWord[0]);
            int curIndex = 0;
            int lastWord;

            for (lastWord = 0; lastWord < lyricsWord.length; lastWord++) {
                curIndex = selection.indexOf(lyricsWord[lastWord] + " ", curIndex);

                if (curIndex < 0) break;
            }

            if (lastWord == lyricsWord.length)
                rank = curIndex - start + lyricsWord[lastWord - 1].length();
            else rank = -1;

            if (rank > 0 && (bestRank > rank || bestRank < 0))
                bestRank = rank;
            selection = selection.substring(start + lyricsWord[0].length());
        }

        return bestRank;
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