import java.util.SplittableRandom;

public class ParallelMonteCarloPi {

    private static final long ITERATIONS = 100_000_000L;
    private static final long SEED = 42;

    static class Worker extends Thread {
        private final long points;
        private final long seed;
        private long hits = 0;

        Worker(long points, long seed) {
            this.points = points;
            this.seed = seed;
        }

        @Override
        public void run() {
            SplittableRandom random = new SplittableRandom(seed);
            for (long i = 0; i < points; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();
                if (x * x + y * y <= 1.0) {
                    hits++;
                }
            }
        }

        long getHits() {
            return hits;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("Usage: java ParallelMonteCarloPi <threads>");
            return;
        }
        int threads = Integer.parseInt(args[0]);
        if (threads < 1) {
            System.out.println("Number of threads must be at least 1");
            return;
        }

        long start = System.nanoTime();

        Worker[] workers = new Worker[threads];
        long part = ITERATIONS / threads;
        for (int i = 0; i < threads; i++) {
            long points = (i == threads - 1) ? ITERATIONS - part * (threads - 1) : part;
            workers[i] = new Worker(points, SEED + i);
            workers[i].start();
        }

        long totalHits = 0;
        for (Worker w : workers) {
            w.join();
            totalHits += w.getHits();
        }

        double pi = 4.0 * totalHits / ITERATIONS;
        double ms = (System.nanoTime() - start) / 1_000_000.0;

        System.out.printf("PI is %.5f%n", pi);
        System.out.println("THREADS " + threads);
        System.out.printf("ITERATIONS %,d%n", ITERATIONS);
        System.out.printf("TIME %.2fms%n", ms);
    }
    
}
