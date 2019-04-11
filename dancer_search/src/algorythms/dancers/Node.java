package algorythms.dancers;

/**
 * Binary tree node.
 * Current node holds the right child that is bigger and left node which is lower.
 * Parent node is the previous connecting node of current node.
 * If parent node is null, then current node is the root of binary tree.
 */
public class Node {
    Dancer dancer;
    Node leftChild;
    Node rightChild;
    Node parentNode;

    public Node(Dancer dancer){
        this.dancer = dancer;
        leftChild = null;
        rightChild = null;
        parentNode = null;
    }

}
