package lib280.tree;

import lib280.base.Dispenser280;
import lib280.exception.*;

import java.awt.*;

/**	An implementation of the Dispenser280 interface with
 * functions to access/obtain/remove current item. */

public class ArrayedHeap280<I extends Comparable<? super I>> extends //what's the difference if "I extends Comparable" and this
        ArrayedBinaryTree280<I> implements Dispenser280<I> {

    public ArrayedHeap280(int cap){
        super(cap);
        capacity = cap;
        currentNode = 1;
        count=0;
        items = (I[]) new Comparable[capacity+1];

    }
    @Override
    /**
     * Insert item to the next available node in the tree
     *
     * @param x item to be inserted into heap
     * @precond The tree must not be full
     * @throws ContainerFull280Exception if tree is already full
     */
    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
        if(this.isFull()){
            throw new ContainerFull280Exception("Cannot insert another item. Too full.");
        }
        else {
            count ++;
            items[count] = x;

            int parent = findParent(count);
            int newItem = count;

            while(  parent>0 && items[newItem].compareTo(items[parent]) >= 1){
                swap(parent, newItem);

                newItem = parent;
                parent = findParent(newItem);
            }

        }
    }

    @Override
    /**
     * Deletes item at the root of the heap
     *
     * @precond The tree must not be empty
     * @throws ContainerEmpty280Exception if tree is already empty
     */
    public void deleteItem() throws ContainerEmpty280Exception {
        if(count==0){
            throw new ContainerEmpty280Exception("Cannot delete item if heap is empty/ no item to delete");
        }
        else{
            items[1] = items[count];
            items[count] = null;
            this.count--;

            int left = findLeftChild(1);
            int right = findRightChild(1);


            int next = findPath(left, right, 1);
            currentNode = 1;
            if(items[next] != null){
            while( items[currentNode].compareTo(items[next])== -1 ){

                if( items[next] != null )swap(currentNode, next);

                left = findLeftChild(next);
                right = findRightChild(next);

                //currentNode = next;
                if(left<count || right<count)
                next = findPath(left, right, next);
                else{
                    break;
                }



            }
            currentNode = 1;
        }

    }}

    /**
     * Swaps parent and child in a tree
     *
     * @param up, index to parent; down, index to child
     * @precond parent and child pointed by index should not have same value
     * @throws NoCurrentItem280Exception if parent and child pointed by index have same/duplicated values
     */
    protected void swap(int up, int down) throws DuplicateItems280Exception{

        if(up == down){
            throw new DuplicateItems280Exception("No need to swap, same value");
        }
        else{
            I temp = items[up];
            items[up] = items[down];
            items[down] = temp;
        }
    }

    /**
     * Finds index of child with larger value
     *
     * @param left, right, parent : indexes to children and parent
     * @return index of child with larger value
     */
    protected int findPath(int left, int right, int parent) throws NoCurrentItem280Exception, DuplicateItems280Exception{

        if(right>count){
            return left;
        }
        if(items[left].compareTo(items[right]) >= 1){
            currentNode = parent;
            return left;

        }
        else if(items[left].compareTo(items[right]) == -1){
            currentNode = parent;
            return right;
        }
        else if(items[left].compareTo(items[right]) == 0){
            throw new DuplicateItems280Exception("Duplicated item. No need to swap");
        }
        else{
            throw new NoCurrentItem280Exception("Cannot find next item to be swapped with");
        }
    }

    public static void main(String[] args){

        Exception x = null;

        System.out.println(" Testing constructor. Creates one arrayed heap " );
        ArrayedHeap280 test = new ArrayedHeap280(10);
        System.out.println("  " );
        System.out.println("  " );


        System.out.println(" Testing insert() " );
        System.out.println(" Testing insert() if heap is empty" );
        System.out.println("Before: " + test);
        test.insert(2);     //1
        System.out.println("After: " + test);
        System.out.println("  " );


        System.out.println(" Testing insert() if heap only has one element" );
        System.out.println("Before: " + test);
        test.insert(3);     //2
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println(" Testing insert() on regular heap with two or more elements" );
        System.out.println("Before: " + test);
        test.insert(9);     //3
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println(" Testing insert() if item inserted is smaller than previous elements" );
        System.out.println("Before: " + test);
        test.insert(1);     //4
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println(" Testing insert() if item inserted is larger than previous elements" );
        System.out.println("Before: " + test);
        test.insert(12);    //5
        System.out.println("After: " + test);

        System.out.println("Inserting more items in");
        System.out.println("Before: " + test);
        test.insert(11);    //6
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println(" Testing insert() if item inserted is same as previous element inserted" );

        System.out.println("Before: " + test);
        test.insert(11);    //6
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println("Inserting more items in");
        System.out.println("Before: " + test);
        test.insert(10);       //8
        test.insert(5);         //9
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println(" Testing insert() if next item inserted is last item that can be inserted" );
        System.out.println("Before: " + test);
        test.insert(7);         //10
        System.out.println("After: " + test);
        System.out.println("  " );


        System.out.println(" Testing insert() if heap is already full" );
        x = null;
        try {
            test.insert(4);     //11
        }
        catch(ContainerFull280Exception e) {
            x = e;
        }
        finally {
            if( x == null ) System.out.println("Expected exception if heap is already full.  Got none.");
        }

        System.out.println("  " );
        System.out.println("  " );
        System.out.println(" Testing delete() " );

        System.out.println(" Testing delete() if heap is full" );
        System.out.println("Before: " + test);
        test.deleteItem();  // 1
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println(" Testing delete() if heap's number of items is less than its capacity" );
        System.out.println("Before: " + test);
        test.deleteItem();  // 2
        System.out.println("After: " + test);
        System.out.println("  " );
        System.out.println("Before: " + test);
        test.deleteItem();  // 3
        System.out.println("After: " + test);
        System.out.println("  " );
        System.out.println("Before: " + test);
        test.deleteItem();  // 4
        System.out.println("After: " + test);
        System.out.println("  " );
        System.out.println("Before: " + test);
        test.deleteItem();  // 5
        System.out.println("After: " + test);
        System.out.println("  " );
        System.out.println("Before: " + test);
        test.deleteItem();  // 6
        System.out.println("After: " + test);
        System.out.println("  " );
        System.out.println("Before: " + test);
        test.deleteItem();  // 7
        System.out.println("After: " + test);
        System.out.println("  " );
        System.out.println("Before: " + test);
        test.deleteItem();  // 8;
        System.out.println("After: " + test);
        System.out.println("  " );
        System.out.println("Before: " + test);
        test.deleteItem();  // 9
        System.out.println("After: " + test);
        System.out.println("  " );


        System.out.println(" Testing delete() if there is only one item on heap" );
        System.out.println("Before: " + test);
        test.deleteItem();  // 10
        System.out.println("After: " + test);
        System.out.println("  " );

        System.out.println(" Testing delete() if heap is empty" );

        x = null;
        try {
            test.deleteItem();  // 11
        }
        catch(ContainerEmpty280Exception e) {
            x = e;
            System.out.println("Exception caught!");
        }
        finally {
            if( x == null ) System.out.println("Expected exception if heap is empty.  Got none.");
        }

    }

}