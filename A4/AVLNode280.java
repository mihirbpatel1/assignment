package lib280.tree;

public class AVLNode280<I extends Comparable<? super I>>
        extends BinaryNode280<I>
        implements Cloneable, Comparable<BinaryNode280<I>>
{
    /** Contents of the node. */

    /** The parent node.**/
    protected AVLNode280<I> parent;

    /** right height of node.**/
    protected int rightHeight;

    /** left height of node.**/
    protected int leftHeight;

    /** The left node. */
    protected AVLNode280<I> leftNode;

    /** The right node. */
    protected AVLNode280<I> rightNode;


    public int compareTo(AVLNode280<I> x) {
        return this.item.compareTo(x.item);
    }

    /** Construct a new node with item x.
     *  @timing Time = O(1)
     *  @param x the item placed in the new node */
    public AVLNode280(I x)
    {
        super(x);
        this.rightHeight=0;
        this.leftHeight=0;
        this.parent= null;
        this.leftNode = null;
        this.rightNode = null;
    }

    /** Contents of the node.
     *  @timing Time = O(1) */
    public I item()
    {
        return item;
    }

    /** The left node.
     *  @timing Time = O(1) */
    public AVLNode280<I> leftNode()
    {
        return leftNode;
    }

    /** The left node.
     *  @timing Time = O(1) */
    public AVLNode280<I> rightNode()
    {
        return rightNode;
    }

    /**
     * Set the item contained in the node.
     * @param x The new item to store in the node.
     * @timing Time = O(1)
     * */
    public void setItem(I x) {
        this.item = x;
    }

    /**
     * Set the left child of this node
     * @param n The new left child of this node.
     */
    public void setLeftNode(AVLNode280<I> n) {
        this.leftNode = n;
        n.parent = this;
    }

    /**
     * Set the right child of this node.
     * @param n The new right child of this node.
     */
    public void setRightNode(AVLNode280<I> n) {
        this.rightNode = n;
        n.parent = this;
    }

}



