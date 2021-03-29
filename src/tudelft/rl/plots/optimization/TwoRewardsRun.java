package tudelft.rl.plots.optimization;

import tudelft.rl.plots.Algorithm;

import java.util.List;

import static tudelft.rl.plots.HelperFunctions.normalizeDataDimensions;
import static tudelft.rl.plots.HelperFunctions.output;

public class TwoRewardsRun {
    public static void main(String[] args) {
        List<List<Integer>> data = Algorithm.algorithmFixedTrialsTwoRewards("./data/toy_maze.txt",
                10, 0.7, 0.9,  0.1, 250);

        data = normalizeDataDimensions(data);

        output(data, "./output/twoRewards.txt");
    }
}
