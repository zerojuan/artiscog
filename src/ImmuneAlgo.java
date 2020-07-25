/**
 * @(#)ImmuneAlgo.java
 *
 *
 * @author 
 * @version 1.00 2008/3/10
 */
import java.util.*;
import java.awt.*;
import java.lang.reflect.*;

public class ImmuneAlgo implements Runnable{
	
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
	
	
    public ImmuneAlgo(ArtIsCoGUI mainGUI, int[][] adjMatrix, int populationSize, int selectionSize, int numGens, int chromaticNumber, int replacement, double clonalFactor) {
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
/*    	
    	for(int g=0; g<numGens; g++){
    		for(int i=0; i<selectionSize; i++){
    			mutate(i);
    		}
    		generateRandom(replacement);
    		Collections.sort(antibodyPool);
    		System.out.println("Best Now: "+antibodyPool.elementAt(0).getSignature());
    		mainGUI.generateColor(antibodyPool.elementAt(0).getSignature());
    	}*/
    	
//    	Collections.sort(antibodyPool);
//    	printPool();
//    	trim();

		immuneThread = new Thread(this);
		immuneThread.start();
    }
    
    public void run()
    {	Collections.sort(antibodyPool);
    	trim();
    	for(int g=0; g<numGens; g++){
    		for(int i=0; i<selectionSize; i++){
    			mutate(i);
    		}
    		generateRandom(replacement);
    		Collections.sort(antibodyPool);
    		trim();
    		
			String best=antibodyPool.elementAt(0).getSignature();
//    		System.out.println("Best Now: "+antibodyPool.elementAt(0).getSignature());
//			System.out.println("Violated: "+countViolated(best));
//			System.out.println("Colors used: "+antibodyPool.elementAt(0).getChromatic());
//			System.out.println("========"+g+"========");
			
    		mainGUI.upDate(antibodyPool.elementAt(0).getSignature(),g, countViolated(antibodyPool.elementAt(0).getSignature()),
    																	countColors(antibodyPool.elementAt(0).getSignature()));
    		try {
                  Thread.sleep(100);
               }
            catch (InterruptedException evt) {}
            catch (NullPointerException evt) {System.out.println("Null pointer");}
    	}
    	String best=antibodyPool.elementAt(0).getSignature();
    	System.out.println("Chromatic Number: "+countColors(best));
    	System.out.println("Violated: "+countViolated(best));
    	System.out.println("Affinity: "+antibodyPool.elementAt(0).getAffinity());
    	System.out.println("================================");
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
    
    private int countColors(String antibody){
//    	String wut="RGBRGBYRYYYYYSGS";
//		String antibody=rand;//.replaceAll(",",",,");
//		String[] colors=antibody.split(",");
//		System.out.println
    	int counter=0;
    	for(counter=0;!antibody.replaceAll(",","").isEmpty();){
    		int comma=antibody.indexOf(",")+1;
    		String oneColor=antibody.substring(0,comma);
    		antibody=antibody.replaceAll(","+oneColor,",");
    		antibody=antibody.substring(comma);
//    		System.out.println("oneColor: "+oneColor);
    		counter++;
    	}
//    	System.out.println(counter);
    	
    	return counter;
    }
    
    
    private int countViolated(String antibody){
//    	System.out.println("================");
//		System.out.println("Antibody: "+antibody+" ");
		String[] chromosomes=antibody.split(",");
    	int n=adjTriangle.length, returned=0;
    	int pos1=1, pos2;
//    	System.out.println("Antibody: "+antibody);
    	for(int index=0; index<n; ){
    		for(pos2=0; pos2<pos1; pos2++){
    			if(adjTriangle[index]==1){
//    				System.out.print(""+pos2+","+pos1+" ");
    				if(chromosomes[pos1].equals(chromosomes[pos2])){
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
    
    private double affinity(AntiBody AB){
//    	System.out.print("n:"+numVertices+" v:"+countViolated(antibody)+" c:"+countColors(antibody));
//    	double returned=-((double)AB.getChromatic()/((double)numVertices-(double)countViolated(AB.getSignature())))-(double)countViolated(AB.getSignature())/(double)AB.getChromatic();
		double violated=countViolated(AB.getSignature());
		double returned= (double)numVertices/(double)(countColors(AB.getSignature())+Math.pow(violated+1,2));

    	return returned;
    }
    
//    private void printPool(){
//    	System.out.println();
//    	for(int i=0; i<antibodyPool.size(); i++){
//    		AntiBody temp= antibodyPool.elementAt(i);
//    		double mutationFactor= realisticMaxAffinity/temp.getAffinity();
//    	//	System.out.println( mutationFactor+" : "+temp.getSignature() +" : "+ temp.getAffinity() );
//    	}
//    }
    
    private void mutate(int i){
    	AntiBody atLarge=antibodyPool.elementAt(i);
    	double mutationFactor=Math.abs(realisticMaxAffinity/atLarge.getAffinity());
    	int numChromosomes=1+ (int) (numVertices*mutationFactor)/5; //is maybe have less than overhead of Math.ceil()
    	int numClones= (int) (0.5+(clonalFactor*populationSize)/(i+1));
    	String gene=atLarge.getSignature();
//    	System.out.println("Mutation Factor "+mutationFactor+" Affinity: "+atLarge.getAffinity() );
    //	System.out.println("gene: "+gene);
    	
    //	System.out.println("rank: "+i+" populationSize: "+populationSize+" clonal factor: "+clonalFactor+" numXms: "+numChromosomes+" clones: "+numClones+" gene: "+gene);
    	
    	for(int counter=0; counter<numClones; counter++){
    		AntiBody AB=new AntiBody();
    		String mutant="";
    		for(int xmCounter=0; xmCounter<numChromosomes; xmCounter++){
    			int mutationPoint=(int)(Math.random()*gene.length()) ;
    			String temp1=gene.substring(0,mutationPoint);
    			String temp2=gene.substring(mutationPoint);
    			temp1=temp1.substring(0,temp1.lastIndexOf(',')+1);
    			temp2=temp2.substring(temp2.indexOf(','));
    			mutant=temp1+(int)(Math.random()*chromaticNumber)+temp2;    			
    		}
    		AB.setSignature(mutant);
			AB.setChromatic(countColors(mutant));
    		AB.setAffinity(affinity(AB));
    		antibodyPool.add(AB);
    	//	System.out.println("mutant: "+mutant);
    	}
    }
    
    //this method is called to create n random antibodies
	private void generateRandom(int number){
    	
//    	System.out.println(numVertices+"#");
    	for(int i=0; i<number; i++){
    		String tempString="";
    		AntiBody tempAB=new AntiBody();
    		for(int i2=0; i2<numVertices; i2++){
    			tempString+=""+(int) (Math.random()*chromaticNumber)+",";
    			
    		}
			tempAB.setSignature(tempString);
			tempAB.setChromatic(countColors(tempString));
    		tempAB.setAffinity(affinity(tempAB));
    		antibodyPool.add(tempAB);
    		
//    		System.out.println("AB: "+tempString+" AF: "+tempAB.getAffinity());

    	}
    	Collections.sort(antibodyPool);
    	
    }    
    
}