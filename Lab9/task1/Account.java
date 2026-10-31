public class Account {
    private final int id;
    private int balance;
    public Account(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }
    public int getId() {
        return id;
    }
    public int getBalance() {
        return balance;
    }
    public boolean withdraw(int amount) {
        if (amount <= 0 || balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }
    public void deposit(int amount) {
        balance += amount;
    }
}
