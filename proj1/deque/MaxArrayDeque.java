package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> C;
    public MaxArrayDeque(Comparator<T> c){
        super();
        C = c;
    }
    public T max(){
        if (this.size() == 0)return null;
        T curr = this.get(0);
        for( T cur : this){
            if(C.compare(cur, curr) > 0){
                curr = cur;
            }
        }
        return curr;
    }
    public T max(Comparator<T> c){
        if (this.size() == 0)return null;
        T curr = this.get(0);
        for( T cur : this){
            if(c.compare(cur, curr) > 0){
                curr = cur;
            }
        }
        return curr;
    }

}
