package deque;

public class ArrayDeque<T> {
    private T[] items;
    private  int size;

    private  int next_first;
    private  int next_last;

    public ArrayDeque(){
        size=0;
        int small_size=8;
        items= (T[])new Object[small_size];
        next_first = 4;
        next_last = 5;
    }


    public void addFirst(T item) {
        if(items.length == size)resize((int) (size*1.01) );

        items[next_first]=item;
        next_first--;
        if(next_first < 0) next_first = items.length - 1;
        size++;
    }

    private void resize(int capacity){
        T[] a = (T[]) new Object[capacity];
        int first = next_first+1;
        if(first >= items.length) first = 0;
        for (int i = 0; i < capacity; i += 1) {
            a[i] = items[first];
            first++;
            if(first >= items.length) first=0;
        }
        next_first = capacity-1;
        next_last = size;
        items = a;
    }
    public void addLast(T item) {
        if(items.length == size)resize((int) (size*1.5) );

        items[next_last]=item;
        next_last++;
        if(next_last >= items.length) next_last=0;
        size++;
    }

    public boolean isEmpty() {
        return size==0 ? true : false;
    }


    public int size() {
        return size;
    }

    public void printDeque() {
        int first = next_first + 1;
        int last = next_last - 1;
        while(first != last){
            if(first >= items.length) first=0;
            System.out.print(items[first]+" ");
            first++;
        }
        System.out.println(items[last]);
    }

    public T removeFirst() {
        if(items.length >= 16 && size < items.length/4){
            resize(items.length/4);
        }
        next_first++;
        if(size == 0){
            next_first--;
            return null;
        }
        if(next_first >= items.length) next_first = 0;
        T cur = items[next_first];
        items[next_first] = null;
        size--;
        return cur;
    }

    public T removeLast() {
        if(items.length >= 16 && size < items.length/4){
            resize(items.length/4);
        }
        next_last--;
        if(size == 0){
            next_last++;
            return null;
        }
        if(next_first < 0) next_first = items.length - 1;
        T cur = items[next_last];
        items[next_last] = null;
        size--;
        return cur;
    }

    public T get(int index) {
        int cur_index = next_first + index + 1;
        if(cur_index >= items.length) cur_index -= items.length;
        return items[cur_index];
    }
}
