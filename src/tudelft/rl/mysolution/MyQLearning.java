package tudelft.rl.mysolution;

import java.util.ArrayList;
import java.util.Comparator;

import tudelft.rl.Action;
import tudelft.rl.QLearning;
import tudelft.rl.State;

public class MyQLearning extends QLearning {

	@Override
	public void updateQ(State s, Action a, double r, State s_next, ArrayList<Action> possibleActions, double alpha, double gamma) {
		setQ(s, a, getQ(s, a) + alpha * (r + gamma * getQ(s_next, possibleActions.stream().max(Comparator.comparingDouble(action -> getQ(s_next, action))).orElseThrow()) - getQ(s, a)));
	}

}
