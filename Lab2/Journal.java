public class Journal {
    private final MyList entries = new MyArrayList();

    public void add(JournalEntry entry) {
        entries.add(entry);
    }

    public int size() {
        return entries.size();
    }

    public JournalEntry get(int index) {
        return (JournalEntry) entries.get(index);
    }
}
