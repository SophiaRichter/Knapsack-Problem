package de.hcmlab.rl.abschlussprojekt.solution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import de.hcmlab.rl.abschlussprojekt.KnapsackGame;
import de.hcmlab.rl.abschlussprojekt.gridworld.Action;
import de.hcmlab.rl.abschlussprojekt.learning.LearningAlgorithm;
import de.hcmlab.rl.abschlussprojekt.solution.Multiset.Element;

/**
 * A class defining the Q-learning of an agent that chooses an action
 * and gets a specified amount of reward.
 * 
 * @author Patrizia Schalk and Manuel Richter
 */
public class Solution implements LearningAlgorithm
{
	public static Random rand = new Random(333);
	public final boolean logging = false;
	
	ArrayList<Q> qLearn = new ArrayList<Q>();
	final int epsilon = 15;
	Multiset sack;
	static double bestreward = 0;
	double totalreward = 0;
	
	public int episodeCount = 0;
    public CSVPrinter printer;
    
    long start = 0L;
    long end = 0L;
	
	@Override
	public Action selectAction(Multiset sack, State currentState) 
	{
		this.sack = sack;	// store the multiset that contains the Bricks that may be used
		if(qLearn.isEmpty()) initialize();	// if there aren't already Q-values initialize them
		if (rand.nextInt(100) < epsilon) return explore();	// explore with a probability of epsilon%
		else return exploit(currentState);	// exploit with a probability of (100-epsilon)%
	}

	/**
	 * Initializes the Q-value-table with all possible States and all Bricks that can be placed on every position.
	 */
	private void initialize() {
		int max = (int)Math.pow(2,KnapsackGame.WIDTH*KnapsackGame.HEIGHT+1);	// The maximum number of States that can possibly occur
		for(long i=0; i<=max; i++)
		{
			State s = new State(i);		// create a new State for every Integer that may describe a game-grid
			for(Element e : sack) {
				for(int j=0; j<KnapsackGame.WIDTH; j++)
				{
					for(int k=0; k<KnapsackGame.HEIGHT; k++)
					{
						qLearn.add(new Q(s,new Action(j,k,e.getBrick())));	// add a new Q value for the State s, every possible position and every possible Brick
					}
				}
			}
		}
		State s = new State(Long.MAX_VALUE);		// The error state shall be the maximum of an Integer value
		for(Element e : sack) {
			for(int j=0; j<KnapsackGame.WIDTH; j++)
			{
				for(int k=0; k<KnapsackGame.HEIGHT; k++)
				{
					Q error = new Q(s,new Action(j,k,e.getBrick()));	// create a Q value describing the error state for each possible position and Brick
					error.setReward(0);		// the error state should not be attractive for the agent due to optimistic values
					qLearn.add(error);	// add the Q-value to the Q-value-table
				}
			}
		}
		if(logging) createNewLogTable();	// create a new Table for Logging
		start = System.nanoTime();
	}
	
	/**
	 * Explores a random Action that might not have a good Q-value
	 * 
	 * @return a randomly picked Action, containing a random position and a random Brick
	 */
	private Action explore() 
	{
		Action a = new Action(rand.nextInt(KnapsackGame.WIDTH),rand.nextInt(KnapsackGame.HEIGHT), sack.removeAny());
		return a;
	}

	/**
	 * Exploits by picking the best Action the Q-value table has to offer
	 * 
	 * @param currentState : the state the game currently is in
	 * @return the Action that promises the best reward in the given state
	 */
	private Action exploit(State currentState) 
	{
		Action a = bestAction(currentState);	// get the best possible Action
		sack.remove(a.getBrick());				// remove the Brick associated to the Action from the multiset of possible Bricks
		return a;
	}

	/**
	 * A method that finds the index of the Action with the highest Q-value in the table of Q-values in the current state.
	 * @param currentState	: the state the game currently is in
	 * @return an Integer describing the index of the best Action in the list of Q-values
	 */
	private int bestActionIndex(State currentState) {
		double max = -100;	// the maximum reward currently seen (Cannot be Double.MIN_VALUE since this seems to be 0.0)
		int index = -1;		// the index of the Action that currently has the highest reward of all viewed Actions
		
		for(Q entry : qLearn) {
			if(entry.getState().equals(currentState) && (sack.contains(entry.getAction().getBrick())>0) && entry.getReward() > max)
			{ 	//1. the State of the currently viewed Q-value has to be the same as the state the game is in
				//2. the multiset has contain the Brick which would lead to the best Action
				//3. the promised reward have to be greater than the maximum of the reward we have currently seen
				max = entry.getReward();
				index = qLearn.indexOf(entry);
			}
		}
		return index;
	}
	
	/**
	 * A method that finds the Action with the highest Q-value in the table of Q-values in the current state.
	 * 
	 * @param currentState : the state the game currently is in
	 * @return the Action with the highest Q-value
	 */
	private Action bestAction(State currentState) 
	{
		int index = bestActionIndex(currentState); 	// get the index of the bestAction in the table of Q-values
		if (index == -1) return explore();			// if no such index exists, simply explore
		return qLearn.get(index).getAction();		// return the found Action
	}

	@Override
	public boolean feedback(State previousFieldState, State newFieldState, Action action, double reward, boolean finalState) 
	{
		double alpha = 0.5, gamma = 0.95;		// values of alpha and gamma for the update method of Q-learning
    	double new_Q_val = 0;					// declaration of the new Q-value for the current State-Action pair
    	Q q_Val = new Q(previousFieldState,action);	// old Q-value for the current State-Action pair
    	int index = -1;							// declaration for the index of said old Q-value
    	if(qLearn.contains(q_Val)) index = qLearn.indexOf(q_Val);	// if qLearn does not contain the Q-value, an Error occurred.
    	
    	if(index != -1)
    	{	// if the index is found
    		Q current = qLearn.get(index);	// get the old Q-value in the table of Q-values
    		if(sack.isEmpty()) {	// if the sack is already empty there is no best Action that could be taken
    			new_Q_val = current.getReward() + alpha * (reward - current.getReward());
        		qLearn.get(index).setReward(new_Q_val);
    		}
    		else {
    			Q nextBest = qLearn.get(bestActionIndex(newFieldState));	// also get the best Q-value possible in the next state 
    			new_Q_val = current.getReward() + alpha * (reward + gamma * nextBest.getReward() - current.getReward());
    			qLearn.get(index).setReward(new_Q_val);	// set the new current Q-value to the new calculated one
    		}
    	}
    	else
    	{
    		System.err.println("Error in feedback method of class Solution: ");
    		System.err.println("The Q-value for the state-action pair ("+previousFieldState+", "+action+") cannot be found.");
    		System.exit(-1);
    	}
    	totalreward += reward;	// keep track of the total reward you got this far
    	if(logging) log(previousFieldState, action, reward, new_Q_val);	// log the state, the chosen action and the newly calculated Q-value
    	if(totalreward > bestreward) { 
			bestreward = totalreward;	// if a better reward was gained than ever before save this best reward
			return true;				// mark that the current result as the best so far
		}
    	if(finalState)
    	{
    		if(logging) createNewLogTable();
    		totalreward = 0;				// the episode ended - set the total reward to 0
    	}
    	return false;
	}
	
	/**
	 * Creates a new log table on the file system.
	 * The log table is found in the assets-folder in a new folder called "CSV". 
	 * 
	 * @author Manuel Richter
	 */
	public void createNewLogTable() 
    {
		// time measuring
    	if(episodeCount != 0 && episodeCount % 100 == 0)
    	{
    		end = System.nanoTime();
    		System.out.println("Time needed for 100 episodes: "+((end-start)/1000000000.0)+" seconds.");
    		start = System.nanoTime();
    	}
    	episodeCount++;
        try  
        {	
        	if(printer != null) printer.close();
        	FileWriter out = new FileWriter("CSV//Episode"+ episodeCount +".csv");
        	printer = new CSVPrinter(out,CSVFormat.EXCEL.withHeader("State","Action","Reward","Q-Value").withDelimiter(';'));
        }
        catch (IOException e) 
        {
        	e.printStackTrace();
		}       
    }
	
	/**
	 * Logs the result of the feedback method into an existing log table.
	 * 
	 * @param s : the state before the algorithm performed the action
	 * @param a : the action the algorithm performed
	 * @param r : the actual reward the algorithm got for said action
	 * @param Q : the newly updated Q-value for the state-action pair
	 * @author Manuel Richter
	 */
	public void log(State s, Action a, double r, double Q)
    {
    	try 
    	{
    		ArrayList<String> arrr = new ArrayList<String>();
    		arrr.add(s.toString());
    		arrr.add(a.toString());
    		arrr.add(String.valueOf(r));
    		arrr.add(String.valueOf(Q));
    		
    		printer.printRecord(arrr);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
}
