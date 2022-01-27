package lib280.dispenser;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.tree.ArrayedBinaryTreeIterator280;
import lib280.tree.IterableArrayedHeap280;

import java.awt.*;

public class PriorityQueue280<I extends Comparable<? super I>> {


	// This is the heap that we are restricting.
	// Items in the priority queue get stored in the heap.
	protected IterableArrayedHeap280<I> items;

	/**
	 * Create a new priorty queue with a given capacity.
	 *
	 * @param cap The maximum number of items that can be in the queue.
	 */
	public PriorityQueue280(int cap) {
		items = new IterableArrayedHeap280<I>(cap);

	}

	/*
	 insert item g into t in priority order with the highest number being the highest priority.
		@param item
	 */
	public PriorityQueue280 insert(I item) throws ContainerFull280Exception {
		if(this.items.isFull()){
			throw new ContainerFull280Exception("Container Full!");
		}
		this.items.insert(item);
		return this;
	}

	/**
	 * Checks if queue is full or not
	 *
	 *  @return true if there queue is full, false otherwise
	 */
	public boolean isFull() {
		return this.items.count() == this.items.capacity();
	}

	/**
	 * Checks if queue has items inside or none
	 *
	 *  @return true if queue does not have any items inside/empty
	 */
	public boolean isEmpty() throws ContainerEmpty280Exception {

		return this.items.isEmpty();
	}

	/**
	 * Outputs number of items inside queue
	 *
	 *  @return number of items in queue
	 */
	public int count() {
		if (this.isEmpty()) {
			return 0;
		} else if (this.isFull()) {
			return this.items.capacity();
		} else {
			return this.items.count();
		}
	}

	/**
	 * Obtains the item with highest priority in queue
	 * @throws ContainerEmpty280Exception when queue is empty
	 *  @return item at root
	 */
	public I maxItem() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Queue is empty! Can't retrieve largest item");
		}
		ArrayedBinaryTreeIterator280 find = new ArrayedBinaryTreeIterator280(this.items);
		find.goFirst();
		return this.items.item();
	}

	/**
	 * Obtains the item with least priority inside the queue
	 *  @return item obtained by minItem, the smallest item in queue
	 */
	public I minItem() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Queue is empty! Can't retrieve smallest item");
		}

		ArrayedBinaryTreeIterator280 find = new ArrayedBinaryTreeIterator280(this.items);
		find.goFirst();
		I saved = this.items.item();
		find.goForth();

		while(!find.after()){
			if(this.items.item().compareTo(saved) < 0){
				saved = this.items.item();
				find.goForth();
			}
			else find.goForth();
		}

		return saved;


	}
	/**
	 * Deletes the item with largest priority in queue
	 *  @return queue after deleting the item with largest priority
	 */
	public PriorityQueue280 deleteMax() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Queue is empty! Can't delete largest item");
		}
		ArrayedBinaryTreeIterator280 find = new ArrayedBinaryTreeIterator280(this.items);
		find.goFirst();
		this.items.deleteItem();
		return this;
	}

	/**
	 * Deletes item with least priority
	 *
	 *  @return queue after deleting the item with least priority
	 */
	public PriorityQueue280 deleteMin() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Queue is empty! Can't delete smallest item");
		}
		ArrayedBinaryTreeIterator280 find = new ArrayedBinaryTreeIterator280(this.items);
		find.goFirst();

//		while((!find.after()) ){
//			if(minItem().compareTo(this.items.item())==0){
//				this.items.deleteAtPosition(find);
//				break;
//			}
//			else
//			find.goForth();
//		}

		return this;
	}

	/**
	 * Deletes items with highest priorities
	 *
	 *  @return queue after deleting the item with highest priorities
	 */
	public PriorityQueue280 deleteAllMax() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Queue is empty! Can't delete any item");
		}
		I max = maxItem();
		while (this.maxItem() == max) {
			this.deleteMax();
		}
		return this;
	}

	public String toString() {
		return items.toString();
	}

	// TODO
	// Add Priority Queue ADT methods (from the specification) here.


	/* UNCOMMENT THE REGRESSION TEST WHEN YOU ARE READY*/

	public static void main(String args[]) {
		class PriorityItem<I> implements Comparable<PriorityItem<I>> {
			I item;
			Double priority;

			public PriorityItem(I item, Double priority) {
				super();
				this.item = item;
				this.priority = priority;
			}

			public int compareTo(PriorityItem<I> o) {
				return this.priority.compareTo(o.priority);
			}

			public String toString() {
				return this.item + ":" + this.priority;
			}
		}

		PriorityQueue280<PriorityItem<String>> Q = new PriorityQueue280<PriorityItem<String>>(5);

		// Test isEmpty()
		if (!Q.isEmpty())
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");

		// Test insert() and maxItem()
		Q.insert(new PriorityItem<String>("Sing", 5.0));
		if (Q.maxItem().item.compareTo("Sing") != 0) {
			System.out.println("??Error: Front of queue should be 'Sing' but it's not. It is: " + Q.maxItem().item);
		}

		// Test isEmpty() when queue not empty
		if (Q.isEmpty())
			System.out.println("Error: Queue is not empty, but isEmpty() says it is.");

		// test count()
		if (Q.count() != 1) {
			System.out.println("Error: Count should be 1 but it's not.");
		}

		// test minItem() with one element
		if (Q.minItem().item.compareTo("Sing") != 0) {
			System.out.println("Error: min priority item should be 'Sing' but it's not.");
		}

		// insert more items
		Q.insert(new PriorityItem<String>("Fly", 5.0));
		if (Q.maxItem().item.compareTo("Sing") != 0)
			System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Dance", 3.0));
		if (Q.maxItem().item.compareTo("Sing") != 0)
			System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Jump", 7.0));
		if (Q.maxItem().item.compareTo("Jump") != 0)
			System.out.println("Front of queue should be 'Jump' but it's not.");

		if (Q.minItem().item.compareTo("Dance") != 0) System.out.println("minItem() should be 'Dance' but it's not.");

		if (Q.count() != 4) {
			System.out.println("Error: Count should be 4 but it's not.");
		}

		// Test isFull() when not full
		if (Q.isFull())
			System.out.println("Error: Queue is not full, but isFull() says it is.");

		Q.insert(new PriorityItem<String>("Eat", 10.0));
		if (Q.maxItem().item.compareTo("Eat") != 0) System.out.println("Front of queue should be 'Eat' but it's not.");

		if (!Q.isFull())
			System.out.println("Error: Queue is full, but isFull() says it isn't.");

		// Test insertion on full queue
		try {
			Q.insert(new PriorityItem<String>("Sleep", 15.0));
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got none.");
		} catch (ContainerFull280Exception e) {
			// Expected exception
		} catch (Exception e) {
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got a different exception.");
			e.printStackTrace();
		}

		// test deleteMin
		Q.deleteMin();
		if (Q.minItem().item.compareTo("Sing") != 0) System.out.println("Min item should be 'Sing', but it isn't.");

		Q.insert(new PriorityItem<String>("Dig", 1.0));
		if (Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		// Test deleteMax
		Q.deleteMax();
		if (Q.maxItem().item.compareTo("Jump") != 0)
			System.out.println("Front of queue should be 'Jump' but it's not.");

		Q.deleteMax();
		if (Q.maxItem().item.compareTo("Fly") != 0) System.out.println("Front of queue should be 'Fly' but it's not.");

		if (Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		Q.deleteMin();
		if (Q.maxItem().item.compareTo("Fly") != 0) System.out.println("Front of queue should be 'Fly' but it's not.");

		Q.insert(new PriorityItem<String>("Scream", 2.0));
		Q.insert(new PriorityItem<String>("Run", 2.0));

		if (Q.maxItem().item.compareTo("Fly") != 0) System.out.println("Front of queue should be 'Fly' but it's not.");

		// test deleteAllMax()
		Q.deleteAllMax();
		if (Q.maxItem().item.compareTo("Scream") != 0)
			System.out.println("Front of queue should be 'Scream' but it's not.");
		if (Q.minItem().item.compareTo("Scream") != 0) System.out.println("minItem() should be 'Scream' but it's not.");
		Q.deleteAllMax();

		// Queue should now be empty again.
		if (!Q.isEmpty())
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");

		System.out.println("Regression test complete.");
	}

}
