package deque;

import java.util.Comparator;

public class Compartor2 <T> implements Comparator <T> {

    public Compartor2() {

    }
    @Override
    public int compare(T t, T t1) {
        int test = t.toString().compareTo(t1.toString());
        if (test > 0) return 1;
        else if (test == 0) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
