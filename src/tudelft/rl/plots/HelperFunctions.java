package tudelft.rl.plots;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelperFunctions {

    public static List<Integer> getAverage(List<List<Integer>> trialData) {

        List<Integer> result = new ArrayList<>();

        int n = trialData.size();
        int d = trialData.get(0).size();

        for (int i = 0; i < d; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += trialData.get(j).get(i);
            }
            sum = sum / n;
            result.add(sum);
        }
        return result;
    }

    public static List<List<Integer>> normalizeDataDimensions(List<List<Integer>> data) {
        int max = Integer.MIN_VALUE;

        for (List<Integer> trial : data) {
            if (trial.size() > max) {
                max = trial.size();
            }
        }

        for (List<Integer> trial : data) {
            while (trial.size() < max) {
                trial.add(0);
            }
        }
        return data;
    }

    public static void output(List<List<Integer>> data, String outputPath) {
        List<String> trialList = new ArrayList<>();


        for (List<Integer> trial : data) {
            trialList.add(trial.stream().map(String::valueOf)
                    .collect(Collectors.joining(",")));
        }
        String content = String.join("\n", trialList);

        try {

            File file = new  File(outputPath);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
