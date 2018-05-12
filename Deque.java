/*******************************************************
 * 
 *          Alexander Bakx - 1283648
 *          Riley Cochrane - 1218251
 *
 *******************************************************/

public class Deque<Item>{

    //Number of items currently in the deque
    private int size;
    //Head item in the deque
    Node<Item> head;
    //Last item in the deque
    Node<Item> last;

    /*****************************************************
    *     Doubly-linked List 
    *****************************************************/
    private class Node<Item> {
	Item item;
	//Pointer to the next item in the deque
        Node<Item> next;
	//Pointer to the previous item in the deque
	Node<Item> previous;

	//Returns the item
	public Item getItem(){
	    return item;
	}
    }

    /*****************************************************
    *     Checks if the deque is empty  
    *****************************************************/
    public boolean isEmpty(){
	return head == null;
    }

    /*****************************************************
    *     Gets the size of the deque    
    *****************************************************/
    public int getSize(){
	return size;
    }

    /*****************************************************
    *     Adds an item to the front of the deque    
    *****************************************************/
    public void addToFront(Item item){
	//Create temp node to store the new item
	Node<Item> newHead = new Node<Item>();
	newHead.item = item;

	//Checks if the deque is not empty
	if(!isEmpty()){
	    //Sets the old head previous pointer to the new head
	    head.previous = newHead;
	    //Sets the new head next pointer to the old head
	    newHead.next = head; 
	}

	//Sets the head to the new head
	head = newHead;
	//If there is no last item sets the last item to be the head too
	if(last == null){
	    last = head;
	}

	//Increases size of deque by 1
	size++;
    }

    /*****************************************************
    *     Adds an item to the end of the deque    
    *****************************************************/
    public void addToEnd(Item item){
	//Create temp node to store the new last item
	Node<Item> newEnd = new Node<Item>();
	newEnd.item = item;

	//Checks if the deque is not empty
	if(!isEmpty()){
	    //Sets new last pointer to the old last item
	    newEnd.previous = last;
	    //Sets the old last pointer to the new last item
	    last.next = newEnd;
	}

	//Sets the last item to the new last item
	last = newEnd;
	//If there is no head sets the head  to be the last item too
	if(head == null){
	    head = last;
	}

	//Increases size of deque by 1
	size++;
    }

    /*****************************************************
    *     Removes an item from the front of the deque  
    *****************************************************/
    public Item removeFromFront(){
	//Create temp node to store the item we want to remove
	Node<Item> oldHead = new Node<Item>();
	//Sets the item we want to remove to the head
	oldHead = head;
	//Sets the head to the next item in the deque
	head = head.next;

	//If the new head is empty
	if(head == null){
	    //Set the last item to be empty too
	    last = null;
	} else {
	    //Remove the pointer to the item we want to remove
	    head.previous = null;
	}

	//Decrease the size of the deque by 1
	size--;

	//Returns the item we removed from the deque
	return oldHead.getItem();
    }
}
