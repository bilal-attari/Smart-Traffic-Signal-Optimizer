package traffic.dsa;

import java.util.ArrayList;

public class SimpleChainedHashTable {
    private ArrayList<Entry>[] table;

    @SuppressWarnings("unchecked")
    public SimpleChainedHashTable(int size) {
        table = new ArrayList[size];
        for (int i = 0; i < table.length; i++) {
            table[i] = new ArrayList<Entry>();
        }
    }

    public void put(int key, String value) {
        int index = hash(key);
        ArrayList<Entry> chain = table[index];
        for (int i = 0; i < chain.size(); i++) {
            Entry entry = chain.get(i);
            if (entry.key == key) {
                entry.value = value;
                return;
            }
        }
        chain.add(new Entry(key, value));
    }

    public String get(int key) {
        int index = hash(key);
        ArrayList<Entry> chain = table[index];
        for (int i = 0; i < chain.size(); i++) {
            Entry entry = chain.get(i);
            if (entry.key == key) {
                return entry.value;
            }
        }
        return null;
    }

    public int hash(int key) {
        if (key < 0) {
            key = key * -1;
        }
        return key % table.length;
    }

    private static class Entry {
        int key;
        String value;

        Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
