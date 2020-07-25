/**
 * @(#)AntiBody.java
 *
 *
 * @author 
 * @version 1.00 2008/3/10
 */


public class AntiBody implements Comparable<AntiBody>{

	private double affinity;
	private String signature;


    public AntiBody() {
    }
    
    public void setAffinity(double aff){
    	affinity=aff;
    }
    
    public double getAffinity(){
    	return affinity;
    }
    
    public void setSignature(String sig){
    	signature=sig;
    }
    
    public String getSignature(){
    	return signature;
    }
    
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