/**
 * Displays the graphical representation of the graph @(#)GraphPanel.java
 *
 *
 * @author Julius
 * @version 1.00 2008/2/28
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
	
	/*reference to the graph*/
	private Graph graph = null;
	/*responsible for the animation*/
	private Timer timer = null;
	/*for tracking the curr active vertex*/
	private Vertex currVertex = null;
	/*for creating the temporary line for edges*/
	private int xStart = 0,
				yStart = 0,
				xEnd = 0,
				yEnd = 0;
	
	private String STATUS = "";
	
    public GraphPanel() {
		graph = new Graph(0);
		addMouseListener(this);
		addMouseMotionListener(this);
		
    	initGraph(graph);
    	
    	timer = new Timer(100,this);
    	timer.start();
    	timer.addActionListener(this);    	
    	setOpaque(false);
    }
    
    public void initGraph(Graph theGraph)
    {
    	this.graph = theGraph;
    	
    	int n = graph.getSize();
    	
    	/*divide the circle into n parts*/
    	double increment = (Math.PI*2)/n;
    	/*theta is the angle*/
    	double theta = 0;
    	/*radius, distance from the center, 2nd term for increment by 50,
    	 *3rd term for resetting if it gets too large
    	 **/
    	int radius = 250;
    	/*radius of the vertices, decreases every 150*/
    	int vertexRadius = 20 - 3*(n/150);
    	for(int i = 0; i < n; i++)
    	{
    		
    		theta = increment*i;
    		
    		int xtranslate = (int)(Math.cos(theta)*(radius-50*(i%2)));
    		int ytranslate = (int)(Math.sin(theta)*(radius-50*(i%2)));
    		int x = (this.getWidth()/2) + xtranslate;
    	 	int y = (this.getHeight()/2)+ ytranslate;	    		
    		Vertex v = graph.getVertexAt(i);
    		v.setX(x);
    		v.setY(y);
    		v.setRadius(vertexRadius);
    	}
    	repaint();
    }
    
    public void paintComponent(Graphics graphics)    
    {
    	//super.paintComponent(graphics);
    	Graphics g = graphics;
       	
       	/*background*/
       	g.setColor(Color.black);        	
       	g.fillRect(1,1,this.getWidth(),this.getHeight());
		       	
       	g.setColor(Color.white);
       	g.drawString(STATUS, 10, 10);       	 	
        
        /*draw a temp edge if a current vertex is selected*/      
        if(currVertex!=null)
        	if(currVertex.getState()==Vertex.SELECTED)
        	{
        		g.drawLine(xStart,yStart,xEnd,yEnd);
        	}
        	else
        	{
        		xStart = 0;
        		yStart = 0;
        		xEnd = 0;
        		yEnd = 0;
        	}
        /*draw the edges*/
        for(int i = 0; i < graph.getSize(); i++)
        {
        	Vertex v = graph.getVertexAt(i);
        	v.paintEdges(g,graph);        		
        }
        
        
        /*draw the vertices*/
        for(int i = 0; i < graph.getSize(); i++)
        {
        	Vertex v = graph.getVertexAt(i);
        	v.paintVertex(g);        		
        }        
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	repaint();
    }
    
    public void mousePressed(MouseEvent e) {
    	 int n = graph.getSize();    	    	 	 
    	 if(currVertex == null) //if no selected, start drawing that "edge" temp
    	 {
    	 	for(int i = 0; i < n; i++)     
    	 	{
    	 		Vertex v = graph.getVertexAt(i);    	 	    	 	    	 	
    	 		if(v.contains(e.getX(),e.getY()))
    	 		{
	    	 		v.setState(Vertex.SELECTED);	    	 		
    	 			currVertex = v;     	 		
    	 		}
    	 		else
    	 		{
    	 			v.setState(Vertex.RESTING);
    	 		}
    	 	}    	 	
    	 }
    	 if(currVertex!=null) //else edd edge if a currvertex is selected
    	 	if(currVertex.getState()==Vertex.SELECTED)
    	 	{
    	 		boolean pressedEmpty = true;
    	 		for(int i = 0; i < n; i++)     
    			 {
    		 	Vertex v = graph.getVertexAt(i);    	 	    	 	    	 	
	    	 	if(v.contains(e.getX(),e.getY()))
    	 		{
	    	 		v.setState(Vertex.SELECTED);
	    	 		graph.AddEdge(currVertex, v);
	    	 		pressedEmpty = false;
    	 			currVertex = v;    	 		
    	 		}
    	 		else
    	 		{
	    	 		v.setState(Vertex.RESTING);
    		 	}
    	 		}
    	 		if(pressedEmpty)
    	 		{
    	 			currVertex = null; //the user pressed an empty space	
    	 			xStart = 0;
					yStart = 0;
					xEnd = 0;
					yEnd = 0;    	 			
    	 		}
    	 	}
    	 	else
    	 	{
    	 		currVertex = null;
    	 		
    	 	}
    	 repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {     
    }

    public void mouseClicked(MouseEvent e) {       
    }
	public void mouseMoved(MouseEvent e) {
		STATUS = "("+e.getX()+","+e.getY()+") translated: (" + (e.getX()-(this.getWidth()/2)) +
					", " + ((this.getHeight()/2)-e.getY()) + ")";
		if(currVertex!=null)
		{
			STATUS += "Vertex " + currVertex.getLabel();
			if(currVertex.getState()==Vertex.DRAGGING)
			{
				currVertex.setState(Vertex.RESTING);
				currVertex = null;
			}
			else if(currVertex.getState()==Vertex.SELECTED)
			{
				xStart = currVertex.getCenX();
				yStart = currVertex.getCenY();
				xEnd = e.getX();
				yEnd = e.getY();
			}
		}		
		
		repaint();     
    }

    public void mouseDragged(MouseEvent e) {
    	int n = graph.getSize();    	    	 	 
    	 for(int i = 0; i < n; i++)     
    	 {
    	 	Vertex v = graph.getVertexAt(i);    	 	    	 	    	 	
    	 	if(v.contains(e.getX(),e.getY()))
    	 	{
    	 		currVertex = v;
    	 		v.setState(Vertex.DRAGGING);    	 		
    	 	}
    	 }
    	 
    	 if(currVertex!=null)
    	 if(currVertex.getState()==Vertex.DRAGGING)
    	 		currVertex.drag(e.getX(),e.getY());
    	 	
    	 repaint();       
    }
}