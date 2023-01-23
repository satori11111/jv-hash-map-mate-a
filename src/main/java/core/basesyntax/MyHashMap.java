package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getIndex(key);
        if (size > threshold) {
            resize();
        }
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(getHash(key),key, value, null);
        } else {
            for (Node<K, V> node = table[getIndex(key)]; node != null; node = node.next) {
                if (node.key == key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(getHash(key),key, value, null);
                    break;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int EntryIndex = getIndex(key);
        Node<K,V> newNode;
        if (table[EntryIndex] == null) {
            return null;
        } else {
            newNode = table[EntryIndex];
             while (newNode != null) {
                if (key == newNode.key || key != null && key.equals(newNode.key)) {
                    return newNode.value;
                }
                 newNode = newNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return key == null ? 0 : getHash(key) % table.length;
    }

    private void resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        int oldThr = threshold;
        int newCap = oldCap * 2;
        threshold = oldThr * 2;
        size = 0;
        table = (Node<K,V>[]) new Node[newCap];
        for (Node<K,V> e:oldTab) {
            while (e != null) {
                put(e.key,e.value);
                e = e.next;
            }
        }
    }
}