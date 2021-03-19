import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the first assignment. Finds shortest path between two points in a maze according to a specific
 * path specification.
 */
public class AntColonyOptimization {
	
	private int antsPerGen;
    private int generations;
    private double Q;
    private double evaporation;
    private Maze maze;
    private double localEvaporation;
    private double exploitationProbability;
    private int convergenceCriterion;
    private List<Double> averages = new ArrayList<>();
    private List<Integer> localBests = new ArrayList<>();
    private List<Integer> globalBests = new ArrayList<>();

    /**
     * Constructs a new optimization object using ants.
     * @param maze the maze .
     * @param antsPerGen the amount of ants per generation.
     * @param generations the amount of generations.
     * @param Q normalization factor for the amount of dropped pheromone
     * @param evaporation the evaporation factor.
     * @param localEvaporation local evaporation factor
     * @param exploitationProbability exploitation probability
     * @param convergenceCriterion maximum number of generations without global best update
     */
    public AntColonyOptimization(Maze maze, int antsPerGen, int generations, double Q, double evaporation, double localEvaporation, double exploitationProbability, int convergenceCriterion) {
        this.maze = maze;
        this.antsPerGen = antsPerGen;
        this.generations = generations;
        this.Q = Q;
        this.evaporation = evaporation;
        this.localEvaporation = localEvaporation;
        this.exploitationProbability = exploitationProbability;
        this.convergenceCriterion = convergenceCriterion;
    }

    /**
     * Loop that starts the shortest path process
     * @param spec Spefication of the route we wish to optimize
     * @return ACO optimized route
     */
    public Route findShortestRoute(PathSpecification spec) {
        maze.reset();
        Route route = null;
        boolean flag = false;
        int count = 0;
        for (int i = 0; i < generations; i++) {
            int sum = 0;
            int best = Integer.MAX_VALUE;
            for (int j = 0; j < antsPerGen; j++) {
                Route localRoute = new Ant(maze, spec, exploitationProbability).findRoute();
                maze.addPheromoneRoute(localRoute, localEvaporation);
                if (route == null || localRoute.shorterThan(route)) {
                    route = localRoute;
                    flag = true;
                }
                sum += localRoute.size();
                if (localRoute.size() < best) {
                    best = localRoute.size();
                }
            }
            maze.evaporate(evaporation);
            assert route != null;
            maze.addPheromoneRoute(route, Q, evaporation);
            if (flag) {
                count = 0;
                flag = false;
            } else {
                count++;
            }
            if (count >= convergenceCriterion) {
                break;
            }
            averages.add((double) sum / antsPerGen);
            localBests.add(best);
            globalBests.add(route.size());
        }
        return route;
    }

    public List<Double> getAverages() {
        return averages;
    }

    public List<Integer> getLocalBests() {
        return localBests;
    }

    public List<Integer> getGlobalBests() {
        return globalBests;
    }

    /**
     * Driver function for Assignment 1
     */
    public static void main(String[] args) throws FileNotFoundException {
    	//parameters
    	int gen = 150;
        int noGen = 10000;
        double Q = 500;
        double evap = 0.1;
        double localEvap = 0.1;
        double exploitationProbability = 0.9;
        int convergenceCriterion = 100;
        double initialPheromone = 1;
        
        //construct the optimization objects
        Maze maze = Maze.createMaze("./data/hard maze.txt", initialPheromone);
        PathSpecification spec = PathSpecification.readCoordinates("./data/hard coordinates.txt");
        AntColonyOptimization aco = new AntColonyOptimization(maze, gen, noGen, Q, evap, localEvap, exploitationProbability, convergenceCriterion);
        
        //save starting time
        long startTime = System.currentTimeMillis();
        
        //run optimization
        Route shortestRoute = aco.findShortestRoute(spec);
        
        //print time taken
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
        
        //save solution
        shortestRoute.writeToFile("./data/hard_solution.txt");
        
        //print route size
        System.out.println("Route size: " + shortestRoute.size());
    }
}
