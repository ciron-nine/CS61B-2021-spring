package bstmap;


import java.util.*;

public class BSTMap<K extends Comparable<K>, V>  implements Map61B<K, V>, Iterable<K>{


    private Node root;             // root of BST
    private int size;
    private class Node {
        private K key;           // sorted by key
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    private void helper(Node cur) {
        if(cur == null) return;
        helper(cur.left);
        helper(cur.right);
        System.out.println(cur.val);
    }
    public void printInOrder() {
        helper(root);
    }
    public BSTMap() {
        size = 0;
    }
    public void clear() {
        size = 0;
        root = null;
    }

    public boolean containsKey(K key) {
        Node test = get_node(key);
        if(test == null) {
            return false;
        }
        return true;
    }

    private Node get_node(K key) {
        Node cur = root;
        while(cur != null) {
            if (cur.key.compareTo(key) == 0) {
                return cur;
            }
            else if (cur.key.compareTo(key) > 0) {
                cur = cur.right;
            }
            else {
                cur = cur.left;
            }
        }
        return null;
    }
    public V get(K key) {
        Node cur = root;
        while(cur != null) {
            if (cur.key.compareTo(key) == 0) {
                return cur.val;
            }
            else if (cur.key.compareTo(key) > 0) {
                cur = cur.right;
            }
            else {
                cur = cur.left;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        Node cur = root;
        Node prev = root;
        if(root == null) {
            size ++;
            root = new Node(key, value, 1);
            return;
        }
        while(cur != null) {
            prev = cur;
            if(cur.key.compareTo(key) > 0) {
                cur = cur.right;
            }
            else if(cur.key.compareTo(key) < 0) {
                cur = cur.left;
            }
            else {
                cur.val = value;
                break;
            }
        }
        if(cur == null) {
            cur = new Node(key, value, 1);
            size ++;
            if(prev.key.compareTo(key) > 0) {
                prev.right = cur;
            }
            else {
                prev.left = cur;
            }
        }
    }

    public Set<K> keySet() {
        Set<K> key = new TreeSet<>();
        if(root == null) {
            return key;
        }
        else {
            Queue<Node> que = new LinkedList<>();
            que.add(root);
            while(!que.isEmpty()) {
                Node cur = que.poll();
                key.add(cur.key);
                if(cur.left != null) {
                    que.add(cur.left);
                }
                if(cur.right != null) {
                    que.add(cur.right);
                }
            }
        }
        return key;
    }

    private void check_left_or_right(Node prev, Node cur) {
        if(prev.left == cur) {
            prev.left = null;
        }
        else {
            prev.right = null;
        }
    }
    private class BSTItreator<K> implements Iterator<K> {

        private Queue<Node> que;
        public BSTItreator(Node root) {
            que = new LinkedList<>();
            que.add(root);
        }
        @Override
        public boolean hasNext() {
            return !que.isEmpty();
        }

        @Override
        public K next() {
            Node cur = que.poll();
            if(cur.left != null) {
                que.add(cur.left);
            }
            if(cur.right != null) {
                que.add(cur.right);
            }
            return (K) cur.key;
        }
    }
    public V remove(K key) {
        Node cur = root;
        Node prev = root;
        while(cur != null) {
            if(cur.key.compareTo(key) > 0) {
                prev = cur;
                cur = cur.right;
            }
            else if(cur.key.compareTo(key) < 0) {
                prev = cur;
                cur = cur.left;
            }
            else {
                break;
            }
        }
        if(cur == null) {
            return null;
        }
        else {
            size --;
            V value = cur.val;
            if(cur == root) {
                if(cur.left == null && cur.right == null) {
                    root = null;
                }
                else if(cur.left == null || cur.right == null) {
                    if(cur.left == null) {
                        root = root.right;
                    }
                    else {
                        root = root.left;
                    }
                }
                else {
                    Node small_node = root.right;
                    while(small_node.left != null) {
                        small_node = small_node.left;
                    }
                    remove(small_node.key);
                    small_node.left = root.left;
                    small_node.right = root.right;
                    root = small_node;
                }
            }
            else {
                if(cur.left == null && cur.right == null) {
                    if(prev.left == cur) {
                        prev.left = null;
                    }
                    else {
                        prev.right = null;
                    }
                }
                else if(cur.left == null) {
                    if(prev.left == cur) {
                        prev.left = cur.right;
                    }
                    else {
                        prev.right = cur.right;
                    }
                }
                else if(cur.right == null) {
                    if(prev.left == cur) {
                        prev.left = cur.left;
                    }
                    else {
                        prev.right = cur.left;
                    }
                }
                else {
                    Node small_node = root.right;
                    while(small_node.left != null) {
                        small_node = small_node.left;
                    }
                    remove(small_node.key);
                    small_node.left = cur.left;
                    small_node.right = cur.right;
                    cur = small_node;
                    if(prev.left == cur) {
                        prev.left = small_node;
                    }
                    else {
                        prev.right = small_node;
                    }
                }
            }
            return value;
        }
    }

    public V remove(K key, V value) {
        Node cur = root;
        Node prev = root;
        while(cur != null) {
            if(cur.key.compareTo(key) > 0) {
                prev = cur;
                cur = cur.right;
            }
            else if(cur.key.compareTo(key) < 0) {
                prev = cur;
                cur = cur.left;
            }
            else {
                if(value.equals(cur.val)) {
                    break;
                }
                else {
                    cur = null;
                    break;
                }
            }
        }
        if(cur == null) {
            return null;
        }
        else {
            size --;
            if(cur == root) {
                if(cur.left == null && cur.right == null) {
                    root = null;
                }
                else if(cur.left == null || cur.right == null) {
                    if(cur.left == null) {
                        root = root.right;
                    }
                    else {
                        root = root.left;
                    }
                }
                else {
                    Node small_node = root.right;
                    while(small_node.left != null) {
                        small_node = small_node.left;
                    }
                    remove(small_node.key);
                    small_node.left = root.left;
                    small_node.right = root.right;
                    root = small_node;
                }
            }
            else {
                if(cur.left == null && cur.right == null) {
                    if(prev.left == cur) {
                        prev.left = null;
                    }
                    else {
                        prev.right = null;
                    }
                }
                else if(cur.left == null) {
                    if(prev.left == cur) {
                        prev.left = cur.right;
                    }
                    else {
                        prev.right = cur.right;
                    }
                }
                else if(cur.right == null) {
                    if(prev.left == cur) {
                        prev.left = cur.left;
                    }
                    else {
                        prev.right = cur.left;
                    }
                }
                else {
                    Node small_node = root.right;
                    while(small_node.left != null) {
                        small_node = small_node.left;
                    }
                    remove(small_node.key);
                    small_node.left = cur.left;
                    small_node.right = cur.right;
                    cur = small_node;
                    if(prev.left == cur) {
                        prev.left = small_node;
                    }
                    else {
                        prev.right = small_node;
                    }
                }
            }
            return value;
        }
    }

    public Iterator<K> iterator() {
        return new BSTItreator<>(root);
    }
}
