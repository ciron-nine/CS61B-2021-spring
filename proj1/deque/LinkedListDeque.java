package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>,Deque<T> {
    private final Node sention;
    private int size;


    private class Node{

        public T item;
        public Node prev;
        public Node next;
        public Node(T items){
            item=items;
            prev=null;
            next=null;
        }
    }


    public LinkedListDeque(){
        size=0;
        sention=new Node(null);
        sention.prev = sention;
        sention.next = sention;
    }

    @Override
    public void addFirst(T item){
        Node temp = new Node(item);
        Node head = sention.next;
        sention.next = temp;
        temp.next = head;
        head.prev = temp;
        temp.prev = sention;
        size++;
    }

    @Override
    public void addLast(T item){
        Node temp = new Node(item);
        Node tail = sention.prev;
        sention.prev = temp;
        temp.next = sention;
        tail.next = temp;
        temp.prev = tail;
        size++;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public T removeFirst(){
        if(sention.next==sention){
            return null;
        }
        Node delete_head = sention.next;
        Node newhead = sention.next.next;
        newhead.prev = sention;
        sention.next = newhead;
        size--;
        return delete_head.item;
    }

    @Override
    public T removeLast(){
        if(sention.next==sention){
            return null;
        }
        Node delete_tail = sention.prev;
        Node newtail = sention.prev.prev;
        newtail.next = sention;
        sention.prev = newtail;
        size--;
        return delete_tail.item;
    }


    @Override
    public T get(int index){
        Node cur=sention.next;
        while (cur != sention){
            if(index == 0) return cur.item;
            index--;
            cur = cur.next;
        }
        return null;
    }

    public Iterator<T> iterator() {
        return new LinkedDequeIterator(sention);
    }
    private class LinkedDequeIterator implements Iterator<T> {
        private final Node senti;
        private Node curr;
        public LinkedDequeIterator(Node next_index){
            senti = next_index;
            curr = senti.next;
        }

        public boolean hasNext() {
            return curr != senti;
        }

        public T next() {
            T items = curr.item;
            curr = curr.next;
            return items;
        }
    }
    private T gethelper(Node cur,int index){
        if(cur == sention)return null;
        if(index == 0)return cur.item;
        else {
            return gethelper(cur.next,index - 1);
        }
    }
    public T getRecursive(int index){
        return gethelper(sention.next,index);
    }


    @Override
    public void printDeque(){
        Node cur = sention.next;
        while(cur != sention){
            System.out.print(cur.item);
            cur = cur.next;
            if(cur != sention) System.out.print(" ");
        }
        System.out.println();
    }

    public boolean equals(Object o){
        if(o instanceof LinkedListDeque){
            LinkedListDeque o1;
            o1 = (LinkedListDeque) o;
            int size1 = o1.size();
            int size2 = this.size();
            if(size1 != size2)return false;
            for(int i = 0;i < size1;i++){
                boolean equal = this.get(i).equals(o1.get(i));
                if(!equal)return false;
            }
            return true;
        }
        else return false;
    }


}
