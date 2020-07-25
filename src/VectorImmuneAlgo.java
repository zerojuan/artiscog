/**
 * @(#)VectorImmuneAlgo.java
 *
 *
 * @author 
 * @version 1.00 2008/3/17
 */
import java.util.*;
import java.awt.*;
import java.lang.reflect.*;


public class VectorImmuneAlgo implements Runnable{
	
	private ArtIsCoGUI mainGUI = null;
	
	private int[] adjTriangle;
	private int numVertices;
	private int replacement;
	private Vector<AntiBody> antibodyPool;
	private int populationSize;
	private int selectionSize;
	private int numGens;
	private int chromaticNumber;
	private double clonalFactor;
	private double realisticMaxAffinity;
	private Thread immuneThread;



	public VectorImmuneAlgo(ArtIsCoGUI mainGUI, int[][] adjMatrix, int populationSize, int selectionSize, int numGens, int chromaticNumber, int replacement, double clonalFactor) {
    	this.mainGUI = mainGUI;
    	numVertices=adjMatrix.length;
    	this.populationSize=populationSize;
    	this.selectionSize=selectionSize;
    	this.numGens=numGens;
    	this.chromaticNumber=chromaticNumber;
    	this.clonalFactor=clonalFactor;
    	this.replacement = replacement;
    	realisticMaxAffinity= (float)numVertices/chromaticNumber;
    	
    	System.out.println("RMA: "+realisticMaxAffinity);
    	
    	adjTriangle=convertToArray(adjMatrix);
    	
    	antibodyPool=new Vector<AntiBody>(populationSize);
    	generateRandom(populationSize); //creates the first batch

		immuneThread = new Thread(this);
		immuneThread.start();
    }
    
	public void run(){
		Collections.sort(antibodyPool);
		trim();
		
		for(int g=0; g<numGens; g++){
    		for(int i=0; i<selectionSize; i++){
    			mutate(i);
    	}
    	
    	generateRandom(replacement);
    	Collections.sort(antibodyPool);
    	trim();
    	mainGUI.upDate(antibodyPool.elementAt(0).getSignature(),g, countViolated(antibodyPool.elementAt(0)),
    																	antibodyPool.elementAt(0).getChromatic());
    	try{
        	Thread.sleep(100);
        }catch (InterruptedException evt) {}
         catch (NullPointerException evt) {System.out.println("Null pointer");}
    	}
//    	String best=antibodyPool.elementAt(0).getSignature();
//    	System.out.println("Chromatic Number: "+countColors(best));
//    	System.out.println("Violated: "+countViolated(best));
//    	System.out.println("Affinity: "+antibodyPool.elementAt(0).getAffinity());
//    	System.out.println("================================");
    	mainGUI.doneColoring();
	}
    
    private int[] convertToArray(int[][] adjMatrix){
    	int counter=0;//adjMatrix.length;
    	int[] returned=new int[(numVertices*numVertices-numVertices)/2];
//    	System.out.println((n*n-n)/2);
    	for(int i=0; i<numVertices; i++){
    		for(int i2=0; i2<numVertices; i2++){
    			if(i==i2){
//    				System.out.println();
    				break;    				
    			}
    			returned[counter]=adjMatrix[i][i2];
    			counter++;    			
//    			System.out.print(" "+ i2 +","+i);
    		}
    	}
    	return returned;
    }
    
    private void generateRandom(int number){
    	
//    	System.out.println(numVertices+"#");
    	for(int i=0; i<number; i++){
    	Vector<Integer> temp=new Vector<Integer>();
    	AntiBody tempAB=new AntiBody();
    		for(int i2=0; i2<numVertices; i2++){
    			temp.add((int) (Math.random()*chromaticNumber));
    			
    		}
			
			tempAB.setChromatic(countColors(temp));
			tempAB.setSignature(temp);
    		tempAB.setAffinity(affinity(tempAB));
    		antibodyPool.add(tempAB);
    		
//    		System.out.println("AB: "+tempString+" AF: "+tempAB.getAffinity());

    	}
    	Collections.sort(antibodyPool);
    	
    }    
    
    private int countColors(Vector<Integer> antibody){
    	Vector<Integer> temp= (Vector<Integer>)antibody.clone();
		int counter=0;
//		System.out.println("antibody "+antibody);
		Vector<Integer> newAntibody=(Vector<Integer>) antibody.clone();
		for(int i=0; !temp.isEmpty() ; i++){
			Integer oneColor=temp.elementAt(0);
			//Collection<Integer> c=new Collection<Integer>();
			Vector<Integer> c=new Vector<Integer>();
			c.add(oneColor);
			temp.removeAll(c);
			
			for(int k=0; k<antibody.size(); k++){
				if(antibody.elementAt(k).equals(oneColor)){
					newAntibody.setElementAt(i,k);
//					System.out.println("setElementAt: "+ i+ ", "+k);
				}
			}
			counter++;
			
		}
//		System.out.println("counter: "+counter);
		antibody=(Vector<Integer>)newAntibody.clone();
//		System.out.println("antibody2: "+antibody);
//		System.out.println("=======================");
		
		return counter;
    }
    
    private double affinity(AntiBody AB){
    	double violated= countViolated(AB);
    	double returned= (double)numVertices/(double)(AB.getChromatic()+Math.pow(violated+1,2));
    	
    	return returned;
    }
    
    private int countViolated(AntiBody AB){
    	int n=adjTriangle.length, returned=0;
    	int pos1=1, pos2;
    	
    	Vector<Integer> signature=AB.getSignature();
    	
//    	System.out.println("Antibody: "+antibody);
    	for(int index=0; index<n; ){
    		for(pos2=0; pos2<pos1; pos2++){
    			if(adjTriangle[index]==1){
//    				System.out.print(""+pos2+","+pos1+" ");
    				if(signature.elementAt(pos1).equals(signature.elementAt(pos2))){
    					AB.addViolated(pos1,pos2);
    					returned++;
    				}
    			}
    			index++;
    		}
    		pos1++;
//    		System.out.println();
    		
    	}
//    	System.out.println("violated: "+returned);
//		System.out.println();
    	return returned;
    }
    
    private void trim(){
//		System.out.println("List: "+antibodyPool);
//		for(int i=0; i<5; i++){
//			antibodyPool.add(new AntiBody());
//		}
//		System.out.println("NextList: "+antibodyPool);
  	 	try{
			Class klass = antibodyPool.getClass();
			Class[] paramTypes = { Integer.TYPE, Integer.TYPE };
			Method m = klass.getDeclaredMethod("removeRange", paramTypes);
      		Object[] arguments = { new Integer(populationSize), new Integer(antibodyPool.size()) };
      		m.setAccessible(true);
      		m.invoke(antibodyPool, arguments);
//      		System.out.println("The new list is: " + antibodyPool);
      	}catch(Exception e){
      		System.out.println(e);
	    }
    }
    
    private void mutate(int i){
    
    	AntiBody atLarge=antibodyPool.elementAt(i);
    	double mutationFactor=Math.abs(realisticMaxAffinity/atLarge.getAffinity());
    	int numChromosomes=1+ (int) (numVertices*mutationFactor)/5; //is maybe have less than overhead of Math.ceil()
    	int numClones= (int) (0.5+(clonalFactor*populationSize)/(i+1));    
    	int localChromatic= (int)(Math.random()*chromaticNumber);//atLarge.getChromatic()+1);
    	
    	Vector<Integer> gene=atLarge.getSignature();
//    	System.out.println("Gene: " + gene);
//    	System.out.println("Local k: " + localChromatic);
    	for(int counter=0; counter<numClones; counter++){
    		AntiBody AB=new AntiBody();	
    		Vector<Integer> mutant=(Vector<Integer>)gene.clone();
    		for(int xmCounter=0; xmCounter<numChromosomes; xmCounter++){
    			int mutationPoint=(int)(Math.random()*gene.size()) ;
    			
    			Vector<Integer> violated=atLarge.getViolated();
    			if(violated.size()>0)
    				mutationPoint= violated.elementAt( (int)(Math.random()*violated.size()) );
    			
    			int mutation= (int)(Math.random()*localChromatic);
    		//	int mutation= (int)(Math.random()*chromaticNumber);
    			mutant.setElementAt(mutation,mutationPoint);
    		}
    	
//    	System.out.println("Mutant: " + mutant);
    		AB.setChromatic(countColors(mutant));
			AB.setSignature(mutant);
    		AB.setAffinity(affinity(AB));
    		antibodyPool.add(AB);
    	}
    	
    }
    
    
}