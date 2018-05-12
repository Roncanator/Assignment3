import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>{

    //Number of items currently in the deque
    private int size;
    Node first;
    Node last;
    
    private class Node {
	Item item;
        Node next;
	Node previous;
    }

    public boolean isEmpty(){
	return first == null;
    }
    
    public int getSize(){
	return size;
    }

    public void addToFront(Item item){
	
	Node newFirst = new Node();
	newFirst.item = item;
	
	if(!isEmpty()){
	    first.previous = newFirst;
	    newFirst.next = first; 
	}
	
	first = newFirst;
	if(last == null){
	    last = first;
	}

	size++;
    }

    public void addToEnd(Item item){
	
	Node newEnd = new Node();
	newEnd.item = item;
	
	if(!isEmpty()){
	    newEnd.previous = last;
	    last.next = newEnd;
	}
	
	last = newEnd;
	if(first == null){
	    first = last;
	}

	size++;
    }

    public Item removeFromFront(){

	Node oldFirst = new Node();
	oldFirst = first;
	first = first.next;

	if(first == null){
	    last = null;
	} else {
	    first.previous = null;
	}

	size--;

	return oldFirst.item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ItemsIterator();
    }

    private class ItemsIterator implements Iterator<Item> {

        private Node current;

        public ItemsIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null)
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
