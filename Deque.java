public class Deque<Item>{

    //Number of items currently in the deque
    private int size;
    Node<Item> first;
    Node<Item> last;
    
    private class Node<Item> {
	Item item;
        Node<Item> next;
	Node<Item> previous;

	public Item getItem(){
	    return item;
	}
    }

    public boolean isEmpty(){
	return first == null;
    }
    
    public int getSize(){
	return size;
    }

    public void addToFront(Item item){
	
	Node<Item> newFirst = new Node<Item>();
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
	
	Node<Item> newEnd = new Node<Item>();
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

	Node<Item> oldFirst = new Node<Item>();
	oldFirst = first;
	first = first.next;

	if(first == null){
	    last = null;
	} else {
	    first.previous = null;
	}

	size--;

	return oldFirst.getItem();
    }
}
