import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSum {

    private static final int SIZE = 1_000_000;
    private static final int THRESHOLD = 20;

    // Task: sum of numbers in array[from..to)
    static class SumTask extends RecursiveTask<Long> {
        private final int[] array;
        private final int from;
        private final int to;

        SumTask(int[] array, int from, int to) {
            this.array = array;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            // small part: just count it
            if (to - from < THRESHOLD) {
                long sum = 0;
                for (int i = from; i < to; i++) {
                    sum += array[i];
                }
                return sum;
            }
            // big part: split into two halves
            int middle = (from + to) / 2;
            SumTask left = new SumTask(array, from, middle);
            SumTask right = new SumTask(array, middle, to);
            left.fork();                      // left half runs in another thread
            long rightSum = right.compute();  // right half runs here
            long leftSum = left.join();       // wait for left half
            return leftSum + rightSum;
        }
    }

    public static void main(String[] args) {
        int[] array = new int[SIZE];
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            array[i] = random.nextInt(101);   // 0..100
        }
        System.out.println("Array created, size: " + SIZE);

        // ForkJoin sum
        ForkJoinPool pool = new ForkJoinPool();
        long start = System.nanoTime();
        long forkJoinSum = pool.invoke(new SumTask(array, 0, SIZE));
        double forkJoinTime = (System.nanoTime() - start) / 1_000_000.0;

        // Simple sum for checking
        start = System.nanoTime();
        long simpleSum = 0;
        for (int value : array) {
            simpleSum += value;
        }
        double simpleTime = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Parallelism (threads in pool): " + pool.getParallelism());
        System.out.printf("ForkJoin sum: %d (%.2f ms)%n", forkJoinSum, forkJoinTime);
        System.out.printf("Simple sum:   %d (%.2f ms)%n", simpleSum, simpleTime);
        System.out.println(forkJoinSum == simpleSum ? "Result is correct" : "Result is WRONG");
        pool.shutdown();
    }
}
