package tudelft.rl.mysolution;

import tudelft.rl.Action;
import tudelft.rl.Agent;
import tudelft.rl.EGreedy;
import tudelft.rl.Maze;
import tudelft.rl.QLearning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class MyEGreedy extends EGreedy {
    private final Random random = new Random();

    @Override
    public Action getRandomAction(Agent r, Maze m) {
        List<Action> actions = m.getValidActions(r);
        // select random actions (uniformly)
        return actions.get(random.nextInt(actions.size()));
    }

    @Override
    public Action getBestAction(Agent r, Maze m, QLearning q) {
        ArrayList<Action> actions = m.getValidActions(r);
        // shuffle to counter bias in case agent did not learn enough
        Collections.shuffle(actions);
        double[] actionValues = q.getActionValues(r.getState(m), actions);
        // return action with highest Q
        return actions.get(IntStream.range(0, actionValues.length).boxed().max(Comparator.comparingDouble(i -> actionValues[i])).orElseThrow());
    }

    @Override
    public Action getEGreedyAction(Agent r, Maze m, QLearning q, double epsilon) {
        // epsilon probability for random
        // (1 - epsilon) probability for greedy
        return random.nextDouble() < epsilon ? getRandomAction(r, m) : getBestAction(r, m, q);
    }

}
