/**
 * File: SearchByTitlePrefix.java
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 03/28/2022 - Aidan Bradley - Revisions to statistics method
 * 03/23/2022 - Brendon Butler - Implementing search function & tests
 *                             - Implementing statistics method
 *                             - Implementing Extra Credit top10songs method
 * 03/23/2022 - Aidan Bradley & Brendon Butler - Creating Constructor
 *****************************************************************************
 */
package student;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchByLyricsWords {
    private Set<String> commonWords;
    private TreeMap<String, Set<Song>> lyricToSongMap;

    /**
     * Constructor for SearchByLyricsWords
     *
     * @author Brendon Butler & Aidan Bradley
     * @param sc SongCollection instance containing all songs from input file
     */
    public SearchByLyricsWords(SongCollection sc) {
        // Pattern finds all words [a-zA-Z] that are longer than 1 character, greedily
        Pattern pattern = Pattern.compile("[a-zA-Z]{2,}+");
        lyricToSongMap = new TreeMap<>();

        Instant start = Instant.now();
        this.commonWords = new LinkedHashSet<>(Arrays.asList("all", "an", "and", "are", "as", "at", "be", "been", "but",
                "by", "can", "could", "did", "do", "each", "for", "from", "get", "had", "has", "have", "he", "her",
                "him", "his", "how", "if", "in", "into", "is", "you", "it", "its", "made", "make", "many", "me", "more",
                "my", "no", "not", "now", "of", "on", "one", "or", "our", "see", "she", "so", "some", "than", "that",
                "the", "their", "them", "then", "there", "these", "they", "this", "to", "too", "two", "was", "we",
                "were", "what", "when", "which", "will", "with", "would", "your"));

        // iterate through all songs in the SongCollection
        for (Song song : sc.getAllSongs()) {
            // find all values that match the regex
            Matcher matcher = pattern.matcher(song.getLyrics().toLowerCase());

            // iterate through found matches
            while (matcher.find()) {
                // if the match is a common word, exclude it
                if (!commonWords.contains(matcher.group())) {
                    Set<Song> songSet = lyricToSongMap.get(matcher.group());

                    // if the songSet doesn't exist, create it
                    if (songSet == null) {
                        songSet = new TreeSet<>();
                    }

                    // add the song to the set of songs and the word to the map as a key with the set of songs as the value
                    songSet.add(song);
                    lyricToSongMap.put(matcher.group(), songSet);
                }
            }
        }
        Instant end = Instant.now();
        System.out.printf("%19s: %.3fs%n", "Execution Time", Duration.between(start, end).toMillis() / 1000D);
    }

    /**
     * Method to print statistics for the SearchByLyricsWords class
     *
     * @author Brendon Butler
     * Revised By: Aidan Bradley
     */
    public void statistics() {
        int count = 0;

        // calculate Song references
        for (Set<Song> set : lyricToSongMap.values()) {
            count += set.size();
        }

        System.out.printf("%19s: %,d%n", "Number of Keys", lyricToSongMap.size());
        System.out.printf("%19s: %,d%n", "Song References", count);
        System.out.printf("%19s: %,d bytes%n", "Map Space Used", lyricToSongMap.size() * 8);
        System.out.printf("%19s: %,d bytes%n", "Songs Space Used", count * 8);
        System.out.printf("%19s: %,d bytes%n", "Compound Space Used", lyricToSongMap.size() * 8 + count * 8);
        System.out.printf("%19s: O(%s)%n", "Space Complexity", "n");
    }

    /**
     * EXTRA CREDIT:
     * Print out the top 10 words (words with the most Song references)
     *
     * @author Brendon Butler
     */
    public void top10words() {
        // create a new sorted tree map to ensure order
        SortedMap<Integer, String> top10 = new TreeMap<>();

        Instant start = Instant.now();
        // loop through each entry in lyricsToSongMap
        for (Map.Entry<String, Set<Song>> entry : lyricToSongMap.entrySet()) {
            // if there are less than 10 entries, add the value, else...
            if (top10.size() < 10) {
                top10.put(entry.getValue().size(), entry.getKey());
            } else {
                /* ...loop through the quantities, if the current value is greater than the
                 * count of one of the previous most common words, remove the least common
                 * word from the key set and add the new word
                 */
                for (Integer keyQuantity : top10.keySet()) {
                    if (keyQuantity < entry.getValue().size()) {
                        top10.remove(top10.firstKey());
                        top10.put(entry.getValue().size(), entry.getKey());
                        break;
                    }
                }
            }
        }
        Instant end = Instant.now();

        final int[] i = {10};
        System.out.printf("%nTOP 10 WORDS (execution time: %.3fs):%n", Duration.between(start, end).toMillis() / 1000D);
        top10.forEach((k, v) -> System.out.printf("%2d) %6s [%5d matches]%n", i[0]--, v, k));
    }

    /**
     * testing method for this unit
     * @param args command line arguments set in Project Properties -
     * the first argument is the data file name, the second being a flag
     * for processing the top10words method
     *
     * Adapted from SearchByArtistPrefix
     * Revised by: Brendon Butler
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [-top10words]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);

        sblw.statistics();

        if (args.length > 1 && args[1].equalsIgnoreCase("-top10words"))
            sblw.top10words();
    }
}