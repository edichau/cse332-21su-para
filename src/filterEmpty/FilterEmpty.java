package filterEmpty;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import cse332.exceptions.NotYetImplementedException;

public class FilterEmpty {
    static ForkJoinPool POOL = new ForkJoinPool();

    /**
     * Use the ForkJoin framework to write the following method in Java.
     *
     * Returns an array with the lengths of the non-empty strings from arr (in order)
     * For example, if arr is ["", "", "cse", "332", "", "hw", "", "7", "rox"], then
     * filterEmpty(arr) == [3, 3, 2, 1, 3].
     *
     * A parallel algorithm to solve this problem in O(lg n) span and O(n) work is the following:
     * (1) Do a parallel map to produce a bit set
     * (2) Do a parallel prefix over the bit set
     * (3) Do a parallel map to produce the output
     *
     * In lecture, we wrote parallelPrefix together, and it is included in the gitlab repository.
     * Rather than reimplementing that piece yourself, you should just use it. For the other two
     * parts though, you should write them.
     *
     * Do not bother with a sequential cutoff for this exercise, just have a base case that processes a single element.
     */
    public static int[] filterEmpty(String[] arr) {
        int[] bits = mapToBitSet(arr);
        // System.out.println(java.util.Arrays.toString(bitset));
        int[] bitsum = ParallelPrefixSum.parallelPrefixSum(bits);
        // System.out.println(java.util.Arrays.toString(bitsum));
        int[] result = mapToOutput(arr, bits, bitsum);
        return result;
    }

    public static int[] mapToBitSet(String[] arr) {
        throw new NotYetImplementedException();
    }


    public static int[] mapToOutput(String[] input, int[] bits, int[] bitsum) {
        throw new NotYetImplementedException();
    }

    private static void usage() {
        System.err.println("USAGE: FilterEmpty <String array>");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }

        String[] arr = args[0].replaceAll("\\s*", "").split(",");
        System.out.println(Arrays.toString(filterEmpty(arr)));
    }
}