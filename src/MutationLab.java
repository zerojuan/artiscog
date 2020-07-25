/**
 * @(#)MutationLab.java
 *
 *
 * @author 
 * @version 1.00 2008/3/10
 */


public class MutationLab {

    public MutationLab(String gene, int numXms) {
    	
    	for(int xmCounter=0; xmCounter<numXms; xmCounter++){
			int mutationPoint=(int)(Math.random()*gene.length()) ;
    		String temp1=gene.substring(0,mutationPoint);
    		String temp2=gene.substring(mutationPoint);
    		temp1=temp1.substring(0,temp1.lastIndexOf(',')+1);
    		temp2=temp2.substring(temp2.indexOf(','));
    		System.out.println("utot".substring(0,2)+" "+"utot".substring(2));
    		System.out.println(temp1+"paningit"+temp2);
    		System.out.println();
    	}
    	
    }
    
    public static void main(String args[]){
    	MutationLab lab=new MutationLab("12345,123,1232445,",10);
    }
    
    
}