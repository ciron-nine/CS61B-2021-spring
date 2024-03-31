package hashmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author cirno-nine
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private Set<K> keys;
    private int size;
    private int number;

    private double loadFactor;
    @Override
    public void clear() {
        number = 0;
        for(int i = 0; i < size; i ++) {
            buckets[i] = null;
        }
    }
    private int hashTextbook(K key, int num) {
        return (key.hashCode() & 0x7fffffff) % num;
    }
    private int hashTextbook(K key) {
        return (key.hashCode() & 0x7fffffff) % size;
    }
    @Override
    public boolean containsKey(K key) {
        Collection<Node> bucket = buckets[hashTextbook(key)];
        if(bucket == null) {
            return false;
        }
        else {
            for(Node node: bucket) {
                if(node.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Collection<Node> bucket = buckets[hashTextbook(key)];
        if(bucket == null) {
            return null;
        }
        else {
            for(Node node: bucket) {
                if(node.key.equals(key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    private Node get_node(K key) {
        Collection<Node> bucket = buckets[hashTextbook(key)];
        if(bucket == null) {
            return null;
        }
        else {
            for(Node node: bucket) {
                if(node.key.equals(key)) {
                    return node;
                }
            }
        }
        return null;
    }
    @Override
    public int size() {
        return number;
    }

    private void resize(int new_size) {
        Collection<Node>[] new_buckets = new Collection[new_size];
        for(K key: keys) {
            Node node = get_node(key);
            Collection<Node> bucket =  new_buckets[hashTextbook(key, new_size)];
            if(bucket == null) {
                new_buckets[hashTextbook(key, new_size)] = createBucket();
                bucket = new_buckets[hashTextbook(key, new_size)];
                bucket.add(node);

            }
            else {
                bucket.add(node);
            }
        }
        buckets = new_buckets;
        size = new_size;
    }
    @Override
    public void put(K key, V value) {
         Collection<Node> bucket = buckets[hashTextbook(key)];
         if(bucket == null) {
             buckets[hashTextbook(key)] = createBucket();
             buckets[hashTextbook(key)].add(new Node(key, value));
             keys.add(key);
             number ++;
         }
         else {
             boolean is_new = true;
             for(Node node: bucket) {
                 if(node.key.equals(key)) {
                     node.value = value;
                     is_new = false;
                     break;
                 }
             }
             if(is_new) {
                 bucket.add(new Node(key, value));
                 keys.add(key);
                 number++;
             }
         }
         if((double) number / size >= loadFactor) {
             resize(2 * size);
         }
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public V remove(K key) {
        Collection<Node> bucket = buckets[hashTextbook(key)];
        if(bucket == null) {
            return null;
        }
        else {
            V value = null;
            for(Node node: bucket) {
                if(node.key.equals(key)) {
                    number --;
                    value = node.value;
                    bucket.remove(node);
                    break;
                }
            }
            return value;
        }
    }

    @Override
    public V remove(K key, V value) {
        Collection<Node> bucket = buckets[hashTextbook(key)];
        if(bucket == null) {
            return null;
        }
        else {
            V val = null;
            for(Node node: bucket) {
                if(node.key.equals(key) && node.value.equals(value)) {
                    number --;
                    val = node.value;
                    bucket.remove(node);
                    break;
                }
            }
            return val;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = new Collection[16];
        loadFactor = 0.75;
        size = 16;
        number = 0;
        for(int i = 0; i < size; i ++) {
            buckets[i] = null;
        }
        keys = new HashSet<>();
    }

    public MyHashMap(int initialSize) {
        loadFactor = 0.75;
        size = initialSize;
        number = 0;
        buckets = new Collection[initialSize];
        for(int i = 0; i < size; i ++) {
            buckets[i] = null;
        }
        keys = new HashSet<>();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        loadFactor = maxLoad;
        size = initialSize;
        number = 0;
        buckets = new Collection[initialSize];
        for(int i = 0; i < size; i ++) {
            buckets[i] = null;
        }
        keys = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new HashSet<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // Your code won't compile until you do so!

}
