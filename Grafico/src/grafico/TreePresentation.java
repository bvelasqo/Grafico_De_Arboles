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
        CalculatePositions();
        grphcs.translate(getWidth() / 2, parentToChild);
         drawTree(grphcs, tree.getRoot(), Integer.MAX_VALUE, Integer.MAX_VALUE, 
                  fontMetrics.getLeading() + fontMetrics.getAscent());
         fontMetrics = null;
    }

    private void CalculatePositions() {
        postionNodes.clear();
        subtreeSizes.clear();
        BinaryNode root = tree.getRoot();
        if(root!=null){
            CalculateSizeSubTree(root);
            CalculatePosition(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        }
    }

    private Dimension CalculateSizeSubTree(BinaryNode Currentroot) {
        if(Currentroot==null)
            return new Dimension(0, 0);
        Dimension leftDimension=CalculateSizeSubTree(Currentroot.getLeft());
        Dimension rightDimension=CalculateSizeSubTree(Currentroot.getRight());
        int height=fontMetrics.getHeight()+parentToChild+Math.max(leftDimension.height, rightDimension.height);
        int width=childToChild+leftDimension.width+rightDimension.width;
        Dimension d= new Dimension(width, height);
        subtreeSizes.put(Currentroot,d);
        return d;
    }

    private void CalculatePosition(BinaryNode n, int left, int right, int top) {
        if(n==null)return;
        Dimension firstDimension=(Dimension) subtreeSizes.get(n.getLeft());
        if(firstDimension==null)
            firstDimension=emptyDimension;
        Dimension secondDimension=(Dimension) subtreeSizes.get(n.getRight());
        if(secondDimension==null)
            secondDimension=emptyDimension;
        int center = 0;
        if (right != Integer.MAX_VALUE)
          center = right - secondDimension.width - childToChild/2;
      else if (left != Integer.MAX_VALUE)
          center = left + firstDimension.width + childToChild/2;
      int width = fontMetrics.stringWidth(n.getData()+"");
      postionNodes.put(n,new Rectangle(center - width/2 - 3, top, width + 6, fontMetrics.getHeight()));
      CalculatePosition(n.getLeft(), Integer.MAX_VALUE, center - childToChild/2, top + fontMetrics.getHeight() + parentToChild);
      CalculatePosition(n.getRight(), center + childToChild/2, Integer.MAX_VALUE, top + fontMetrics.getHeight() + parentToChild);
    }
    
    private void drawTree(Graphics graphics, BinaryNode n, int x, int y, int sizeFm){
        if(n==null)return;
        Rectangle rectangle= (Rectangle) postionNodes.get(n);
        graphics.drawOval(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        graphics.drawString(n.getData()+"", rectangle.x+3, rectangle.y+sizeFm);
        if(x!=Integer.MAX_VALUE)
            graphics.drawLine(x, y, (int)rectangle.x+rectangle.width/2, rectangle.y);
        drawTree(graphics, n.getLeft(),(int)rectangle.x+rectangle.width/2 , rectangle.y+rectangle.height, sizeFm);
        drawTree(graphics, n.getRight(),(int)rectangle.x+rectangle.width/2 , rectangle.y+rectangle.height, sizeFm);
    }
    
}
