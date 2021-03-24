package tudelft.rl;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class QLearning {
    private HashMap<State, HashMap<Action, Double>> Q;

    public QLearning() {
        Q = new HashMap<>();
    }

    public double getQ(State s, Action a) {
        //checks if it can find a value for <s,a> in q and returns it, if not return 0.
        HashMap<Action, Double> actionValues = Q.get(s);
        if (actionValues != null) {
            return actionValues.getOrDefault(a, 0.0);
        } else
            return 0;
    }

    public void setQ(State s, Action a, double q) {
        //sets the value of am <s,a> pair to q
        HashMap<Action, Double> actionValues = Q.get(s);
        if (actionValues != null) {
            actionValues.put(a, q);
        } else {
            //no entry known for s, make one and store the action value too
            actionValues = new HashMap<>();
            actionValues.put(a, q);
            Q.put(s, actionValues);
        }
    }

    public double[] getActionValues(State s, ArrayList<Action> actions) {
        //returns the associated action values for all actions in <actions> in that order;
        double[] result = new double[actions.size()];
        int count = 0;
        for (Action a : actions) {
            result[count++] = getQ(s, a);
        }
        return result;
    }

    public abstract void updateQ(State s, Action a, double r, State s_next, ArrayList<Action> possibleActions, double alpha, double gamma);
}
