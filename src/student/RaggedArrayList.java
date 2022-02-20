/**  
 * File: RaggedArrayList.java
 * ****************************************************************************
 *                           Revision History
 * ****************************************************************************
 * 02/16/2022 - Brendon Butler - reorganized and commented Task #1
 * 02/16/2022 - Shea Durgin - implementing Task #1 (run())
 * 02/13/2022 - Brendon Butler - updating comments & javadoc(s)
 * 02/12/2022 - Brendon Butler - optimizing Task #1 to match Task #2 solution
 * 02/12/2022 - Lydia Clark - implementing and testing Task #2 (findEnd())
 * 02/10/2022 - Brendon Butler - implementing and testing Task #1 (findFront())
 * 8/2015 - Anne Applin - Added formatting and JavaDoc 
 * 2015 - Bob Boothe - starting code
 * ****************************************************************************
 */
package student;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * * 
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator. Duplicates
 * are allowed. New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them 
 * @author Bob Booth
 * @param <E>  A generic data type so that this structure can be built with any
 * data type (object)
 */
public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces

    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * ***********************************************************
     * nested class for 2nd level arrays 
     * read and understand it.
     * (DONE - do not change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

   
    // ***********************************************************
    
    /**
     * total size (number of entries) in the entire data structure 
     * (DONE - do not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array 
     * (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts: 
     * level 1 index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int level1Index;

        /**
         * Level 2 index
         */
        public int level2Index;

        /**
         * Parameterized constructor DONE (Do Not Change)
         *
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        /**
         * test if two ListLoc's are to the same location 
         * (done -- do not change)
         *
         * @param otherObj the other listLoc
         * @return true if they are the same location and false otherwise
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index
                    && level2Index == other.level2Index;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be one index past the last value in the used level 2 array. Can
         * be used internally to scan through the array for sublist also can be
         * used to implement the iterator.
         */
        public void moveToNext() {
            // TO DO IN PART 5 and NOT BEFORE
        }
    }

    /**
     * find 1st matching entry
     *
     * @author Brendon Butler
     * Revised By: Lydia Clark & Brendon Butler
     * @param item the thing we are searching for a place to put.
     * @return ListLoc of 1st matching item or of 1st item greater than 
     * the item if no match this might be an unused slot at the end of a 
     * level 2 array
     */
    public ListLoc findFront(E item) {
        // TO DO in part 3
        int i1 = 0;  // current L1Array index
        int i2 = 0;  // current L2Array index
        // if the list is empty, skip and return the list location as (0, 0)
        if (size > 0) {
            L2Array l2Array = (L2Array) l1Array[i1];

            // Find the first L2 array that's first value is greater than the input
            while (i1 < l1NumUsed - 1 && comp.compare(item, l2Array.items[l2Array.numUsed - 1]) > 0) {
                i1++;
                l2Array = (L2Array) l1Array[i1];
            }

            // find the first instance of the input in the L2 array
            while (i2 < l2Array.numUsed && comp.compare(item, l2Array.items[i2]) > 0) {
                i2++;
            }

            // adjust the L1 value back by 1 if the comparison is not equal to 0
            if (i1 > 0 && i2 == 0 && comp.compare(item, l2Array.items[i2]) != 0) {
                i1--;
                i2 = l2Array.numUsed - 1;
            }
        }

        // using the found index of the L1 array and L2 array, return a new ListLoc with those values
        return new ListLoc(i1, i2);
    }

    /**
     * find location after the last matching entry or if no match, it finds
     * the index of the next larger item this is the position to add a new 
     * entry this might be an unused slot at the end of a level 2 array
     *
     * @author Lydia Clark
     * @param item the thing we are searching for a place to put.
     * @return the location where this item should go
     */
    public ListLoc findEnd(E item) {
        // TO DO in part 3
        int i1 = 0;  // current L1Array index
        int i2 = 0;  // current L2Array index
        // if the list is empty, skip and return the list location as (0, 0)
        if (size > 0) {
            i1 = l1NumUsed - 1;
            L2Array l2Array = (L2Array)l1Array[l1NumUsed - 1];

            // Find the last L2 array that's first value is greater than the input
            while (i1 > 0 && comp.compare(item, l2Array.items[0]) < 0) {
                i1--;
                l2Array = (L2Array)l1Array[i1];
            }

            // find the last instance of the input in the L2 array
            while (i2 < l2Array.numUsed && comp.compare(item, l2Array.items[i2]) >= 0) {
                i2++;
            }

            // adjust the L1 value forward by 1 if there is another L2 array ahead of the current one
            if (i1 < l1NumUsed - 1 && i2 == l2Array.numUsed) {
                i1++;
                i2 = 0;
            }
        }

        // using the found index of the L1 array and L2 array, return a new ListLoc with those values
        return new ListLoc(i1, i2);
    }

    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     *
     * @author Shea Durgin
     * Revised by: Brendon Butler
     * @param item the thing we are searching for a place to put.
     * @return
     */
    public boolean add(E item) {
        // TO DO in part 4 and NOT BEFORE
        ListLoc location = findEnd(item);
        L2Array l2Array = (L2Array) l1Array[location.level1Index];

        // move the items following the insertion index over by 1
        // then insert the item
        System.arraycopy(l2Array.items, location.level2Index,
                l2Array.items, location.level2Index + 1,
                l2Array.numUsed - location.level2Index);
        l2Array.items[location.level2Index] = item;

        // increment number of items used and size in the l2Array
        l2Array.numUsed++;
        size++;

        // if the L2 array is full, resize or split the array
        if (l2Array.numUsed == l2Array.items.length) {
            /*  If l2Array's length is less than the max length
                determined by the l1Array length, double the l2Array
                else split the array */
            if (l2Array.items.length < l1Array.length) {
                l2Array.items = Arrays.copyOf(l2Array.items,
                        l2Array.items.length * 2);
            } else {
                int l1Index = location.level1Index + 1;
                int halfL2ArrayLength = l2Array.items.length / 2;

                // copy the second half of values from l2Array into a
                // new L2Array, then adjust the numUsed value
                L2Array latterHalf = new L2Array(l2Array.items.length);
                System.arraycopy(l2Array.items, halfL2ArrayLength,
                        latterHalf.items, 0, halfL2ArrayLength);
                latterHalf.numUsed = halfL2ArrayLength;

                // fill the second half of l2Array with null
                // then adjust the numUsed value
                Arrays.fill(l2Array.items, halfL2ArrayLength,
                        l2Array.items.length, null);
                l2Array.numUsed = halfL2ArrayLength;

                // insert new l2Array into l1Array
                System.arraycopy(l1Array, l1Index, l1Array,
                        l1Index + 1, l1NumUsed - l1Index);
                l1Array[l1Index] = latterHalf;

                // increment l1NumUsed
                l1NumUsed++;

                // double length of l1Array if full
                if (l1NumUsed == l1Array.length) {
                    l1Array = Arrays.copyOf(l1Array, l1NumUsed * 2);
                }
            }
        }

        // always return true for add method as it will always be able to expand and add items if needed,
        return true;
    }

    /**
     * check if list contains a match
     *
     * @param item the thing we are looking for.
     * @return true if the item is already in the data structure
     */
    public boolean contains(E item) {
        // TO DO in part 5 and NOT BEFORE

        return false;
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        // TO DO in part 5 and NOT BEFORE

        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive. The original list is
     * unaffected findStart and findEnd will be useful here
     *
     * @param fromElement the starting element
     * @param toElement the element after the last element we actually want
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        // TO DO in part 5 and NOT BEFORE

        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance
     * of the inner Itr() class (DONE)
     *
     * @return an iterator
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iterator is just a list loc. It starts at (0,0) and finishes with index2
     * 1 past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check to see if there are more items
         */
        public boolean hasNext() {
            // TO DO in part 5 and NOT BEFORE

            return false;
        }

        /**
         * return item and move to next throws NoSuchElementException if 
         * off end of list.  An exception is thrown here because calling 
         * next() without calling hasNext() shows a certain level or stupidity
         * on the part of the programmer, so it can blow up. They deserve it.
         */
        public E next() {
            // TO DO in part 5 and NOT BEFORE

            throw new IndexOutOfBoundsException();
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
