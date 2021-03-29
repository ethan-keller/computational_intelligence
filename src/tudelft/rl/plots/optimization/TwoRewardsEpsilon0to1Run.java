package tudelft.rl.plots.optimization;

import tudelft.rl.plots.Algorithm;

import java.util.ArrayList;
import java.util.List;

import static tudelft.rl.plots.HelperFunctions.*;

public class TwoRewardsEpsilon0to1Run {

    public static void main(String[] args) {

        List<List<Integer>> data = new ArrayList<>();

        double[] epsilons = new double[]{0, 0.25, 0.5, 0.75, 1};
//        double[] epsilons = new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1};

        for (double eps : epsilons) {
            List<List<Integer>> trialData = Algorithm.algorithmFixedTrialsTwoRewards(
                    "./data/toy_maze.txt",10, 0.7, 0.9,  eps, 250);
//        System.out.println(data);

            trialData = normalizeDataDimensions(trialData);

            List<Integer> avarage = getAverage(trialData);
            data.add(avarage);

        }

        int max = Integer.MIN_VALUE;

        data = normalizeDataDimensions(data);

        output(data, "./output/twoRewardsEpsilon0to1.txt");
    }


}
