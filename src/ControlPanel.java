/**
 * menu for tweaking the algorithm and the graph @(#)ControlPanel.java
 *
 *
 * @author Julius
 * @version 1.00 2008/2/28
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

public class ControlPanel extends JPanel implements ActionListener, ChangeListener{
	/*reference to the main*/
	private ArtIsCoGUI mainGUI = null;
	
	/*number of vertices*/
	int verticesN = 1;
	/*Antibody population*/
	int antibodyN = 1;
	/*Selection pool size*/
	int selectionPoolN = 1;
	/*Remainder replacement*/
	int remainderN = 0;
	/*Clonal factor*/
	int clonalFactor = 50;
	/*Generations*/
	int generationsN = 100;
	/*chromatik according to brooks*/
	int brooks = 0;
	/*max degree*/
	int maxDegree = 0;
	/*chromatik number of antibody*/
	int antibodyK = 0;
	/*constrained vertices of the antibody*/
	int constrained = 0;

	/*labels*/
	private JLabel verticesL = null;
	private JLabel antibodyL = null;
	private JLabel selectionPoolL = null;
	private JLabel remainderL = null;
	private JLabel clonalFactorL = null;
	private JLabel generationsL = null;
	private JLabel brooksL = null;
	private JLabel maxDegreeL = null;
	private JLabel antibodyKL = null;
	private JLabel constrainedL = null;
	
	/*sliders*/
	private JSlider verticesS = null;
	private JSlider antibodyS = null;
	private JSlider selectionPoolS = null;
	private JSlider remainderS = null;
	private JSlider clonalFactorS= null;
	private JSlider generationsS = null;

	/*buttons*/
	private JButton colorify = null;
	private JButton reset = null;
	private JButton randomize = null;
	
	/*progress bar*/
	private JProgressBar progressBar = null;
	
	/*panels*/
	private JPanel slidersPane = null;
	private JPanel buttonsPane = null;
	private JPanel statusPane = null;
	
    public ControlPanel(ArtIsCoGUI mainGUI) {
    	/*reference to the main*/
    	this.mainGUI = mainGUI;
    	
    	//setPreferredSize(new Dimension(250,100));
    	//setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    	setLayout(new BorderLayout());
    	
    	
    	verticesL = new JLabel("Vertex: " + verticesN);
    	antibodyL = new JLabel("Antibody Population: " + antibodyN);
    	selectionPoolL = new JLabel("Selection Pool: " + selectionPoolN);
    	remainderL = new JLabel("Remainder Replacement Size: " + remainderN);
    	double clonalFactorDouble = (double)clonalFactor/100;
    	clonalFactorL = new JLabel("Clonal Factor: " + clonalFactorDouble);
    	generationsL = new JLabel("Generations: " + generationsN);
    	brooksL = new JLabel("Chromatic Upper Bound (Brooks): ??");
    	maxDegreeL = new JLabel("Max Degree: ??");
    	antibodyKL = new JLabel("Chromatic Number: ??");
    	constrainedL = new JLabel("Constrained Edges: ??");
    	
    	verticesS = new JSlider(JSlider.HORIZONTAL, 1, 100, verticesN);
    	antibodyS = new JSlider(JSlider.HORIZONTAL, 1, 100, antibodyN);
    	selectionPoolS = new JSlider(JSlider.HORIZONTAL, 1, 100, selectionPoolN);
    	remainderS = new JSlider(JSlider.HORIZONTAL, 0, 100, remainderN);
    	clonalFactorS = new JSlider(JSlider.HORIZONTAL, 1, 100, clonalFactor);
    	generationsS = new JSlider(JSlider.HORIZONTAL, 1, 5000, generationsN);
    	
    	
    	
    	colorify = new JButton("COLORIFY");
    	reset = new JButton("RESET");
    	randomize = new JButton("RANDOMIZE");
    	
    	setUpUI();
    	
    	setUpActions();
    }
    
    /*setup the UI*/
   private void setUpUI()
    {
    	slidersPane = new JPanel();
    	slidersPane.setLayout(new BoxLayout(slidersPane, BoxLayout.PAGE_AXIS));
    	buttonsPane = new JPanel();
    	buttonsPane.setLayout(new BoxLayout(buttonsPane, BoxLayout.LINE_AXIS));
    	JPanel upperPane = new JPanel();
    	upperPane.setLayout(new BoxLayout(upperPane, BoxLayout.PAGE_AXIS));
    	upperPane.setBorder(BorderFactory.createRaisedBevelBorder());
    	statusPane = new JPanel();
    	statusPane.setLayout(new BorderLayout());
    	
    	JPanel graphPropertiesPane = new JPanel();
    	graphPropertiesPane.setLayout(new BoxLayout(graphPropertiesPane, BoxLayout.PAGE_AXIS));
    	graphPropertiesPane.setBorder(BorderFactory.createTitledBorder("Graph Properties"));
    	JPanel antibodyPropertiesPane = new JPanel();
    	antibodyPropertiesPane.setLayout(new BoxLayout(antibodyPropertiesPane, BoxLayout.PAGE_AXIS));
    	antibodyPropertiesPane.setBorder(BorderFactory.createTitledBorder("Antibody Properties"));
    	
    	
    	graphPropertiesPane.add(brooksL);
    	graphPropertiesPane.add(maxDegreeL);
    	
    	
    	antibodyPropertiesPane.add(antibodyKL);
    	antibodyPropertiesPane.add(constrainedL);
    	
    	JPanel propsCombined = new JPanel(new GridLayout(0,1));
    	
    	propsCombined.add(graphPropertiesPane);
    	propsCombined.add(antibodyPropertiesPane);
    	slidersPane.add(verticesL);
    	slidersPane.add(verticesS);
    	slidersPane.add(Box.createRigidArea(new Dimension(0,5)));
    	slidersPane.add(antibodyL);
    	slidersPane.add(antibodyS);
    	slidersPane.add(Box.createRigidArea(new Dimension(0,5)));
    	slidersPane.add(selectionPoolL);
    	slidersPane.add(selectionPoolS);
    	slidersPane.add(Box.createRigidArea(new Dimension(0,5)));
    	slidersPane.add(remainderL);
    	slidersPane.add(remainderS);
    	slidersPane.add(Box.createRigidArea(new Dimension(0,5)));
    	slidersPane.add(clonalFactorL);
    	slidersPane.add(clonalFactorS);
    	slidersPane.add(Box.createRigidArea(new Dimension(0,5)));
    	slidersPane.add(generationsL);
    	slidersPane.add(generationsS);
    	slidersPane.add(Box.createRigidArea(new Dimension(0,5)));
    	
    	buttonsPane.add(colorify);
    	buttonsPane.add(Box.createHorizontalGlue());
    	buttonsPane.add(reset);
    	buttonsPane.add(Box.createHorizontalGlue());
    	buttonsPane.add(randomize);
    	
    	
    	
    	upperPane.add(slidersPane);
    	upperPane.add(buttonsPane);
    	upperPane.add(Box.createRigidArea(new Dimension(0,5)));
    //	statusPane.add(graphPropertiesPane, BorderLayout.NORTH);
    //	statusPane.add(antibodyPropertiesPane, BorderLayout.CENTER);
    	statusPane.add(propsCombined, BorderLayout.CENTER);
    	//this.add(upperPane);
    //	this.add(buttonsPane);
    	this.add(upperPane, BorderLayout.PAGE_START);
    }
    
    
    /*setup the actions*/
    private void setUpActions()
    {
    	verticesS.addChangeListener(this);
    	antibodyS.addChangeListener(this);
    	selectionPoolS.addChangeListener(this);
    	remainderS.addChangeListener(this);
    	clonalFactorS.addChangeListener(this);
    	generationsS.addChangeListener(this);
    	
    	colorify.addActionListener(this);    	
    	reset.addActionListener(this);    	
    	randomize.addActionListener(this);    	
    }
    
    public void setGraphProperties(int brooks, int maxDegree)
    {
    	this.brooks = brooks;
    	this.maxDegree = maxDegree;
    	
    	brooksL.setText("Chromatic Upper Bound (Brooks): " + brooks);
    	maxDegreeL.setText("Max Degree: " + maxDegree);
    }
    
    public void setAntibodyProperties(int constrained, int k)
    {
    	antibodyK = k;
    	this.constrained = constrained;
    	
    	antibodyKL.setText("Chromatic Number: " + k);
    	constrainedL.setText("Constrained Edges: " + constrained);
    }
    /*sets up the progress*/
    public void setProgress(int n)
    {
    	try{
    		progressBar.setValue(n);
    		progressBar.setString("Colorifying: " + (int)(progressBar.getPercentComplete()*100)+ "%");
    	}
    	catch(NullPointerException e)
    	{
    	};
    }
    
    /*done coloring*/
    public void doneColoring()
    {
    	Toolkit.getDefaultToolkit().beep();
		progressBar.setValue(progressBar.getMaximum());
		progressBar.setString("Colorifying: DONE!!");
		colorify.setEnabled(true);

    }
    
    /*listen to the sliders*/
    public void stateChanged(ChangeEvent e)
    {
    	JSlider source = (JSlider)e.getSource();
    	/*handle only if the user has finished adjusting*/
    	    	
    		if(source.equals(verticesS))
    		{
	    		verticesN = (int)source.getValue();
    			verticesL.setText("Vertex: " + verticesN);
    			if(!source.getValueIsAdjusting())
    			{
    				System.out.println("Creating " + verticesN + " vertices...");    			
    				mainGUI.createGraph(verticesN);    			
    			}
    		}
    		else if(source.equals(antibodyS))
    		{
	    		antibodyN = (int)source.getValue();
    			antibodyL.setText("Antibody Population: " + antibodyN);
    		}
    		else if(source.equals(selectionPoolS))
    		{
	    		selectionPoolN = (int)source.getValue();
    			selectionPoolL.setText("Selection Pool: " + selectionPoolN);
    		}
    		else if(source.equals(remainderS))
    		{
	    		remainderN = (int)source.getValue();
    			remainderL.setText("Remainder Replacement Size: " + remainderN);
    		}
    		else if(source.equals(clonalFactorS))
    		{
	    		clonalFactor = (int)source.getValue();
    			double clonalFactorDouble = (double)clonalFactor/100;
    			clonalFactorL.setText("Clonal Factor: " + clonalFactorDouble);
    		}
    		else if(source.equals(generationsS))
    		{
	    		generationsN = (int)source.getValue();
    			generationsL.setText("Generations: " + generationsN);
    		}
    	
    }
    
    
    /*listen to the buttons*/
    public void actionPerformed(ActionEvent e)
    {
    	JButton source = (JButton)e.getSource();
    	if(source.equals(colorify))
    	{
    		double clonalFactorDouble = (double)clonalFactor/100;
    		mainGUI.colorify(antibodyN, selectionPoolN, remainderN, clonalFactorDouble, generationsN);
    		
    		if(progressBar!=null)    		
    			statusPane.remove(progressBar);
    		progressBar = new JProgressBar(0, generationsN);
    		progressBar.setValue(0);
    		progressBar.setStringPainted(true);
    		progressBar.setString("Colorifying:");
    		statusPane.add(progressBar,BorderLayout.SOUTH);    
    		this.add(statusPane,BorderLayout.CENTER);
    		this.revalidate();
    		
    		colorify.setEnabled(false);	
    	}
    	else if(source.equals(reset))
    	{
    		colorify.setEnabled(true);
    	}
    	else if(source.equals(randomize))
    	{
    		mainGUI.randomizeGraph();
    	}
    }
   
}