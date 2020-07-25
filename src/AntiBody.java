/**
 * @(#)AntiBody.java
 *
 *
 * @author 
 * @version 1.00 2008/3/10
 */
import java.util.Vector;

public class AntiBody implements Comparable<AntiBody>{

	private double affinity;
	private Vector<Integer> signature;
	private int localChromatic;
	private Vector<Integer> violated;


    public AntiBody() {
    	localChromatic=-1;
    	signature=new Vector<Integer>();
    	violated= new Vector<Integer>();
    }
    
    public void setAffinity(double aff){
    	affinity=aff;
    }
    
    public double getAffinity(){
    	return affinity;
    }
    
    public void setSignature(Vector<Integer> sig){
    	signature=sig;
    }
    
    public Vector<Integer> getSignature(){
    	return signature;
    }
    
    public void setChromatic(int forcedChromatic){
    	localChromatic=forcedChromatic;
    }
//    public void setChromatic(int forcedChromatic){
//    	replaceColors();
//    	localChromatic=forcedChromatic;
//    }
    
    public int getChromatic(){
    	return localChromatic;
    }
    
    public void addViolated(int pos1, int pos2){
    	violated.add(pos1);
    	violated.add(pos2);
    }
    
    public Vector<Integer> getViolated(){
    	return violated;
    }
    
//    private void replaceColors(){
//    	String reGene=","+signature;
//    	System.out.println(signature);
//		int counter=0;
//    	for(counter=0;!signature.replaceAll(",","").isEmpty();){
//    		int comma=signature.indexOf(",")+1;
//    		String oneColor=signature.substring(0,comma);
//    		signature=signature.replaceAll(","+oneColor,",");
//    		signature=signature.substring(comma);
//    		reGene=reGene.replaceAll(","+oneColor,","+counter+",");
//    		System.out.println("oneColor: "+oneColor);
//    		counter++;
//    		System.out.println(counter);
//    	}
//    	reGene=reGene.substring(reGene.indexOf(",")+1);
//    	System.out.println(reGene);
//		signature=reGene;
//		localChromatic=counter;
//    }
    
    public int compareTo(AntiBody  a) throws ClassCastException{
    	if (!(a instanceof AntiBody))
      		throw new ClassCastException("An AntiBody object expected.");
    	double b = ((AntiBody) a).getAffinity();
    	double eval = this.getAffinity()-b;
    	int returned=0;
    	if(eval<0)
    		returned=-1;
    	else if(eval>0)
    		returned=1;
    	else returned=0;
//    	return (int)(this.getAffinity()-b);
		return -returned;
    }
    
    
}