public class ProducerConsumer {
    private static final int PRODUCERS = 5;
    private static final int TRANSLATORS = 2;
    private static final int MESSAGES = 100;

    public static void main(String[] args) throws InterruptedException {
        RingBuffer<String> first = new RingBuffer<>(10);
        RingBuffer<String> second = new RingBuffer<>(10);

        for (int i = 1; i <= PRODUCERS; i++) {
            int id = i;
            Thread producer = new Thread(() -> {
                try {
                    int number = 1;
                    while (true) {
                        first.put("Thread #" + id + " generated message " + number++);
                    }
                } catch (InterruptedException e) {
                }
            });
            producer.setDaemon(true);
            producer.start();
        }

        for (int i = 1; i <= TRANSLATORS; i++) {
            int id = PRODUCERS + i;
            Thread translator = new Thread(() -> {
                try {
                    while (true) {
                        String message = first.take();
                        second.put("Thread #" + id + " translated message \"" + message + "\"");
                    }
                } catch (InterruptedException e) {
                    // stop the thread
                }
            });
            translator.setDaemon(true);
            translator.start();
        }

        for (int i = 1; i <= MESSAGES; i++) {
            System.out.println(i + ". " + second.take());
        }
        System.out.println("Main thread finished.");
    }
}
