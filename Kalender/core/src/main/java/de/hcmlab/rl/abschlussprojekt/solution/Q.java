package de.hcmlab.rl.abschlussprojekt.solution;

import de.hcmlab.rl.abschlussprojekt.KnapsackGame;
import de.hcmlab.rl.abschlussprojekt.gridworld.Action;

/**
 * A class representing a Q value in the Q-value-table 
 * needed for Q-learning.
 * 
 * @author Manuel Richter
 */
public class Q {

	private State s; //state
	private Action a; //action
	private double r; //reward
	
	/**
	 * Constructs a Q object for the state s and the action a
	 * with optimistic initial values for the reward.
	 * 
	 * @param s : the state in which the Q-value should be requested
	 * @param a : the Action for which the saved reward is expected
	 */
	public Q(State s, Action a)
	{
		this.s = s;
		this.a = a;
		this.r = KnapsackGame.average_reward;
	}
	
	/**
	 * @return the State in which the Q-value should be requested
	 */
	public State getState()
	{
		return this.s;
	}
	
	/**
	 * @return the Action for which the saved reward is expected
	 */
	public Action getAction()
	{
		return this.a;
	}
	
	/**
	 * Sets the saved reward to a specified value
	 * 
	 * @param reward : the amount of reward that should be expected in the given State when the given Action is performed
	 */
	public void setReward(double reward)
	{
		this.r = reward;
	}
	
	/** 
	 * @return the amount of reward that is expected in the given State when the given Action is performed
	 */
	public double getReward()
	{
		return r;
	}
	
	@Override
	public boolean equals(Object o) 
	{
		if(o instanceof Q) 
		{
			Q compare = (Q) o;
			if(compare.getState().equals(this.getState()) && compare.getAction().equals(this.getAction()))
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	public String toString() 
	{
		return "("+s.toString()+","+a.toString()+")";
	}
	
}
