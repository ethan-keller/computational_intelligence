package tudelft.rl.plots.development;

import tudelft.rl.plots.Algorithm;

import java.util.List;

import static tudelft.rl.plots.HelperFunctions.normalizeDataDimensions;
import static tudelft.rl.plots.HelperFunctions.output;

public class Run {

    public static void main(String[] args) {
        List<List<Integer>> data = Algorithm.algorithmFixedTrials("./data/toy_maze.txt",
                10, 0.7, 0.9,  0.1, 250);

        data = normalizeDataDimensions(data);

        output(data, "./output/default.txt");
    }


}
