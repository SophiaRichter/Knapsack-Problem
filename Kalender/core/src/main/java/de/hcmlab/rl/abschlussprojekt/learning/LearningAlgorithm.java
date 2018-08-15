package de.hcmlab.rl.abschlussprojekt.learning;

import de.hcmlab.rl.abschlussprojekt.gridworld.Action;
import de.hcmlab.rl.abschlussprojekt.solution.Multiset;
import de.hcmlab.rl.abschlussprojekt.solution.State;

/**
 * An interface for the actor that has to chose an action and gets a reward for that action.
 * 
 * @author Manuel Richter
 */
public interface LearningAlgorithm {

    Action selectAction(Multiset sack, State currentState);

    boolean feedback(State previousFieldState, State newFieldState, Action action, double reward, boolean finalState);

}
