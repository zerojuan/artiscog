/**
 * Data structure for graph @(#)Vertex.java
 *
 *
 * @author Julius
 * @version 1.00 2008/3/1
 */
import java.util.Vector;
import java.util.Collections;
import java.awt.*;

public class Vertex {
	
	/*this vertex's label*/
	private int i = 0;
	
	/*reference to the main graph*/
	private Graph theGraph = null;
	
	/*true if the vertex is constrained*/
	private boolean isConstrained;
	
	/*the color of this vertex, initialized as gray*/
	private Color color = Color.GRAY;
	private int colorValue = 0;
	/*list of edges*/
	private Vector<Integer> edgeListInt = null;	

	/*graphics manipulation*/
	private int xPos = 0;
	private int yPos = 0;
	private int state = 0;
	private int radius = 10;
	private Rectangle bounds = null;
	
	/*the different "states" the vertex is in*/
	public static int DRAGGING = 1;
	public static int SELECTED = 2;
	public static int EDGING = 3;
	public static int RESTING = 0;
	
	/*for the animation*/
	private int ticks = 0;

	/*i is this vertex's label*/
    public Vertex(int i, Graph theGraph) {
    	this.i = i;
    	this.theGraph = theGraph;
    	edgeListInt = new Vector<Integer>();    	
    }
    
    /*set the color*/
    public void setColor(int color)
    {
    	this.color = Color.decode(ArtIsCoG.COLORS[color]);
    	this.colorValue = color;
    	
    	isConstrained = false;
    	for(int i = 0; i < edgeListInt.size(); i++)
    	{
    		Vertex v = theGraph.getVertexAt(edgeListInt.get(i).intValue());
    		
    		if(v.getColor() == colorValue)
    			isConstrained = true;
    	}
    }
    
    /*returns this vertex's label*/
    public int getLabel()
    {
    	return i;
    }
    
   	/*returns the vertex's color*/
   	public int getColor()
   	{
   		return colorValue;
   	}
    /*returns the array of adjacent vertices*/
    public Vector getEdgeList()
    {
    	return edgeListInt;
    }
    
    public int degree()
    {
    	return edgeListInt.size();
    }
    
    public void addEdge(int edge)
    {
    	if(edge == getLabel()) //do not add edge if we are the same
    		return;
    	
    	if(!edgeListInt.contains(edge))
    	{
    		edgeListInt.add(edge);
    	 	Collections.sort(edgeListInt);
    	}
    }	
    
    public void removeEdge(int edge)
    {
    	edgeListInt.remove(edge);
    }
    /*removes all the edges*/
    public void removeEdges()
    {
    	edgeListInt.clear();
    }
    /*methods for graphics manipulation starts here
     *
     *
     *
     *
     *
     **/
    /*paint the vertex*/ 
    public void paintVertex(Graphics g)
    {
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.setColor(color);
    	g2d.fillOval(xPos, yPos, radius, radius);  
    	if(state == Vertex.RESTING)
    	{
    		g2d.setColor(color);    		
    	}
    	else if(state == Vertex.DRAGGING)
    	{
    		g2d.setColor(new Color(250-ticks*5, 0, 0));
    		g2d.fillOval(xPos+ticks, yPos+ticks, radius-ticks*2, radius-ticks*2);
    				
    	}
    	else if(state == Vertex.SELECTED)
    	{
    		g2d.setColor(new Color(250-ticks*5,250-ticks*5,0));
    		g2d.fillOval(xPos+ticks, yPos+ticks, radius-ticks*2, radius-ticks*2);		
    	}
    	else if(state == Vertex.EDGING)
    	{
    		
    	}
    	g2d.setColor(new Color(250-ticks*5,250-ticks*5,0));
    	g2d.drawString(i+"", xPos,yPos);
    	g2d.drawString(colorValue+"", xPos,yPos+radius);
    
    	  	    	
    	if(isConstrained)
    	{
    		if(ticks%2 == 0)
    			g2d.setColor(Color.RED);
    		else
    			g2d.setColor(Color.BLACK);
    		g2d.setStroke(new BasicStroke(4));
    		g2d.drawOval(xPos,yPos,radius,radius);
    	}
    	else
    	{
    		//g2d.setColor(Color.GREEN);
    		///g2d.setStroke(new BasicStroke(2));
    		//g2d.drawOval(xPos,yPos,radius,radius);
    	}
    	
    	if(ticks>5) ticks = 0;
    	ticks++;
    	
    }
    
    /*paint the edges*/
    public void paintEdges(Graphics g, Graph graph)
    {
    	
    	int cenX = getCenX();
    	int cenY = getCenY();
    	
    	for(int i = 0; i < edgeListInt.size(); i++)
    	{
    		Integer label = edgeListInt.get(i);
    		Vertex v = graph.getVertexAt(label.intValue());
    		
    		int endX = v.getCenX();
    		int endY = v.getCenY();
    		
    		if(state != Vertex.RESTING || v.getState() != Vertex.RESTING)
    			g.setColor(Color.CYAN);
    		else
    			g.setColor(Color.WHITE);
    		g.drawLine(cenX, cenY, endX, endY);
    	}
    }
     
     
    public int getX()
    {
    	return xPos;
    }
    public void setX(int x)
    {
    	xPos = x;
    }
    public int getY()
    {
    	return yPos;
    }
    public void setY(int y)
    {
    	yPos = y;
    }
    public int getCenX()
    {
    	return xPos+radius/2;
    }
    public int getCenY()
    {
    	return yPos+radius/2;
    }
    public void setState(int state)
    {
    	this.state = state;
    }
    public int getState()   
    {
    	return state;
    }
    public int getRadius()
    {
    	return radius;
    }
    public void setRadius(int rad)
    {
    	radius = rad;
    }
    /*change the position of the vertex, dragging*/
    public void drag(int x, int y)
    {
    	xPos = x - radius/2;
    	yPos = y - radius/2;
    }
    public boolean contains(int x, int y)
    {    	
    	bounds = new Rectangle(xPos, yPos, radius, radius);
    
    	return bounds.contains(x,y);    	
    }
}