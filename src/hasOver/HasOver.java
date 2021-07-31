package hasOver;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HasOver {
    /**
     * Use the ForkJoin framework to write the following method in Java.
     * <p>
     * Returns true if arr has any elements strictly larger than val.
     * For example, if arr is [21, 17, 35, 8, 17, 1], then
     * hasOver(21, arr) == true and hasOver(35, arr) == false.
     * <p>
     * Your code must have O(n) work, O(lg n) span, where n is the length of arr, and actually use the sequentialCutoff
     * argument.
     */
    public static final ForkJoinPool POOL = new ForkJoinPool();

    public static boolean hasOver(int val, int[] arr, int sequentialCutoff) {
        return POOL.invoke(new HasOverTask(arr, arr.length, 0, val, sequentialCutoff));
    }

    private static boolean sequential(int[] arr, int hi, int lo, int val) {
        boolean hasOver = false;
        for (int i = lo; i < hi; i++) {
            if (arr[i] > val) {
                hasOver = true;
                break;
            }
        }
        return hasOver;
    }

    private static void usage() {
        System.err.println("USAGE: HasOver <number> <array> <sequential cutoff>");
        System.exit(2);
    }

    private static class HasOverTask extends RecursiveTask<Boolean> {

        int[] arr;
        int lo;
        int hi;
        int cutoff;
        int val;

        public HasOverTask(int[] arr, int hi, int lo, int val, int cutoff) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.cutoff = cutoff;
        }

        protected Boolean compute() {
            if (hi - lo <= cutoff) {
                return sequential(arr, hi, lo, val);
            }

            int mid = lo + (hi - lo) / 2;

            HasOverTask left = new HasOverTask(arr, hi, mid, val, cutoff);
            HasOverTask right = new HasOverTask(arr, mid, lo, val, cutoff);

            left.fork();
            boolean result = right.compute();
            boolean leftResult = left.join();
            return result || leftResult;
        }

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
            System.out.println(hasOver(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }

    }
}
