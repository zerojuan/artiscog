/**
 * @(#)Graph.java
 *
 *
 * @author 
 * @version 1.00 2008/3/1
 */
import java.util.Vector;


public class Graph {
	
	/*n is the number of nodes*/
	private int n = 0;
	
	/*max degree of this graph*/
	private int maxDegree = 0;
	
	/*list of the vertices*/
	private Vector<Vertex> vertexList = null;
	
	/*n is the number of nodes*/
    public Graph(int n) {
    	this.n = n;
    	
    	/*initialize*/
    	vertexList = new Vector<Vertex>();
    	
    	/*start making the vertices*/
    	for(int i = 0; i < n; i++)
    	{
    		vertexList.add(i,new Vertex(i,this));
    	}
    	
    	System.out.println("Graph Initialized with size " + n);
    }
    
    public int getSize()
    {
    	return n;
    }
    
    public int maxDegree()
    {
    	return maxDegree;
    }
    
    /*returns the vertex object at the ith position*/
    public Vertex getVertexAt(int i)
    {
    	return vertexList.get(i);
    }
    
    /*getMaxNumber of edges with same degree*/
    public int getSameDegreeMax()
    {
    	int[] degrees = new int[n];
    	/**vertices with the same degrees*/
    	int maxSame = 0;
    	
    	for(int i = 0; i < vertexList.size(); i++)
    	{
    		Vertex v = getVertexAt(i);
    		
    		int degree = v.degree();
    		
    		if(degree >= (maxDegree+2)/2) /** at least delta+2/2*/
    			degrees[degree] += 1;     /*  increment the count for that degree*/		
    			
    			if(degrees[degree] > maxSame) /*if the count on that degree is greater than max*/
    				maxSame = degrees[degree]; /*then it is our new max*/
    	}
    	System.out.print("The array = ");
    		for(int i = 0; i < degrees.length; i++)
    		{
    			System.out.print(degrees[i] + " ");
    		}
    	System.out.println("]]");
    	return maxSame;
    }
    
    
    /*add an edge using vertices*/
    public void AddEdge(Vertex v1, Vertex v2) 
    {
    	v1.addEdge(v2.getLabel());
    	v2.addEdge(v1.getLabel());
    	
    	if(v1.degree() > maxDegree)
			maxDegree = v1.degree();
		else if(v2.degree() > maxDegree)
			maxDegree = v2.degree();
    }

	/*remove an edge using vertices*/
	public void RemoveEdge(Vertex v1, Vertex v2)
	{
		v1.removeEdge(v2.getLabel());
		v2.removeEdge(v1.getLabel());
	}
	
	/*remove an edge using int labels*/
	public void AddEdge(int n1, int n2)
	{
		Vertex v1 = (Vertex)vertexList.get(n1);
		Vertex v2 = (Vertex)vertexList.get(n2);
		
		v1.addEdge(n2);
		v2.addEdge(n1);
		
		if(v1.degree() > maxDegree)
			maxDegree = v1.degree();
		else if(v2.degree() > maxDegree)
			maxDegree = v2.degree();
			
	}
	
	/*remove an edge using int labels*/
	public void RemoveEdge(int n1, int n2)
	{
		Vertex v1 = (Vertex)vertexList.get(n1);
		Vertex v2 = (Vertex)vertexList.get(n2);
		
		v1.removeEdge(n2);
		v2.removeEdge(n1);
	}

	/*return the adjacency list*/
	public int[][] getAdjList()
	{
		int[][] adjList = new int[n][n];
		
		for(int i = 0; i < n; i++)
		{
			Vertex v = getVertexAt(i);
			Vector edges = v.getEdgeList();
			for(int c = 0; c < edges.size(); c++)
			{
				int y = ((Integer)edges.get(c)).intValue();
				adjList[i][y] = 1;
			}
		}
		return adjList;
	}
}