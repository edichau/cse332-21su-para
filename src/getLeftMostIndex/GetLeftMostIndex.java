package getLeftMostIndex;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import cse332.exceptions.NotYetImplementedException;

public class GetLeftMostIndex {
    /**
     * Use the ForkJoin framework to write the following method in Java.
     *
     * Returns the index of the left-most occurrence of needle in haystack (think of needle and haystack as
     * Strings) or -1 if there is no such occurrence.
     *
     * For example, getLeftMostIndex("cse332", "Dudecse4ocse332momcse332Rox") == 9 and
     * getLeftMostIndex("sucks", "Dudecse4ocse332momcse332Rox") == -1.
     *
     * Your code must actually use the sequentialCutoff argument. You may assume that needle.length is much
     * smaller than haystack.length. A solution that peeks across subproblem boundaries to decide partial matches
     * will be significantly cleaner and simpler than one that does not.
     */

    private static final ForkJoinPool POOL = new ForkJoinPool();
    public static int getLeftMostIndex(char[] needle, char[] haystack, int sequentialCutoff) {
        return POOL.invoke(new GetLeftMostIndexTask(needle, haystack, sequentialCutoff, 0, haystack.length));
    }

    public static Integer sequential(char[] needle, char[] haystack, int lo, int hi) {
        for (int i = lo; i < hi; i++){
            if(needle[0] == haystack[i]){
                for(int j = 0; j < needle.length; j++) {
                    if(i + j >= haystack.length) {
                        return -1;
                    }
                    if(needle[j] != haystack[i + j]) {
                        break;
                    }
                    if(j == needle.length - 1) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    public static class GetLeftMostIndexTask extends RecursiveTask<Integer> {
        char[] needle;
        char[] haystack;
        int cutoff;
        int lo;
        int hi;

        public GetLeftMostIndexTask(char[] needle, char[] haystack, int cutoff, int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
            this.cutoff = cutoff;
            this.needle = needle;
            this.haystack = haystack;
        }

        public Integer compute() {
            if (hi - lo <= cutoff) {
                return sequential(needle, haystack, lo, hi);
            }

            int mid = lo + (hi - lo) / 2;

            GetLeftMostIndexTask left = new GetLeftMostIndexTask(needle, haystack, cutoff, lo, mid);
            GetLeftMostIndexTask right = new GetLeftMostIndexTask(needle, haystack, cutoff, mid, hi);

            left.fork();
            int rightVal = right.compute();
            int leftVal = left.join();

            if (leftVal != -1 && rightVal != -1) {
                return Math.min(leftVal, rightVal);
            }
            return Math.max(leftVal, rightVal);

        }

    }
    private static void usage() {
        System.err.println("USAGE: GetLeftMostIndex <needle> <haystack> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        char[] needle = args[0].toCharArray();
        char[] haystack = args[1].toCharArray();
        try {
            System.out.println(getLeftMostIndex(needle, haystack, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}
