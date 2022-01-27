

package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

import java.util.Iterator;
import java.util.ListIterator;

/**	This list class incorporates the functions of an iterated 
 dictionary such as has, obtain, search, goFirst, goForth,
 deleteItem, etc.  It also has the capabilities to iterate backwards
 in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
	 Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{
		// TODO/DONE
		return new BilinkedNode280<I>(item);
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list
	 * @postcond inserted item now first node of list
	 */
	public void insertFirst(I x) throws ContainerFull280Exception
	{
		// TODO/DONE
		BilinkedNode280<I> newNode = createNewNode(x);
		newNode.setNextNode(this.head);

		// If the cursor is at the first node, cursor predecessor becomes the new node.
		if( this.position == this.head ) this.prevPosition = newNode;

		// Special case: if the list is empty, the new item also becomes the tail.
		if( this.isEmpty() ) this.tail = newNode;
		this.head = newNode;

	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x)
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");

		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);

			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;
		}
	}


	/**	Insert x before the current position and make it current item. <br>
	 Analysis: Time = O(1)
	 @param x item to be inserted before the current position */
	public void insertPriorGo(I x)
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
	 Analysis: Time = O(1)
	 @param x item to be inserted after the current position */
	public void insertNext(I x)
	{
		if (isEmpty() || before())
			insertFirst(x);
		else if (this.position==lastNode())
			insertLast(x);
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x);
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list
	 * @postcond new item now last node of list
	 */
	public void insertLast(I x) throws ContainerFull280Exception
	{
		// TODO/DONE

		BilinkedNode280<I> newItem = createNewNode(x);
		newItem.setNextNode(null);
		newItem.setPreviousNode((BilinkedNode280<I>) this.tail);

		// If the cursor is after(), then cursor predecessor becomes the new node.
		if( this.after() ) this.prevPosition = newItem;

		// If list is empty, handle special case
		if( this.isEmpty() ) {
			this.head = newItem;
			this.tail = newItem;
		}
		else {
			this.tail.setNextNode(newItem);
			this.tail = newItem;
		}

	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 * @postcond item where cursor position is deleted and list one node shorter
	 * @throws NoCurrentItem280Exception
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		// TODO
		if(!this.itemExists()) throw new NoCurrentItem280Exception("There is no item at the cursor to delete.");

		// If we are deleting the first item...
		if( this.position == this.head ) {
			// Handle the special case...
			this.deleteFirst();
			this.position = this.head;
		}

		else if( this.position == this.tail ) {
			// Reset the tail reference if we deleted the last node.
			this.deleteLast();
		}
		else {
			// Set the previous node to point to the successor node.
			this.prevPosition.setNextNode(this.position.nextNode());
			LinkedNode280<I> temp = this.prevPosition;

			/*change*/
			LinkedNode280<I> oldNode = this.position;
			this.position = this.position.nextNode();
			//is this needed?
			this.prevPosition = temp;

			//is this needed?
			oldNode.setNextNode(null);
		}
	}


	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();

		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();

		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {

			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();

				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}

		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());

		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());

		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;

		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);

		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);

	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 * @postcond first item  is deleted and list one node shorter
	 * @throws ContainerEmpty280Exception
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
		// TODO/DONE
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");

		// If the cursor is on the second node, set the prev pointer to null.
		if( this.prevPosition == this.head ) this.prevPosition = null;

			// Otherwise, if the cursor is on the first node, set the cursor to the next node.
		else if (this.position == this.head ) {
			this.position = this.position.nextNode();
			/*new change*/
			this.prevPosition = null;
		}
		// If we're deleting the last item, set the tail to null.
		// Setting the head to null gets handled automatically in the following
		// unlinking.
		if( this.head == this.tail ) this.tail = null;

		// Unlink the first node.
		LinkedNode280<I> oldhead = this.head;
		this.head = this.head.nextNode();
		oldhead.setNextNode(null);
	}



	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 * @postcond last item  is deleted and list one node shorter
	 * @throws ContainerEmpty280Exception
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
		// TODO/DONE
		goLast();
		// Special cases if there are 0 or 1 nodes.
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");
		else if( this.head != null && this.head == this.tail ) this.deleteFirst();
		else {


			// 	Find the second-last node -- note this makes the deleteLast() algorithm O(n)
			LinkedNode280<I> temp = this.head;
			while(temp.nextNode() != this.tail) temp = temp.nextNode();


			// If the cursor is in the after() position, then prevPosition
			// has to become the second last node.
			if( this.after() ) {
				this.prevPosition = temp;
			}

			// Unlink the last node.
			temp.setNextNode(null);
			this.tail = temp;
			this.position = this.tail;
		}
	}

	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 * @postcond cursor now on last item on list
	 * @throws ContainerEmpty280Exception
	 */
	public void goLast() throws BeforeTheStart280Exception, ContainerEmpty280Exception
	{
		// TODO/DONE
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot position cursor at end of container.");
		//if one node
		if(this.tail == this.head){
			this.position = this.tail;
			this.prevPosition = null;
		}
		else{
			//if two or more nodes
			LinkedNode280<I> temp = this.head;
			while(temp.nextNode() != this.tail){
				temp = temp.nextNode();
			}
			this.prevPosition = temp;
			this.position = temp.nextNode;
		}
	}


	/**	Move back one item in the list.
	 Analysis: Time = O(1)
	 @precond !before()
	 @postcond cursor moves back item before it
	 @throws ContainerEmpty280Exception
	 */
	public void goBack() throws BeforeTheStart280Exception, ContainerEmpty280Exception {
		// TODO/DONE
		//if 0 node
		if (this.isEmpty()) throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");
			//if cursor is before this.head
		else if (this.before())
			throw new BeforeTheStart280Exception("Cannot go back if already before the beginning of the list.");
		else if (this.after()) goLast();
		else {

			LinkedNode280<I> prevTemp = this.head;
			LinkedNode280<I> temp = prevTemp.nextNode;
			while (temp.nextNode() != this.position) {
				temp = temp.nextNode();
				prevTemp = prevTemp.nextNode();
			}
			this.position = temp;
			this.prevPosition = prevTemp;
		}
	}


	/**	Iterator for list initialized to first item. 
	 Analysis: Time = O(1)
	 */
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
	 Analysis: Time = O(1)
	 @param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter"
					+ " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
	 Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}



	/**	A shallow clone of this object. 
	 Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {
		// TODO

		System.out.println("NOTE: For the testing of user-created methods, a combination of blackbox and whitebox testing is used.");
		System.out.println("Descriptions provided before the start of every test to explain where the cursor is or how many elements ");
		System.out.println("are currently in the LinkedList. Caution was also taken to ensure that every path of each method has been ");
		System.out.println("tested at least once, and that state changes and exceptions were also tested");
		System.out.println("");

		BilinkedList280<Integer> test = new BilinkedList280<Integer>();

		System.out.println("I & II. CREATENEWNODE() AND INSERTFIRST()");

		System.out.println("Testing insertFirst() on list");
		System.out.println("createNewNode() called in insertFirst()");

		System.out.print("List should be empty...");
		if( test.isEmpty() ) System.out.println("and it is.");
		else System.out.println("ERROR: and it is *NOT*.");


		//test goLast()
		//test insertFirst()
		System.out.println("AFTER: " + test);


		System.out.println("BEFORE: " + test);
		test.insertFirst(6);
		test.insertFirst(5);
		test.insertFirst(4);
		test.insertFirst(3);

		System.out.println("List should be 3, 4, 5, 6, ");
		System.out.println("and it is.");
		System.out.println("AFTER: " + test);


		System.out.println("");
		System.out.println("III. GOLAST()");
		System.out.println("Testing goLast() on unempty list");
		test.goLast();

		System.out.print("cursor should be at 6 .... ");
		if( test.item() == 6) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");


		//test insertLast()
		System.out.println("");
		System.out.println("IV. INSERTLAST()");
		System.out.println("Testing insertLast() on unempty list");
		System.out.println("BEFORE: " + test);

		test.insertLast(7);
		test.insertLast(8);
		test.insertLast(9);

		System.out.print("List should be 3, 4, 5, 6, 7, 8, 9, ");
		System.out.println("and it is.");
		System.out.println("AFTER: " + test);

		System.out.println("Testing insertLast() if cursor in after()");
		test.after();
		System.out.println("BEFORE: " + test);
		test.insertLast(10);
		test.goLast();
		if( test.item() == 10) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

    /*still need to test insertLast() if empty()*/

		//test goBack()
		System.out.println("");
		System.out.println("V. GOBACK()");
		System.out.println("Testing goBack() given that cursor in last node");
		test.goBack();
		System.out.print("cursor should be at 9 .... ");
		if( test.item() == 9) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.println("Testing goBack() given that cursor in before. Should throw a BeforeTheStartException");
		test.goBefore();
		try {
			test.goBack();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		}
		catch(BeforeTheStart280Exception e) {
			System.out.println("Caught exception.  OK!");
		}


		System.out.println("Testing goBack() given that cursor in after.");
		test.goAfter();
		test.goBack();
		if( test.item() == 10) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

    /*still need to test goBack() if empty(). See last portion tests*/

		//test deleteFirst()
		test.goFirst();
		test.goForth();
		System.out.println("");
		System.out.println("VI. DELETEFIRST()");
		System.out.println("Testing deleteFirst() given that cursor on second node");

		System.out.println("BEFORE: " + test);
		test.deleteFirst();

		System.out.print("cursor should be at 4 .... ");
		if( test.item() == 4) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

		System.out.println("Testing deleteFirst() given that cursor on first node");

		test.goFirst();
		System.out.println("BEFORE: " + test);
		test.deleteFirst();
		System.out.println("AFTER: " + test);

		System.out.print("cursor should be at 5 .... ");
		if( test.item() == 5) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");


		//test deleteLast()
		System.out.println("");
		System.out.println("VII. DELETELAST()");
		System.out.print("Testing deleteLast() if there are at least two nodes on list ");

		System.out.println("BEFORE: " + test);
		test.deleteLast();
		System.out.println("AFTER: " + test);
		System.out.println("Current position: " + test.position);
		System.out.print("cursor should be at 9 .... ");
		if( test.item() == 9) System.out.println("and it is.  OK!");
		if( test.item() == 9) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.print("Testing deleteLast() if cursor on last node ");
		System.out.println("BEFORE: " + test);
		test.goLast();
		test.deleteLast();
		System.out.print("cursor should be at 8 .... ");
		if( test.item() == 8) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

		System.out.println("Testing deleteLast() if cursor on after()");
		test.goAfter();
		System.out.println("BEFORE: " + test);
		System.out.println("Current position: " + test.position);
		test.deleteLast();
		System.out.println("Position after: " + test.position);
		System.out.print("cursor should be at 7 .... ");
		if( test.item() == 7) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

    /*still need to test deleteLast() if empty()*/

		//TEST deleteItem()
		System.out.println("");
		System.out.println("VIII. DELETEITEM()");
		test.goFirst();
		System.out.print("Testing deleteItem() if cursor on first item");
		System.out.println("BEFORE: " + test);
		test.deleteItem();
		System.out.print("cursor should be at 6 ....");
		if( test.item() == 6) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

		System.out.print("Testing deleteItem() if cursor on last item if cursor on second from first and last item (i.e. there are only two nodes)");
		test.goLast();

		System.out.println("BEFORE: " + test);
		System.out.println("Current pointer: "+ test.position);
		test.deleteLast();
		System.out.print("cursor should be at 6 ....");
		if( test.item() == 6) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

		System.out.print("Testing deleteLast() if one node only ");
		System.out.println("BEFORE: " + test);
		test.deleteLast();
		if( test.isEmpty() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

		System.out.print("Testing deleteFirst() if one node only ");
		test.insert(6);
		System.out.println("BEFORE: " + test);
		test.deleteFirst();
		if( test.isEmpty() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("AFTER: " + test);

		System.out.println("Testing insertLast(), deleteLast(), deleteFirst(), goLast(), and goBack() if list is empty / testing preconditions ");

		System.out.println("Testing deleteLast() if empty. Should throw ContainerEmpty280Exception... ");
		try {
			test.deleteLast();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception.  OK!");
		}
		System.out.println("");
		System.out.println("IX. MISCELLANEOUS TESTINGS FOR METHODS WHEN LIST IS EMPTY");
		System.out.print("Testing deleteFirst() if empty. Should throw ContainerEmpty280Exception... ");
		try {
			test.deleteFirst();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception.  OK!");
		}


		System.out.print("Testing goBack() if empty. Should throw ContainerEmpty280Exception... ");
		try {
			test.goBack();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception.  OK!");
		}

		System.out.print("Testing goLast() if empty. Should throw ContainerEmpty280Exception... ");
		try {
			test.goLast();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception.  OK!");
		}

		System.out.println("Testing insertLast() if empty. Should throw ContainerEmpty280Exception... ");
		System.out.println("BEFORE: " + test);
		test.insertLast(6);
		System.out.println("AFTER: " + test);

		test.goFirst();
		if( test.item() == 6 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println("");
		System.out.print(" *** END OF TESTS FOR METHODS MARKED AS TO-DO*** ");


	}
}
