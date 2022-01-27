package lib280.tree;

import lib280.base.Container280;
import lib280.base.Dispenser280;
import lib280.base.Searchable280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;
import sun.awt.image.ImageWatched;

import java.awt.*;


@SuppressWarnings("unchecked")
public class AVLTree280<I extends Comparable<? super I>>
        extends OrderedSimpleTree280<I>
        implements Dispenser280<I>, Searchable280<I> {
    /*cur, parent searchesContinue  inherited from Ordered simple tree
      rootNode                      inherited from LinkedSimpleTree*/

    /**	The root of the AVL Tree */
    protected AVLNode280<I> rootNode;

    /**	Create an empty lib280.tree. <br>
     Analysis: Time = O(1) */
    public AVLTree280(){
        rootNode = null;
    }


    /**	Create a lib280.tree with lt, r, and rt being the left subtree, root item, and right subtree
     respectively (lt and/or rt can be null for an empty subtree). <br>
     Analysis: Time = O(1) <br>
     PRECONDITION: <br>
     <ul>
     All items in lt are less than or equal to r <br>
     All items in rt are greater than or equal to r <br>
     (These conditions are not checked because of their time complexity.)
     </ul>
     @param lt lib280.tree to initialize as the left subtree
     @param r item to initialize as the root
     @param rt lib280.tree to initialize as the right subtree */
    public AVLTree280(AVLTree280<I> lt, I r, AVLTree280<I> rt)
    {
        rootNode = createNewNode(r);
        setRootLeftSubtree(lt);
        setRootRightSubtree(rt);
    }

    /**	Create a new node that is appropriate to this lib280.tree.  This method should be
     overidden for classes that extend this class and need a specialized node,
     i.e., a descendant of BinaryNode280.
     Analysis: Time = O(1)
     @param item    The item to be placed in the new node */
    protected AVLNode280<I> createNewNode(I item)
    {
        return new AVLNode280<I>(item);
    }

    /** Returns the root node of the AVL tree
     **/
    protected AVLNode280<I> rootNode(){
        return this.rootNode;
    }

    /**	Contents of the root item.
     Analysis: Time = O(1)
     @precond !isEmpty()
     */
    public I rootItem() throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot access the root of an empty lib280.tree.");

        return rootNode.item();
    }

    /**	Set contents of the root to x.
     Analysis: Time = O(1)
     @precond !isEmpty()
     @param x item to become the new root item
     @throws ContainerEmpty280Exception when tree is empty
     */
    public void setRootItem(I x) throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot set the root of an empty lib280.tree.");

        rootNode.setItem(x);
    }

    /**	Set root node to new node.
     Analysis: Time = O(1)
     @param newNode node to become the new root node */
    protected void setRootNode(AVLNode280<I> newNode)
    {
        rootNode = newNode;
    }


    /**	Retrieve the left subtree
     Analysis: Time = O(1)  <br>
     @return tree's left subtree
     PRECONDITION: <br>
     <ul>
     !isEmpty() <br>
     values in the new left subtree are less than rootItem() <br>
     */
    public AVLTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        AVLTree280<I> result = this.clone();
        result.clear();
        result.setRootNode(rootNode.leftNode());
        return result;
    }

    /**	Retrieve the right subtree
     Analysis: Time = O(1)  <br>
     @return tree's right subtree
     PRECONDITION: <br>
     <ul>
     !isEmpty() <br>
     values in the new left subtree are less than rootItem() <br>
     */
    public AVLTree280<I> rootRightSubtree() throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        AVLTree280<I> result = this.clone();
        result.clear();
        result.setRootNode(rootNode.rightNode());
        return result;
    }


    /**	Delete all items from the data structure. <br>
     Analysis : Time = O(1) */
    public void clear()
    {
        setRootNode(null);
    }


    /**	Set the left subtree to t (set isEmpty if t == null).  <br>
     Analysis: Time = O(1)  <br>
     PRECONDITION: <br>
     <ul>
     !isEmpty() <br>
     values in the new left subtree are less than rootItem() <br>

     (The second condition of the precondition isn't checked as it is too time consuming.
     Don't use this operation on this class unless the condition is known to hold.)
     </ul> */
    public void setRootLeftSubtree(AVLTree280<I>  tree) throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");

        if (tree != null)
            rootNode.setLeftNode(tree.rootNode());
        else
            rootNode.setLeftNode(null);
    }



    /**	Set the right subtree to t (set isEmpty if t == null).  <br>
     Analysis: Time = O(1)  <br>
     PRECONDITION: <br>
     <ul>
     !isEmpty() <br>
     values in the new right subtree are greater than rootItem() <br>

     (The second condition of the precondition isn't checked as it is too time consuming.
     Don't use this operation on this class unless the condition is known to hold.)
     </ul> */
    public void setRootRightSubtree(AVLTree280<I> tree) throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");

        if (tree != null)
            rootNode.setRightNode(tree.rootNode());
        else
            rootNode.setRightNode(null);
    }


    /**	Insert x into the lib280.tree. <br>
     Analysis : Time = O(h) worst case, where h = height of the lib280.tree */
    public void insert(I x){
        if (isEmpty()) {
            rootNode = createNewNode(x);
        }
        else{
            insert(rootNode, x);
        }
    }

    /**	Insert x into the lib280.tree. <br>
     Analysis : Time = O(h) worst case, where h = height of the lib280.tree */
    protected void insert(AVLNode280<I> cur, I data) {

        if (data.compareTo(cur.item()) < 0) {   // is data less than or equal cur
            // if cur is greater than data
            if (cur.leftNode() == null) {
                AVLNode280<I> newItem = createNewNode(data);
                cur.setLeftNode(newItem);
            } else {
                insert(cur.leftNode(), data);
            }
            cur.leftHeight = height(cur.leftNode);

        } else {                                // if data is greater than cur
            if(cur.rightNode() == null){
                AVLNode280<I> newItem = createNewNode(data);
                cur.setRightNode(newItem);
            }
            else {
                insert(cur.rightNode(), data);
            }
            cur.rightHeight = height(cur.rightNode);

        }

//        AVLNode280<I> criticalFinder = searchCritical(rootNode, null);
        restoreAVL(cur, cur.parent);
    }



    /**	Delete the current item, making its replacement the current item. <br>
     Analysis : Time = O(h) worst case, where h = height of the structure <br>
     PRECONDITION: <br>
     <ul>
     itemExists()
     </ul> */
    public void deleteItem(I x) throws ContainerEmpty280Exception{
        if(!itemExists())
            throw new NoCurrentItem280Exception("No current item to delete");
        else if(this.isEmpty()){
            throw new ContainerEmpty280Exception("Tree is empty");
        }
        else{
            search(x);
            deleteItem();
        }
    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {

        if(!itemExists())
            throw new NoCurrentItem280Exception("No current item to delete");

        boolean foundReplacement = false;
        BinaryNode280<I> replaceNode = null;

		/*	Test if there is only one child so it can replace the root. */
        if (cur.rightNode() == null)
        {
            replaceNode = cur.leftNode();
            foundReplacement = true;
        }
        else if (cur.leftNode() == null)
        {
            replaceNode = cur.rightNode();
            foundReplacement = true;
        }
        else
            foundReplacement = false;

        if (foundReplacement)
        {
			/*	Set parent node to refer to the replacement node. */
            if (parent == null)
                setRootNode(replaceNode);
            else if (parent.leftNode() == cur)
                parent.setLeftNode(replaceNode);
            else
                parent.setRightNode(replaceNode);
            cur = replaceNode;

            AVLNode280<I> newP = (AVLNode280) parent;
            AVLNode280<I> newC = (AVLNode280) cur;

            newC.rightHeight = height(newC.rightNode);
            newC.leftHeight = height(newC.leftNode);

            if(cur != rootNode){
                newP.rightHeight = height(newP.rightNode);
                newP.leftHeight = height(newP.leftNode);
            }

            restoreAVL(newC, newP);
        }
        else
        {
			/*	Replace the current item by its inorder successor and
				then delete the inorder successor from its original place. */

			/*	Find the position (replaceParent and replaceCur) of the inorder successor. */
            BinaryNode280<I> replaceParent = cur;
            BinaryNode280<I> replaceCur = replaceParent.rightNode();
            while (replaceCur.leftNode() != null)
            {
                replaceParent = replaceCur;
                replaceCur = replaceParent.leftNode();
            }

			/*	Replace the current item (to be deleted) by the inorder successor. */
            cur.setItem(replaceCur.item());
			/*	Delete the inorder successor from its original place. */
            BinaryNode280<I> saveParent = parent;
            BinaryNode280<I> saveCur = cur;
            parent = replaceParent;
            cur = replaceCur;
            deleteItem();
            parent = saveParent;
            cur = saveCur;

            AVLNode280<I> newP2 = (AVLNode280) parent;
            AVLNode280<I> newC2 = (AVLNode280) cur;

            newC2.rightHeight = height(newC2.rightNode);
            newC2.leftHeight = height(newC2.leftNode);

            if(cur != rootNode){
                newP2.rightHeight = height(newP2.rightNode);
                newP2.leftHeight = height(newP2.leftNode);
            }

            restoreAVL(newC2, newP2);

        }

    }

    /**	Delete the current item, making its replacement the current item. <br>
     Analysis : Time = O(h) worst case, where h = height of the structure <br>
     PRECONDITION: <br>
     <ul>
     itemExists()
     </ul> */


    /**	Go to item x, if it is in the lib280.tree.  If searchesContinue, continue in the right subtree. <br>
     Analysis : Time = O(h) worst case, where h = height of the lib280.tree */
    @Override
    public void search(I x) {
        boolean found = false;
        if (!searchesContinue || above())
        {
            parent = null;
            cur = rootNode;
        }
        else if (!below())
        {
            parent = cur;
            cur = cur.rightNode();
        }
        while (!found && itemExists())
        {
            if (x.compareTo(item()) < 0)
            {
                parent = cur;
                cur = parent.leftNode();
            }
            else if (x.compareTo(item()) > 0)
            {
                parent = cur;
                cur = parent.rightNode();
            }
            else
                found = true;
        }
    }



    /**	Checks if current node is a leaf node or not
     @param cur the current node
     @return true if cur is leaf, false otherwise
     **/
    public boolean isLeaf(AVLNode280<I> cur){
        return (cur.leftNode() == null && cur.rightNode() == null);
    }


    /**	Is the lib280.tree empty?.
     Analysis: Time = O(1)  */
    public boolean isEmpty()
    {
        return rootNode == null;
    }

    /**	Implements a left rotation on a critical tree with RR imbalance
     @param critical is node that has imbalance of 2
     @param top is parent of critical node
     **/
    protected void leftRotate(AVLNode280<I> critical, AVLNode280<I> top){
        /*  critical refers to critical node
            top refers to parent of critical node
         */

        AVLNode280<I> newRoot = critical.rightNode;
        critical.rightNode = newRoot.leftNode;
        critical.rightHeight = height(critical.rightNode);

        if(newRoot.leftNode != null){
            newRoot.leftNode.parent = critical;

        }
        newRoot.parent = critical.parent;


        if(top==null){
            this.rootNode = newRoot;

        }
        else{
            if (top.leftNode.item() == critical.item()) {
                critical.parent.leftNode = newRoot;
                top.leftHeight = height(top.leftNode);
            } else {
                critical.parent.rightNode = newRoot;
                top.rightHeight = height(top.rightNode);
            }
        }

        newRoot.leftNode = critical;
        critical.parent = newRoot;

        newRoot.leftHeight = height(newRoot.leftNode);

    }

    /**	Implements a right rotation on a critical tree with LL imbalance
     @param critical is node that has imbalance of 2
     @param top is parent of critical node
     **/
    protected void rightRotate(AVLNode280<I> critical, AVLNode280<I> top){

        /*  critical refers to critical node
            top refers to parent of critical node
         */

        AVLNode280<I> newRoot = critical.leftNode;
        critical.leftNode = newRoot.rightNode;
        critical.leftHeight = height(critical.leftNode);

        if(newRoot.rightNode != null){
            newRoot.rightNode.parent = critical;
        }
        newRoot.parent = critical.parent;

        if(top==null){
            this.rootNode = newRoot;
        }
        else{
            if (top.leftNode.item() == critical.item()) {
                critical.parent.leftNode = newRoot;
                top.leftHeight = height(top.leftNode);
            } else {
                critical.parent.rightNode = newRoot;
                top.rightHeight = height(top.rightNode);
            }
        }

        newRoot.rightNode = critical;
        critical.parent = newRoot;

        newRoot.rightHeight = height(newRoot.rightNode);

    }

    /**	Implements a double left rotation on a critical tree with LR imbalance
     @param critical is node that has imbalance of 2
     @param top is parent of critical node
     **/
    protected void doubleLeft(AVLNode280<I> critical, AVLNode280<I> top){
        rightRotate(critical.rightNode, top);
        leftRotate(critical, top);
    }

    /**	Implements a double right rotation on a critical tree with RL imbalance
     @param critical is node that has imbalance of 2
     @param top is parent of critical node
     **/
    protected void doubleRight(AVLNode280<I> critical, AVLNode280<I> top){
        leftRotate(critical.leftNode, top);
        rightRotate(critical, top);
    }

    /**	Retrieves height of a tree
     @param root is node whose height needs to be computed
     **/
    public int height(AVLNode280<I> root){
        if(root == null){
            return 0;
        }
        else {
            return 1 + Math.max(root.leftHeight, root.rightHeight);
        }
    }


    @Override
    protected String toStringByLevel(int i) {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = new String();
        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
            result += rootRightSubtree().toStringByLevel(i+1);

        result += "\n" + blanks + i + ": " ;
        if (isEmpty())
            result += "-";
        else
        {
            result += " [" + rootItem() + "]   L:" +rootNode.leftHeight + " R:" + rootNode.rightHeight;
            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                result += rootLeftSubtree().toStringByLevel(i+1);
        }
        return result;
    }



    @SuppressWarnings("unchecked")
    public int getImbalance(AVLNode280<I> critical){
        /* if positive, left heavy. if negative, right heavy*/
        return height(critical.leftNode()) - height( critical.rightNode());
    }


    /**	Finds the critical node and calls appropriate rotation to correct tree im
     @param critical is node that has imbalance of 2
     @param parent is parent of critical node
     **/
    protected void restoreAVL(AVLNode280<I> critical, AVLNode280<I> parent){
        int imbalance = getImbalance(critical);

        if(parent == null){
            return;
        }

        // if no imbalance
        else{
            if ( Math.abs(imbalance) <= 1){
                return;
            }

            // if
            if (imbalance == 2){
                if(getImbalance(critical.leftNode()) >= 0){
                    rightRotate(critical, parent);
                }
                else{
                    leftRotate(critical.leftNode(), parent);
                    rightRotate(critical, parent);
                }
            }
            else{
                if (getImbalance(critical.rightNode()) <=0){
                    leftRotate(critical, parent);
                }
                else{
                    rightRotate(critical.rightNode(), parent);
                    leftRotate(critical, parent);
                }
            }
        }
    }

    /**	A shallow clone of this lib280.tree.
     Analysis: Time = O(1)
     */
    @SuppressWarnings("unchecked")
    public AVLTree280<I> clone()
    {
        return (AVLTree280<I>) super.clone();
    }



    public static void main(String args[]) {
        System.out.println("TESTS START HERE. NOTE:");
        System.out.println("Trees will be printing out in the format: <Level number> : [ item ]     L: leftHeight   R: rightHeight ");
        System.out.println("for every single item in the tree");
        System.out.println("");
        System.out.println("TESTING INSERT FUNCTION");
        System.out.println("_______________________________");

        System.out.println("Creating an empty tree");
        AVLTree280<Integer> T = new AVLTree280<Integer>();

        System.out.println("Testing insert function without rotations yet ");
        System.out.println("Inserting 15 ");
        T.insert(15);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");
        System.out.println();

        System.out.println("Inserting 20 ");
        T.insert(20);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");
        System.out.println();

        System.out.println("Inserting 23 ");
        T.insert(23);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");
        System.out.println();

        System.out.println("Inserting 10 ");
        T.insert(10);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");
        System.out.println();

        System.out.println("Inserting 9 ");
        T.insert(9);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");
        System.out.println();

        //test for left rotation
        System.out.println("Testing for insertion of duplicates ");
        System.out.println("Inserting 10 ");
        T.insert(10);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");
        System.out.println();

        System.out.println("Inserting 14 ");
        T.insert(14);
        System.out.println("This is tree T");
        System.out.println(T.toStringByLevel());
        System.out.println("=====");

        System.out.println("Testing left rotation and for insertion of duplicates ");
        System.out.println("Inserting 14 ");
        T.insert(14);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");

        System.out.println("Testing right rotation");
        T.insert(19);
        T.insert(18);
        T.insert(17);
        System.out.println(T.toStringByLevel());
        System.out.println("=====");

        System.out.println("Testing for RL imbalance or double left");
        T.insert(24);
        T.insert(12);
        System.out.println("After double left");
        System.out.println(T.toStringByLevel());
        System.out.println("=====");


        System.out.println("Testing for LR imbalance or double right");
        System.out.println("Using tree B for this test");
        AVLTree280<Integer> B = new AVLTree280<Integer>();
        B.insert(15);
        B.insert(10);
        B.insert(20);
        B.insert(9);
        B.insert(12);
        System.out.println("This tree B");
        System.out.println(T.toStringByLevel());
        System.out.println("=====");
        B.insert(11);
        B.insert(13);
        System.out.println("After double right");
        System.out.println(T.toStringByLevel());


        System.out.println("TESTING SEARCH FUNCTION");
        System.out.println("_______________________________");

        System.out.println("Testing search function");
        System.out.println("Searching for 20");
        T.search(20);
        if(T.item() == 20) {
            System.out.println("The item searched is: " + T.item());
        }

        System.out.println("Searching for 15");
        T.search(15);
        if(T.item() == 15) {
            System.out.println("The item searched is: " + T.item() + " and it is the root node") ;
        }
        else{
            System.out.println("Error: it did not produce the expected output");
        }

        System.out.println("");
        System.out.println("TESTING DELETE FUNCTION");
        System.out.println("_______________________________");

        T.deleteItem(20);
        System.out.println(T.toStringByLevel());

    }


}