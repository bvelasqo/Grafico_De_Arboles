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
    public HashMap postionNodes;
    private HashMap subtreeSizes;
    private int parentToChild = 20, childToChild = 30;
    /**Dimension es una clase que es utilizada para guardar el alto y el ancho 
     * tanto de los nodos como de los subarboles
    */
    private Dimension emptyDimension = new Dimension(0,0);
    /**
     * FONT METRICS ES UNA CLASE QUE EN ESTE PROYECTO SE VA A USAR EL OBJETO PARA
     * ENCAPSULAR LA INFORMACIÓN DE LO QUE SE HA GRAFICADO EN EL FRAME
    */
    private FontMetrics fontMetrics = null;
    /**
     * 
     * @param tree 
     */
    public TreePresentation(BinarySearchTree tree){
        this.tree=tree;
        postionNodes=new HashMap();
        subtreeSizes=new HashMap();
        repaint();
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        //getFontMetrics retorna la informacion encapsulada de los
        //graficos hechos en el frame 
        fontMetrics=grphcs.getFontMetrics();
        //calcula las posiciones llenado los hashmap postionNodes y subtreeSizes
        CalculatePositions();
        //cambia la posicion inicial de donde se empieza a pintar en X e Y
        grphcs.translate(getWidth() / 2,parentToChild);
        //dibuja el arbol dando los mismos gráficos, empezando por la raiz,
        //dando dos max value para representar que representan que lo primero en
        //pintar será la raiz, se le da la suma del lider del fontmetrics
        //Osea de donde queda el cursor que escribió una linea(getLeading) 
        //y el ascenso que tiene el fontMetrics; es decir, la altura de el texto
        //dentro de los nodos
         drawTree(grphcs, tree.getRoot(), Integer.MAX_VALUE, Integer.MAX_VALUE, 
                  fontMetrics.getLeading() + fontMetrics.getAscent());
         fontMetrics = null;
    }

    private void CalculatePositions() {
        //Limpia los hash map para empezar a guardar la representación del arbol
        postionNodes.clear();
        subtreeSizes.clear();
        BinaryNode root = tree.getRoot();
        if(root!=null){
            CalculateSizeSubTree(root);
            CalculatePosition(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        }
    }
    
    /**metodo recursivo que calcula la dimension de cada subarbol existente en 
     * el arbol
    */
    private Dimension CalculateSizeSubTree(BinaryNode Currentroot) {
        if(Currentroot==null)
            return new Dimension(0, 0);
        //Calcula todos los subarboles de cada nodo
        Dimension leftDimension=CalculateSizeSubTree(Currentroot.getLeft());
        Dimension rightDimension=CalculateSizeSubTree(Currentroot.getRight());
        /**
         * capturamos el ancho y el alto
         * El alto se calcula sumando el alto del fontMetrics osea de los textos 
         * Hechos más la separacion de padre e hijo denominada, más el mayor del alto de
         * las dimensiones obtenidas de los subarboles
        */
        int height=fontMetrics.getHeight()+parentToChild+Math.max(leftDimension.height, rightDimension.height);
        /**El ancho se calcula sumando la separación de hijo a hijo, más el ancho de
        * las dos dimensiones obtenidas de los subarboles
        */
        int width=childToChild+leftDimension.width+rightDimension.width;
        //Se guarda estpa dimension la cual será la dimensión de todo el subarbol respectivo
        Dimension d= new Dimension(width, height);
        /**
         * se mete esa dimension al hasmap poniendo de key el currenroot diciendo que 
        * ese nodo tiene subarbol de dimension d
        */
        subtreeSizes.put(Currentroot,d);
        return d;
    }

    /**metodo recursivo que se encarga de calcular las posiciones y las dimensiones
    * de cada nodo
    */ 
    private void CalculatePosition(BinaryNode n, int left, int right, int top) {
        if(n==null)return;
        //Se calcula la primera dimension dependiendo de la dimension del subarbol 
        //del nodo izquierdo al nodo actual
        Dimension firstDimension=(Dimension) subtreeSizes.get(n.getLeft());
        if(firstDimension==null)
            firstDimension=emptyDimension;
        //Se calcula la segunda dimension dependiendo de la dimension del subarbol 
        //del nodo derecho al nodo actual
        Dimension secondDimension=(Dimension) subtreeSizes.get(n.getRight());
        if(secondDimension==null)
            secondDimension=emptyDimension;
        int center = 0;
        //Se calcula el centro por izquierda y por derecha  
        //del siguiente nodo para hacer la union(estos valores son negativos 
        //debido a que en los graficos se graficara en la parte negativa).
        if (right != Integer.MAX_VALUE)
          center = right - secondDimension.width - childToChild/2;
      else if (left != Integer.MAX_VALUE)
          center = left + firstDimension.width + childToChild/2;
        //se guarda el ancho del texto escrito dentro del nodo
      int width = fontMetrics.stringWidth(n.getData()+"");
      /**
       * Se agrega esta posicion en el hashmap postionNodes con n como key y
      * un rectangulo con un x calculado en la mitad del nodo basado en el center calculado
      * Un Y que es lo que se ha graficado encontrado gracias al fontMetrics guardado en top
      * Un ancho calculado con el ancho del texto
      * Una altura calculada con el alto de lo escrito
      */
      postionNodes.put(n,new Rectangle(center - width/2 - 3, top, width + 6, fontMetrics.getHeight()));
      /**Se calculan las posiciones de todos los siguientes nodos poniendo center - childToChild/2 en 
      * Donde requiera la union sea en el parametro left o right
      */
      CalculatePosition(n.getLeft(), Integer.MAX_VALUE, center - childToChild/2, top + fontMetrics.getHeight() + parentToChild);
      CalculatePosition(n.getRight(), center + childToChild/2, Integer.MAX_VALUE, top + fontMetrics.getHeight() + parentToChild);
    }
    
    //Metodo que se encarga de representar gráficamente el árbol
    private void drawTree(Graphics graphics, BinaryNode n, int x, int y, int sizeFm){
        if(n==null)return;
        //Se guarde el rectangulo del nodo n de postionNodes
        Rectangle rectangle= (Rectangle) postionNodes.get(n);
        //se grafica ese nodo dependiendo de los valores del rectangulo
        graphics.drawOval(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        //Se grafica el dato del nodo con los valores medidos
        graphics.drawString(n.getData()+"", rectangle.x+3, rectangle.y+sizeFm);
        //Se encarga de pintar una linea de unión si es necesario hacerlo
        //Valores calculados dependiendo del centro del rectangulo y partiendo del
        //x y el y actuales
        if(x!=Integer.MAX_VALUE)
            graphics.drawLine(x, y, (int)rectangle.x+rectangle.width/2, rectangle.y);
        //Se dibujan los otros nodos para completar el arbol guardando la posicion
        //En x y en y del nodo anterior para hacer la union si es necesaria
        drawTree(graphics, n.getLeft(),(int)rectangle.x+rectangle.width/2 , rectangle.y+rectangle.height, sizeFm);
        drawTree(graphics, n.getRight(),(int)rectangle.x+rectangle.width/2 , rectangle.y+rectangle.height, sizeFm);
    }
    
}
