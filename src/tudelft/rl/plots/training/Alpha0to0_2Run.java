package tudelft.rl.plots.training;

import tudelft.rl.plots.Algorithm;

import java.util.ArrayList;
import java.util.List;

import static tudelft.rl.plots.HelperFunctions.*;

public class Alpha0to0_2Run {

    public static void main(String[] args) {

        List<List<Integer>> data = new ArrayList<>();

        double[] learningRates = new double[]{0, 0.05, 0.1, 0.15, 0.2};
//        double[] epsilons = new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1};

        for (double alpha : learningRates) {
            List<List<Integer>> trialData = Algorithm.algorithmFixedTrials("./data/toy_maze.txt",
                    10, alpha, 0.9,  0.1, 250);
//        System.out.println(data);

            trialData = normalizeDataDimensions(trialData);

            List<Integer> avarage = getAverage(trialData);
            data.add(avarage);

        }

        int max = Integer.MIN_VALUE;

        data = normalizeDataDimensions(data);

        output(data, "./output/alpha0to0_2.txt");
    }


}
