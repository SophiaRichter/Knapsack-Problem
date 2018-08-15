package de.hcmlab.rl.abschlussprojekt.solution;

import java.util.ArrayList;
import java.util.Iterator;

import de.hcmlab.rl.abschlussprojekt.solution.Multiset.Element;

/**
 * A class representing a multiset that stores the Bricks that can be 
 * placed at a given time. If it was possible to set a Brick as much 
 * as the algorithm wanted, the solution could become trivial. 
 * Therefore the usage of every Brick is restricted to a specified amount.
 * Example:<br>
 * {(1,L),(2,O),(4,-)}<br>
 * represents a state where the algorithm can place one more Brick in the 
 * shape of an L, two more Bricks in the shape of an O and four more 
 * Bricks in the shape of a -. 
 * 
 * @author Patrizia Schalk
 */
public class Multiset implements Iterable<Element>{

	/**
	 * A nested subclass that records the number of times a Brick exists
	 * in the multiset.
	 * 
	 * @author Patrizia Schalk
	 */
	public class Element {
		// This class is only used by Multiset and is hence a nested class
		private int quantity;	// Number of times the multiset contains the element
		private Brick brick;	// Representation of the element
		
		/**
		 * Creates a new Element with a quantity of 1
		 * 
		 * @param brick the Brick that should be placed in the multiset
		 */
		public Element(Brick brick) {
			this.brick = brick;
			this.quantity = 1;
		}
		
		/**
		 * returns the Brick packed in the Element class
		 * 
		 * @return the Brick represented by the Element
		 */
		public Brick getBrick() {
			return this.brick;
		}
		
		/**
		 * Returns how often the multiset contains the Brick
		 * 
		 * @return the quantity of the Brick in the multiset
		 */
		public int getQuantity() {
			return this.quantity;
		}
		
		/**
		 * Increments the quantity of the given Element
		 */
		public void increment() {
			++this.quantity;
		}
		
		/**
		 * Decrements the quantity of the given Element
		 */
		public void decrement() {
			--this.quantity;
		}
		
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Element)) return false;

	        Element elem = (Element) o;
	        
	        if (!this.brick.equals(elem.brick)) return false;
	        // ignore quantity
	        return true;
	    }
	}
	
	private ArrayList<Element> mu;	// the multiset containing the shapes that can be placed
	
	/**
	 * Constructs a new empty Multiset.
	 */
	public Multiset() {
		this.mu = new ArrayList<Element>();
	}
	
	/**
	 * Adds a new Brick to the multiset or increases the quantity
	 * of the Brick in the multiset if the latter already contains it.
	 * 
	 * @param b : the Brick that should be added to the multiset
	 */
	public void add(Brick b) {
		Element e = new Element(b);	// construct an Element out of the given Brick
		if(mu.contains(e)) 	// if the multiset already contains the Element
			mu.get(mu.indexOf(e)).increment(); // increase the quantity of that Element
		else
			mu.add(e);	// else add the new Element to the multiset
	}
	
	/**
	 * Returns how often the multiset contains the Brick.
	 * Returns 0 if the multiset does not contain the Brick.
	 * 
	 * @param b : The brick whose quantity is needed
	 * @return the number of times the multiset contains the Brick b
	 */
	public int contains(Brick b) {
		Element e = new Element(b);	// convert the Brick into an Element
		if(mu.contains(e))	// if the multiset contains the Element
			return mu.get(mu.indexOf(e)).getQuantity();	//return the quantity of that Element
		else 
			return 0; // else return zero
	}
	
	// tries to remove a brick and returns if removing was successful
	/**
	 * Tries to remove a given Brick in the multiset and returns whether or not the operation 
	 * was successful. 
	 * 
	 * @param b : the Brick which should be removed 
	 * @return true if the Brick was found in the multiset and could successfully be removed, false if not
	 */
	public boolean remove(Brick b) {
		Element e = new Element(b);
		if(!mu.contains(e))
			return false;
		Element toRemove = mu.get(mu.indexOf(e));
		if(toRemove.getQuantity() > 1) {
			toRemove.decrement();
			return true;
		}
		else
			return mu.remove(toRemove);
	}
	
	/**
	 * Removes a randomly picked Brick
	 * 
	 * @return the Brick that was removed by the operation or null if nothing could be removed.
	 */
	public Brick removeAny() {
		if(mu.isEmpty()) return null;
		Element e = mu.get(Solution.rand.nextInt(mu.size()));
		if(remove(e.brick))
			return e.brick;
		return null;
	}
	
	/**
	 * Returns whether the multiset contains a Brick
	 * 
	 * @return true if there is no Brick in the multiset, false if there is a Brick in the multiset.
	 */
	public boolean isEmpty() {
		return mu.isEmpty();
	}
	
	@Override
	public String toString() {
		String result = "{";
		for(int i=0; i<mu.size(); i++) {
			Element e = mu.get(i);
			result += "("+e.getQuantity()+", "+e.brick.toString()+")";
			if(i != mu.size()-1) result += ", ";
		}
		result += "}";
		return result;
	}

	@Override
	public Iterator<Element> iterator() {
		return mu.iterator();
	}
}
