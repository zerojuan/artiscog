/**
 * The ArtisCog GUI @(#)ArtIsCoGUI.java
 *
 *
 * @author Julius
 * @version 1.00 2008/2/28
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;



public class ArtIsCoGUI extends JFrame{
	 /** relative path for files */
    public static String RP = "./";
    /*instance for graph panel*/
    private GraphPanel graphPanel = null;
    /*instance of the control panel*/
    private ControlPanel controlPanel = null;
    /*reference to the graph*/
    private Graph theGraph = null;
    
    private JMenuBar menuBar = null;
    private JMenu menu = null;
    private JMenuItem newMenu = null;
    private JMenuItem saveMenu = null;
    private JMenuItem closeMenu= null;
    
    private int[] constantGraph={
36,99,97,41,80,75,16,71,10,3,11,85,72,47,25,73,31,89,38,98,72,18,76,32,81,0,2,55,84,82,8,58,66,68,64,45,54,35,76,56,99,41,76,11,72,70,67,58,23,46,14,70,19,54,61,67,0,99,71,25,7,30,33,98,18,66,46,42,57,45,6,38,64,41,9,27,99,72,72,98,9,30,43,61,56,55,81,93,48,26,17,79,26,96,86,18,13,78,63,47
    };

    public ArtIsCoGUI() 
    {
    	//Set Look and Feel
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (Exception e) {}
    	
    	/** add window event handler */
        addWindowListener(new WindowAdapter() {
            /**
             * Handles window closing events.
             * @param evt window event
             */
            public void windowClosing(WindowEvent evt) {
                /** terminate the program */
                System.exit(0);
            }
        });
        
        /** set the window title */
        setTitle("ArtIsCoG");
        
        /** set the window icon */
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(
                new File(RP + "Images/ico2.jpg").getCanonicalPath()));
        }
        catch (Exception e) { //new ErrorDialog(e);
        						System.out.println("A very big error");}
    	
    	
    	
    	
    	/** get screen size */
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		setSize(d.width-100, d.height-100);
		
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        
        /*setup the UI*/
        setUI();
        
        /** center the window on the screen */
        setLocation(x, y);
        
    	setVisible(true);
    	
    	/*put the window on top of everything*/
    	toFront();
    }
    
    /*setup the UI*/
    private void setUI()
    {
    	menuBar = new JMenuBar();
    	
    	menu = new JMenu("File");
    	newMenu = new JMenuItem("New");
    	saveMenu = new JMenuItem("Save");
    	closeMenu= new JMenuItem("Close");
    	menu.add(newMenu);
    	menu.add(saveMenu);
    	menu.add(closeMenu);
    	
    	menuBar.add(menu);
    	
    	this.setJMenuBar(menuBar);
    	
    	Container container = this.getContentPane();
    	container.setLayout(new BorderLayout(5,5));
    	
    	/*setUp the graph*/
    	graphPanel = new GraphPanel();
    	
    	
    	JScrollPane scroll = new JScrollPane(graphPanel);
    	
    	container.add(scroll, BorderLayout.CENTER);
    	
    	/*setUp the Controls*/
    	controlPanel = new ControlPanel(this);
    	container.add(controlPanel, BorderLayout.LINE_END);
    	
    }
    
    /*get control panel reference*/
    public ControlPanel getControlPanel()
    {
    	return controlPanel;
    }
    
    /*get graph panel reference*/
    public GraphPanel getGraphPanel()
    {
    	return graphPanel;
    }
    
    /*create the graph with n vertices*/
    public void createGraph(int vertices)
    {
    	theGraph = new Graph(vertices);
    	
    	graphPanel.initGraph(theGraph);
    }
    /*reset the coloring of the graph*/
    public void resetGraph()
    {
    }
    
    /*reset edges of the whole graph*/
    public void resetGraphEdges()
    {
    	for(int i = 0; i < theGraph.getSize();i++)
    	{
    		Vertex v = theGraph.getVertexAt(i);
    		
    		v.removeEdges();
    	}
    }
    /*randomize the edges*/
    public void randomizeGraph()
    {
    	resetGraphEdges();
    	for(int i = 0; i < theGraph.getSize(); i++)
    	{
    		Vertex v = theGraph.getVertexAt(i);
    		Random rand = new Random();
    		//choose a random number 0 to N-1, to check how many times it try to pick a random edge
    		int randNumEdge = rand.nextInt(theGraph.getSize());
//    		randNumEdge=10;
    		for(;randNumEdge>=0;randNumEdge--)
    		{
    			//pick a random number 0 to N that this vertex will connect to
    			int randEdge = rand.nextInt(theGraph.getSize());
//    			randEdge=constantGraph[randNumEdge];
    			theGraph.AddEdge(v.getLabel(),randEdge);
    		}
    	}
    	System.out.println("Max degree = " + theGraph.maxDegree());
    }
    
    /*This is the part where the algorithm is implemented
     *
     *
     **/
     public void colorify(int antibodyN, int selectionPoolN, int remainderN, double clonalFactor, int generationsN)
     {
     	int maxDegree = theGraph.maxDegree(); //maximum degree of G
     	int maxSame = theGraph.getSameDegreeMax();
     	
     	System.out.println("Max degree = " + maxDegree);
     	System.out.println("Max same = " + maxSame);
     	
     	//brooks theorem
     	int k = ((maxSame*(maxDegree+2)/(maxSame+1)));
     	
     	System.out.println("Chromatic Number: " + k);
     	
     	/*convert to adjacency list*/
     	int[][] adjList = theGraph.getAdjList();
     	
//     	for(int i = 0; i < theGraph.getSize(); i++)
//     	{System.out.print("[");
//     		for(int c = 0; c < theGraph.getSize();c++)
//     		{
//     			System.out.print(" " + adjList[i][c]);
//     		}
//     		System.out.println("]");
//     	}
     	controlPanel.setGraphProperties(k,maxDegree);
     	VectorImmuneAlgo clonalG = new VectorImmuneAlgo(this, adjList, antibodyN, selectionPoolN, generationsN, k, remainderN, clonalFactor);
     }
     
     public void upDate(Vector<Integer> antibody, int generationsN, int constrained, int k)
     {
     /*	String[] s = antibody.split(",");
     	for(int i = 0; i < theGraph.getSize(); i++)
     	{
     		int color = Integer.parseInt(s[i]);
     		Vertex v = theGraph.getVertexAt(i);
     		v.setColor(color);
     		     		
     	}
     	graphPanel.repaint();
     	controlPanel.setProgress(generationsN);
     	controlPanel.setAntibodyProperties(constrained, k);
     	*/
     	for(int i = 0; i < theGraph.getSize(); i++)
     	{
     		int color = (antibody.get(i)).intValue();
     		Vertex v = theGraph.getVertexAt(i);
     		v.setColor(color);
     		     		
     	}
     	graphPanel.repaint();
     	controlPanel.setProgress(generationsN);
     	controlPanel.setAntibodyProperties(constrained, k);
     }
     /*the task is finished, coloring is done*/
     public void doneColoring()
     {
     	controlPanel.doneColoring();
     }
}