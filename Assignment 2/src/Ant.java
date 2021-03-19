import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that represents the ants functionality.
 */
public class Ant {

    private Maze maze;
    private Coordinate start;
    private Coordinate end;
    private Coordinate currentPosition;
    private double exploitationProbability;

    /**
     * Constructor for ant taking a Maze and PathSpecification.
     * @param maze Maze the ant will be running in.
     * @param spec The path specification consisting of a start coordinate and an end coordinate.
     * @param exploitationProbability Exploitation probability
     */
    public Ant(Maze maze, PathSpecification spec, double exploitationProbability) {
        this.maze = maze;
        this.start = spec.getStart();
        this.end = spec.getEnd();
        this.currentPosition = start;
        this.exploitationProbability = exploitationProbability;
    }

    /**
     * Method that performs a single run through the maze by the ant.
     * @return The route the ant found through the maze.
     */
    public Route findRoute() {
        Route route = new Route(start);
        Set<Coordinate> visited = new HashSet<>();
        while (!currentPosition.equals(end)) {
            visited.add(currentPosition);
            SurroundingPheromone pheromones = maze.getSurroundingPheromone(currentPosition);
            List<Direction> directions = new ArrayList<>();
            boolean endReached = false;
            Direction nextDirection = null;
            double pheromoneSum = 0;
            double maximumPheromone = 0;
            for (Direction direction : Direction.values()) {
                double pheromone = pheromones.get(direction);
                if (pheromone > 0) {
                    Coordinate nextPosition = currentPosition.add(direction);
                    if (nextPosition.equals(end)) {
                        nextDirection = direction;
                        endReached = true;
                        break;
                    } else {
                        if (!visited.contains(nextPosition)) {
                            directions.add(direction);
                            pheromoneSum += pheromone;
                            if (pheromone > maximumPheromone) {
                                nextDirection = direction;
                                maximumPheromone = pheromone;
                            }
                        }
                    }
                }
            }
            boolean reverseStep = false;
            if (!endReached) {
                if (directions.isEmpty()) {
                    currentPosition = currentPosition.subtract(route.removeLast());
                    reverseStep = true;
                } else {
                    if (ThreadLocalRandom.current().nextDouble() >= exploitationProbability) {
                        NavigableMap<Double, Direction> roulette = new TreeMap<>();
                        double probability = 0;
                        for (Direction direction : directions) {
                            roulette.put(probability, direction);
                            probability += pheromones.get(direction) / pheromoneSum;
                        }
                        nextDirection = roulette.floorEntry(ThreadLocalRandom.current().nextDouble()).getValue();
                    }
                }
            }
            if (!reverseStep) {
                route.add(nextDirection);
                currentPosition = currentPosition.add(nextDirection);
            }
        }
        return route;
    }
}

