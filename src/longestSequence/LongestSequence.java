package longestSequence;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import cse332.exceptions.NotYetImplementedException;

public class LongestSequence {
    /**
     * Use the ForkJoin framework to write the following method in Java.
     *
     * Returns the length of the longest consecutive sequence of val in arr.
     * For example, if arr is [2, 17, 17, 8, 17, 17, 17, 0, 17, 1], then
     * getLongestSequence(17, arr) == 3 and getLongestSequence(35, arr) == 0.
     *
     * Your code must have O(n) work, O(lg n) span, where n is the length of arr, and actually use the sequentialCutoff
     * argument. We have provided you with an extra class SequenceRange. We recommend you use this class as
     * your return value, but this is not required.
     */
    public static final ForkJoinPool POOL = new ForkJoinPool();

    public static int getLongestSequence(int val, int[] arr, int sequentialCutoff) {
        SequenceRange result = POOL.invoke(new LongestSequenceTask(val,arr,sequentialCutoff,0,arr.length));
        return result.longestRange;
    }

    public static SequenceRange sequential(int val, int[] arr, int lo, int hi) {
        int count = 0;
        int greatestCount = 0;
        for (int i = lo; i < hi; i++){
            if (arr[i] == val){
                count++;
            } else {
                count = 0;
            }
            greatestCount = Math.max(count, greatestCount);
        }

        int matchingOnLeft = 0;
        int matchingOnRight = 0;

        while ((lo + matchingOnLeft < hi) && arr[lo + matchingOnLeft] == val) {
            matchingOnLeft++;
        }
        while ((hi - 1 - matchingOnRight) >= lo && arr[hi - 1 - matchingOnRight] == val ) {
            matchingOnRight++;
        }

        return new SequenceRange(matchingOnLeft, matchingOnRight, greatestCount, hi-lo);
    }

    private static class LongestSequenceTask extends RecursiveTask<SequenceRange> {

        int[] arr;
        int lo;
        int hi;
        int cutoff;
        int val;

        public LongestSequenceTask(int val, int[] arr, int cutoff, int lo, int hi) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.cutoff = cutoff;
        }

        @Override
        protected SequenceRange compute() {
            if (hi - lo <= cutoff) {
                return sequential(val, arr, lo, hi);
            }
            int mid = (lo + hi) / 2;
            LongestSequenceTask leftSeq = new LongestSequenceTask(val, arr, cutoff, lo, mid);
            LongestSequenceTask rightSeq = new LongestSequenceTask(val, arr, cutoff, mid, hi);
            leftSeq.fork();

            SequenceRange r = rightSeq.compute();
            SequenceRange l = leftSeq.join();

            int left = l.matchingOnLeft;
            int right = r.matchingOnRight;

            if(l.matchingOnLeft == l.sequenceLength) {
                left = l.sequenceLength + r.matchingOnLeft;
            }
            if(r.matchingOnRight == r.sequenceLength) {
                right = r.sequenceLength + l.matchingOnRight;
            }

            int max = Math.max(l.longestRange, Math.max(left, right));
            max = Math.max(max,  Math.max(r.longestRange, l.matchingOnRight + r.matchingOnLeft));

            return new SequenceRange(left, right, max, r.sequenceLength + l.sequenceLength);
        }
    }

    private static void usage() {
        System.err.println("USAGE: LongestSequence <number> <array> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        int val = 0;
        int[] arr = null;

        try {
            val = Integer.parseInt(args[0]);
            String[] stringArr = args[1].replaceAll("\\s*",  "").split(",");
            arr = new int[stringArr.length];
            for (int i = 0; i < stringArr.length; i++) {
                arr[i] = Integer.parseInt(stringArr[i]);
            }
            System.out.println(getLongestSequence(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}