/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafico;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 *
 * @author Brandon Velasquez,Mariana Palacios,Maria Alejandra Franco
 */
public class TreePresentation extends JPanel {
    BinarySearchTree tree;
    public HashMap postionNodes=null;
    private HashMap subtreeSizes = null;
    private int parentToChild = 20, childToChild = 30;
    private Dimension emptyDimension = new Dimension(0,0);
    private FontMetrics fontMetrics = null;
    
    public TreePresentation(BinarySearchTree tree){
        this.tree=tree;
        postionNodes=new HashMap();
        subtreeSizes=new HashMap();
        repaint();
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        fontMetrics=grphcs.getFontMetrics();
        calcularPosicion();
    }

    private void calcularPosicion() {
        postionNodes.clear();
        subtreeSizes.clear();
        BinaryNode root = tree.getRoot();
        if(root!=null){
            calcularTamañoSubarbol(root);
            calcularPosicion(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        }
    }

    private Dimension calcularTamañoSubarbol(BinaryNode Currentroot) {
        if(Currentroot.equals(null))
            return new Dimension(0, 0);
        Dimension leftDimension=calcularTamañoSubarbol(Currentroot.getLeft());
        Dimension rightDimension=calcularTamañoSubarbol(Currentroot.getRight());
        int height=fontMetrics.getHeight()+parentToChild+Math.max(leftDimension.height, rightDimension.height);
        int width=childToChild+leftDimension.width+rightDimension.width;
        Dimension d= new Dimension(height, width);
        subtreeSizes.put(Currentroot,d);
        return d;
    }

    private void calcularPosicion(BinaryNode n, int left, int right, int top) {
        if(n==null)return;
        Dimension firstDimension=(Dimension) subtreeSizes.get(n.getLeft());
        if(firstDimension==null)
            firstDimension=emptyDimension;
        Dimension secondDimension=(Dimension) subtreeSizes.get(n.getLeft());
        if(secondDimension==null)
            secondDimension=emptyDimension;
        int center = 0;
        if (right != Integer.MAX_VALUE)
          center = right - secondDimension.width - childToChild/2;
      else if (left != Integer.MAX_VALUE)
          center = left + firstDimension.width + childToChild/2;
      int width = fontMetrics.stringWidth(n.getData()+"");
      postionNodes.put(n,new Rectangle(center - width/2 - 3, top, width + 6, fontMetrics.getHeight()));
      calcularPosicion(n.getLeft(), Integer.MAX_VALUE, center - childToChild/2, top + fontMetrics.getHeight() + parentToChild);
      calcularPosicion(n.getRight(), center + childToChild/2, Integer.MAX_VALUE, top + fontMetrics.getHeight() + parentToChild);
    }
    
}
