package algorithms.dancers;

import java.util.ArrayList;
import java.util.List;

/**
 * References:
 * http://algorithms.tutorialhorizon.com/binary-search-tree-complete-implementation/
 * http://algs4.cs.princeton.edu/32bst/BST.java.html
 */

public class BinaryTree {
    /**
     * Binary tree root, which holds next siblings.
     */
    public Node root;

    /**
     * Method deletes the node from binary tree and rebuilds the tree.
     * @param dancersHeight dancer height parameter
     * @return
     */
    public boolean delete(int dancersHeight) {
        Node parent = root;
        Node currentNode = root;
        boolean isLeftChild = true;

        while (currentNode.dancer.getHeight() != dancersHeight) {

            parent = currentNode;

            if (dancersHeight < currentNode.dancer.getHeight()) {
                isLeftChild = true;
                currentNode = currentNode.leftChild;
            } else {
                isLeftChild = false;
                currentNode = currentNode.rightChild;
            }
            if (currentNode == null) {
                return false;
            }

        }
        if (currentNode.leftChild == null && currentNode.rightChild == null) {
            if (currentNode == root)
                root = null;
            else if (isLeftChild)
                parent.leftChild = null;
            else parent.rightChild = null;
        } else if (currentNode.rightChild == null) {
            if (currentNode == root)
                root = currentNode.leftChild;

            else if (isLeftChild) parent.leftChild = currentNode.leftChild;
            else parent.rightChild = currentNode.leftChild;

        } else if (currentNode.leftChild == null) {
            if (currentNode == root) root = currentNode.rightChild;
            else if (isLeftChild) parent.leftChild = currentNode.rightChild;
            else parent.rightChild = currentNode.rightChild;
        } else {

            Node replaceNodeParent = currentNode;
            Node replacement = currentNode;
            Node watchingNode = currentNode.rightChild;
            while (watchingNode != null) {
                replaceNodeParent = replacement;
                replacement = watchingNode;
                watchingNode = watchingNode.leftChild;
            }
            if (replacement != currentNode.rightChild) {
                replaceNodeParent.leftChild = replacement.rightChild;
                replacement.rightChild = currentNode.rightChild;
            }

            if (currentNode == root) root = replacement;
            else if (isLeftChild) parent.leftChild = replacement;
            else parent.rightChild = replacement;

            replacement.leftChild = currentNode.leftChild;
        }
        return true;
    }

    /**
     * Method finds higher male dancer to the compared dancer height recursively.
     * @param comparedDancerHeight
     * @param sourceNode
     * @return
     */
    public Dancer findMaleDancer(int comparedDancerHeight, Node sourceNode) {

        if (sourceNode == null) {
            return null;
        }

        if (sourceNode.dancer.getHeight() <= comparedDancerHeight) {
            return findMaleDancer(comparedDancerHeight, sourceNode.rightChild);
        }
        Dancer foundMale = findMaleDancer(comparedDancerHeight, sourceNode.leftChild);
        if (foundMale != null && foundMale.getHeight() > comparedDancerHeight) {
            return foundMale;
        } else {
            if (Math.abs(sourceNode.dancer.getHeight() - comparedDancerHeight) < 6) {
                return sourceNode.dancer;
            } else {
                return null;
            }
        }

    }

    /**
     * Method finds lower female dancer to the compared dancer height recursively.
     * @param comparedDancerHeight
     * @param sourceNode
     * @return
     */
    public Dancer findFemaleDancer(int comparedDancerHeight, Node sourceNode) {
        if (comparedDancerHeight <= 0) return null;
        if (sourceNode == null) {
            return null;
        }
        if (sourceNode.dancer.getHeight() >= comparedDancerHeight) {
            return findFemaleDancer(comparedDancerHeight, sourceNode.leftChild);
        }
        Dancer foundFemale = findFemaleDancer(comparedDancerHeight, sourceNode.rightChild);
        if (foundFemale != null && foundFemale.getHeight() < comparedDancerHeight) {
            return foundFemale;
        } else {
            if (Math.abs(sourceNode.dancer.getHeight() - comparedDancerHeight) < 6) {
                return sourceNode.dancer;
            } else {
                return null;
            }
        }
    }

    /**
     * Method returns binary tree nodes in ascending order.
     * @return
     */
    public List<Dancer> getDancerListAscending() {
        List<Dancer> list = new ArrayList<>();
        populateOrderedDancerList(this.root, list);
        return list;
    }

    private void populateOrderedDancerList(Node root, List<Dancer> listToPopulate){
        if (root != null) {
            populateOrderedDancerList(root.leftChild, listToPopulate);
            listToPopulate.add(root.dancer);
            populateOrderedDancerList(root.rightChild, listToPopulate);
        }
    }

    /**
     * Methods adds new node to the binary tree.
     * @param dancer
     */
    public void addNode(Dancer dancer) {
        Node newNode = new Node(dancer);
        if (root == null) {
            root = newNode;
            root.parentNode = null;
        } else {
            Node parentNode;
            Node watchingNode = root;
            while (true) {
                parentNode = watchingNode;
                if (dancer.getHeight() < watchingNode.dancer.getHeight()) {
                    watchingNode = watchingNode.leftChild;
                    if (watchingNode == null) {
                        newNode.parentNode = parentNode;
                        parentNode.leftChild = newNode;
                        return;
                    }
                } else {
                    watchingNode = watchingNode.rightChild;
                    if (watchingNode == null) {
                        newNode.parentNode = parentNode;
                        parentNode.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    }




}
