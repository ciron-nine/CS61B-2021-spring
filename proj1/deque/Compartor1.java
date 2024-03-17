package deque;

import java.util.Comparator;

public class Compartor1 <T> implements Comparator <T> {

    public Compartor1() {

    }
    @Override
    public int compare(T t, T t1) {
        if (t.hashCode() > t1.hashCode()) return 1;
        else if (t.hashCode() == t1.hashCode()) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
