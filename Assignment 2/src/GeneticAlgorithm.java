import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * TSP problem solver using genetic algorithms.
 */
public class GeneticAlgorithm {

    private int generations;
    private int popSize;
    private double eliteProportion;
    private double crossoverProbability;
    private double mutationProbability;
    private int convergenceCriterion;
    private List<Double> globalBests = new ArrayList<>();
    private List<Double> localBests = new ArrayList<>();
    private List<Double> averages = new ArrayList<>();

    /**
     * Constructs a new 'genetic algorithm' object.
     * @param generations the amount of generations.
     * @param popSize the population size.
     * @param eliteProportion proportion of elites
     * @param crossoverProbability probability of crossover
     * @param mutationProbability probability of mutation
     */
    public GeneticAlgorithm(int generations, int popSize, double eliteProportion, double crossoverProbability, double mutationProbability, int convergenceCriterion) {
        this.generations = generations;
        this.popSize = popSize;
        this.eliteProportion = eliteProportion;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.convergenceCriterion = convergenceCriterion;
    }


    /**
     * Knuth-Yates shuffle, reordering a array randomly
     * @param chromosome array to shuffle.
     */
    private void shuffle(int[] chromosome) {
        int n = chromosome.length;
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));
            int swap = chromosome[r];
            chromosome[r] = chromosome[i];
            chromosome[i] = swap;
        }
    }

    /**
     * This method should solve the TSP. 
     * @param pd the TSP data.
     * @return the optimized product sequence.
     */
    public int[] solveTSP(TSPData pd) {
        int elites = Math.max((int) (popSize * eliteProportion), 1);
        Chromosome[] population = new Chromosome[popSize];
        for (int i = 0; i < popSize; i++) {
            population[i] = new Chromosome(pd.getDistances().length);
        }
        Chromosome chromosome = null;
        double fitness = 0;
        int count = 0;
        boolean flag = false;
        for (int i = 0; i < generations; i++) {
            Chromosome[] newPopulation = new Chromosome[popSize];
            double[] populationFitness = new double[popSize];
            double fitnessSum = 0;
            for (int j = 0; j < popSize; j++) {
                populationFitness[j] = computeFitness(pd, population[j]);
                fitnessSum += populationFitness[j];
            }
            int[] ranks = IntStream.range(0, popSize).boxed().sorted(Comparator.comparingDouble(j -> -populationFitness[j])).mapToInt(Integer::intValue).toArray();
            if (populationFitness[ranks[0]] > fitness) {
                chromosome = population[ranks[0]];
                fitness = populationFitness[ranks[0]];
                flag = true;
            }
            if (flag) {
                count = 0;
                flag = false;
            } else {
                count++;
            }
            if (count >= convergenceCriterion) {
                break;
            }
            int index = 0;
            for (; index < elites; index++) {
                newPopulation[index] = population[ranks[index]];
            }
            NavigableMap<Double, Chromosome> roulette = new TreeMap<>();
            double probability = 0;
            for (int j = 0; j < popSize; j++) {
                roulette.put(probability, population[j]);
                probability += populationFitness[j] / fitnessSum;
            }
            if ((popSize - index) % 2 != 0) {
                newPopulation[index++] = roulette.floorEntry(ThreadLocalRandom.current().nextDouble()).getValue();
            }
            for (; index < popSize; index += 2) {
                Chromosome chromosome1 = roulette.floorEntry(ThreadLocalRandom.current().nextDouble()).getValue();
                Chromosome chromosome2 = roulette.floorEntry(ThreadLocalRandom.current().nextDouble()).getValue();
                if (ThreadLocalRandom.current().nextDouble() < crossoverProbability) {
                    int from = ThreadLocalRandom.current().nextInt(chromosome1.size());
                    int to = ThreadLocalRandom.current().nextInt(from + 1, chromosome1.size() + 1);
                    newPopulation[index] = chromosome1.crossover(chromosome2, from, to);
                    newPopulation[index + 1] = chromosome2.crossover(chromosome1, from, to);
                } else {
                    newPopulation[index] = chromosome1;
                    newPopulation[index + 1] = chromosome2;
                }
            }
            for (int j = 0; j < popSize; j++) {
                if (ThreadLocalRandom.current().nextDouble() < mutationProbability) {
                    int n = ThreadLocalRandom.current().nextInt(newPopulation[j].size() - 1);
                    int m = ThreadLocalRandom.current().nextInt(n + 1, newPopulation[j].size());
                    newPopulation[j] = newPopulation[j].mutate(n, m);
                }
            }
            population = newPopulation;
            globalBests.add(fitness);
            localBests.add(populationFitness[ranks[0]]);
            averages.add(fitnessSum / popSize);
        }
        System.out.println(chromosome);
        System.out.println(1 / fitness);
        assert chromosome != null;
        return chromosome.toArray();
    }

    private double computeFitness(TSPData data, Chromosome chromosome) {
        double fitness = data.getStartDistances()[chromosome.get(0)];
        for (int i = 0; i < chromosome.size() - 1; i++) {
            fitness += data.getDistances()[chromosome.get(i)][chromosome.get(i + 1)];
        }
        return 1 / (fitness + data.getEndDistances()[chromosome.get(chromosome.size() - 1)] + chromosome.size());
    }

    public List<Double> getGlobalBests() {
        return globalBests;
    }

    public List<Double> getLocalBests() {
        return localBests;
    }

    public List<Double> getAverages() {
        return averages;
    }

    /**
     * Assignment 2.b
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	//parameters
    	int populationSize = 20;
        int generations = 1600;
        double eliteProportion = 0.1;
        double crossoverProbability = 0.7;
        double mutationProbability = 0.03;
        int convergenceCriterion = 500;
        String persistFile = "./tmp/productMatrixDist";
        
        //setup optimization
        TSPData tspData = TSPData.readFromFile(persistFile);
        GeneticAlgorithm ga = new GeneticAlgorithm(generations, populationSize, eliteProportion, crossoverProbability, mutationProbability, convergenceCriterion);
        
        //run optimzation and write to file
        int[] solution = ga.solveTSP(tspData);
        tspData.writeActionFile(solution, "./data/TSP solution.txt");
    }
}
