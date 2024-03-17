package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {

    public Iterator<T> iterator() {
        return new ArrayDequeIterator(NextFirst, NextLast, items.length);
    }
    private T[] items;
    private  int size;

    private  int NextFirst;
    private  int NextLast;

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;
        private  final int size;
        private final int end;
        ArrayDequeIterator(int NextIndex, int last, int length) {
            wizPos = NextIndex + 1;
            if (wizPos == length) {
                wizPos = 0;
            }
            end = last;
            size = length;
        }

        public boolean hasNext() {
            return wizPos != end;
        }

        public T next() {
            T returnItem = items[wizPos];
            wizPos += 1;
            if (wizPos == size) {
                wizPos = 0;
            }
            return returnItem;
        }
    }



    public ArrayDeque() {
        size = 0;
        int SmallSize = 8;
        items = (T[]) new Object[SmallSize];
        NextFirst = 4;
        NextLast = 5;
    }


    @Override
    public void addFirst(T item) {
        if (items.length == size) {
            resize((int) (size * 1.5));
        }

        items[NextFirst] = item;
        NextFirst--;
        if (NextFirst < 0) {
            NextFirst = items.length - 1;
        }
        size++;
    }

    private void resize(int capacity){
        T[] a = (T[]) new Object[capacity];
        int first = NextFirst + 1;
        if (first >= items.length) {
            first = 0;
        }
        for (int i = 0; i < capacity; i += 1) {
            a[i] = items[first];
            first++;
            if (first >= items.length) {
                first = 0;
            }
        }
        NextFirst = capacity - 1;
        NextLast = size;
        items = a;
    }

    @Override
    public void addLast(T item) {
        if (items.length == size) {
            resize((int) (size * 1.5));
        }

        items[NextLast] = item;
        NextLast++;
        if (NextLast >= items.length) {
            NextLast = 0;
        }
        size++;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int first = NextFirst + 1;
        int last = NextLast - 1;
        while(first != last){
            if(first >= items.length) first = 0;
            System.out.print(items[first] + " ");
            first++;
        }
        System.out.println(items[last]);
    }


    @Override
    public T removeFirst() {
        if(items.length >= 16 && size < items.length/4){
            resize(items.length/4);
        }
        NextFirst++;
        if(size == 0){
            NextFirst--;
            return null;
        }
        if (NextFirst >= items.length) {
            NextFirst = 0;
        }
        T cur = items[NextFirst];
        items[NextFirst] = null;
        size--;
        return cur;
    }

    @Override
    public T removeLast() {
        if(items.length >= 16 && size < items.length/4){
            resize(items.length/4);
        }
        NextLast--;
        if (size == 0) {
            NextLast++;
            return null;
        }
        if (NextLast < 0) {
            NextLast = items.length - 1;
        }
        T cur = items[NextLast];
        items[NextLast] = null;
        size--;
        return cur;
    }


    @Override
    public T get(int index) {
        int cur_index = NextFirst + index + 1;
        if(cur_index >= items.length) cur_index -= items.length;
        return items[cur_index];
    }

    public boolean equals(Object o){
        if (o instanceof Deque) {
            Deque o1;
            o1 = (Deque) o;
            int size1 = o1.size();
            int size2 = this.size();
            if (size1 != size2) {
                return false;
            }
            for(int i = 0; i < size1; i++){
                boolean equal = this.get(i).equals(o1.get(i));
                if (!equal) {
                    return false;
                }
            }
            return true;
        }
        else return false;
    }
}
