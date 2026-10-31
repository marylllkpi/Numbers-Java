public class Bank {

    public boolean transfer(Account from, Account to, int amount) {
        if (from == to) {
            return false;
        }

        Account first = from.getId() < to.getId() ? from : to;
        Account second = from.getId() < to.getId() ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (!from.withdraw(amount)) {
                    return false; 
                }
                to.deposit(amount);
                return true;
            }
        }
    }

    // Sum of money on all accounts.
    public long total(Account[] accounts) {
        long sum = 0;
        for (Account account : accounts) {
            sum += account.getBalance();
        }
        return sum;
    }
}
