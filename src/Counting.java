/**
 * @(#)Counting.java
 *
 *
 * @author 
 * @version 1.00 2008/3/17
 */
import java.util.*;

public class Counting {
	
	Vector<Integer> antibody=new Vector<Integer>();

    public Counting() {
    	initVector();
    	System.out.println(countColors(antibody));
    	System.out.println(antibody);
//    	replaceColor(antibody);
//    	System.out.println(antibody);
    	
    }
    
    private void initVector(){
//    	for(){
    		antibody.add(0);
    		antibody.add(0);
    		antibody.add(1);
    		antibody.add(0);
    		antibody.add(1);
//    		antibody.add(0);
//    		antibody.add(25);
//    		antibody.add(25);
//    		antibody.add(25);
//    		antibody.add(2);    		
//    		antibody.add(25);
//    		antibody.add(25);
//    		antibody.add(25);
//    		antibody.add(25);
//    		antibody.add(2);
//    		System.out.println(antibody.elementAt(0)==25);
//    		System.out.println(antibody.elementAt(0).equals(25));
    		
//    	}
    }
    
    private void replaceColor(Vector<Integer> antibody){
    	Vector<Integer> temp=antibody;
    	temp.setElementAt(3,1);
    }
    
	private int countColors(Vector<Integer> antibody){
		Vector<Integer> temp= (Vector<Integer>)antibody.clone();
		int counter=0;
		
		for(int i=0; !temp.isEmpty() ; i++){
			Integer oneColor=temp.elementAt(0);
			//Collection<Integer> c=new Collection<Integer>();
			Vector<Integer> c=new Vector<Integer>();
			c.add(oneColor);
			temp.removeAll(c);
			counter++;
		}
		
		
		return counter;
    }
    
    public static void main(String args[]){
    	Counting c=new Counting();
//    	System.out.println(c.countColors("0,0,0,0,2,"));
    }

    
    
}