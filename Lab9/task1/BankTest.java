import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class BankTest {
    private static final int ACCOUNTS = 100;
    private static final int THREADS = 5000;

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        Random random = new Random();

        Account[] accounts = new Account[ACCOUNTS];
        for (int i = 0; i < ACCOUNTS; i++) {
            accounts[i] = new Account(i, random.nextInt(1000));
        }

        long before = bank.total(accounts);
        System.out.println("Accounts: " + ACCOUNTS + ", threads: " + THREADS);
        System.out.println("Total money before: " + before);

        AtomicInteger done = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();
        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(() -> {
                ThreadLocalRandom r = ThreadLocalRandom.current();
                Account from = accounts[r.nextInt(ACCOUNTS)];
                Account to = accounts[r.nextInt(ACCOUNTS)];
                int amount = r.nextInt(500);
                if (bank.transfer(from, to, amount)) {
                    done.incrementAndGet();
                } else {
                    rejected.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long after = bank.total(accounts);
        System.out.println("Transfers done: " + done.get());
        System.out.println("Transfers rejected: " + rejected.get());
        System.out.println("Total money after: " + after);
        System.out.println(before == after ? "OK: total money is the same"
                : "ERROR: money was lost or created!");
    }
}
