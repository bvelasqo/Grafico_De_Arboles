/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafico;

/**
 *
 * @author Brandon Velasquez,Mariana Palacios,Maria Alejandra Franco
 */
public class BinarySearchTree {

    private BinaryNode root;

    public BinaryNode getRoot() {
        return root;
    }
    private BinaryNode father;
    private boolean position;
    int contNodos=0, contLeaves=0, sizeTree=0;
    String[] niveles;

    public BinarySearchTree() {
        root = null;
    }

    public BinarySearchTree(int data) {
        root = new BinaryNode(data);
        contNodos++;
    }

    public boolean Add(int data) {
        if (root == null) {
            root = new BinaryNode(data);
        } else //validar si el dato ya existe
        {
            if (Search(data) != null) {
                System.out.println("Dato repetido, no se puede insertar");
                return false;
            } else {
                Add(data, root);
                contNodos++;
            }
        }
        return true;
    }

    private void Add(int data, BinaryNode currentRoot) {
        if (data < currentRoot.getData()) {
            if (currentRoot.getLeft() == null) {
                currentRoot.setLeft(new BinaryNode(data));
            } else {
                Add(data, currentRoot.getLeft());
            }

        } else if (currentRoot.getRight() == null) {
            currentRoot.setRight(new BinaryNode(data));
        } else {
            Add(data, currentRoot.getRight());
        }
    }

    public BinaryNode Search(int data) {
        return Search(data, root);
    }

    private BinaryNode Search(int data, BinaryNode currentRoot) {
        if (currentRoot == null) {
            return null;
        }
        if (data == currentRoot.getData()) {
            return currentRoot;
        }

        father = currentRoot;

        if (data < currentRoot.getData()) {
            position = false;
            return Search(data, currentRoot.getLeft());
        } else {
            position = true;
            return Search(data, currentRoot.getRight());
        }
    }

    //Punto 6
    public boolean Delete(int data) {
        if (root == null) {
            System.out.print("Árbol vacío");
            return false;
        } else {
            Delete(data, root);
        }
        return true;
    }

    private void Delete(int data, BinaryNode currentRoot) {

        BinaryNode v = Search(data);
        if(v == this.root && v.getRight() == null && v.getLeft() == null){
            this.root = null;
            return;
        }
        if (v.isLeaf()) {
            if (position) {
                father.setRight(null);
            } else {
                father.setLeft(null);
            }
        } else if (v.hasOneChild()) {
            if (v.isChildPosition()) {
                if(root == v){
                    BinaryNode minimum = getMinor(v.getRight());
                    Delete(minimum.getData());
                    v.setData(minimum.getData());
                }else if(position){
                        father.setRight(v.getRight());
                }else{
                    father.setRight(v.getRight());
                }   
            } else {
                if(root == v){
                    root = v.getLeft();
                    v.setLeft(null);
                }else if(position){
                        father.setRight(v.getLeft());
                }else{
                    father.setLeft(v.getLeft());
                }              
            }
        } else {
            BinaryNode minimum = getMinor(v.getRight());
            Delete(minimum.getData());
            v.setData(minimum.getData());
        }
    }

    //Punto 7
    public int LastLevel() {
        LastLevel(root, 0);
        return sizeTree;
    }
    
    public void LastLevel(BinaryNode currentRoot, int contLevel) {
        if(currentRoot != null){
            LastLevel(currentRoot.getLeft(), contLevel+1);
            if(contLevel > sizeTree){
                sizeTree = contLevel;
            }
            LastLevel(currentRoot.getRight(), contLevel + 1);
        }
    }
    
    public BinaryNode getMinor(BinaryNode currentRoot) {
        if (currentRoot.getLeft() == null) {
            return currentRoot;
        } else {
            return getMinor(currentRoot.getLeft());
        }
    }
}
