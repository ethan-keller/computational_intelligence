import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

/**
 * Class that holds all the maze data. This means the pheromones, the open and blocked tiles in the system as
 * well as the starting and end coordinates.
 */
public class Maze {
    private int width;
    private int length;
    private int[][] walls;
    private double[][] pheromones;
    private Coordinate start;
    private Coordinate end;
    private double initialPheromone;

    /**
     * Constructor of a maze
     * @param walls int array of tiles accessible (1) and non-accessible (0)
     * @param width width of Maze (horizontal)
     * @param length length of Maze (vertical)
     * @param initialPheromone initial pheromone
     */
    public Maze(int[][] walls, int width, int length, double initialPheromone) {
        this.walls = walls;
        this.length = length;
        this.width = width;
        pheromones = new double[width][length];
        this.initialPheromone = initialPheromone;
        initializePheromones();
    }

    /**
     * Initialize pheromones to a start value.
     */
    private void initializePheromones() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                pheromones[x][y] = walls[x][y] == 1 ? initialPheromone : 0;
            }
        }
    }

    /**
     * Reset the maze for a new shortest path problem.
     */
    public void reset() {
        initializePheromones();
    }

    public void addPheromoneRoute(Route r, double rho) {
        Coordinate position = r.getStart();
        for (Direction direction : r.getRoute()) {
            position = position.add(direction);
            pheromones[position.getX()][position.getY()] = (1 - rho) * pheromones[position.getX()][position.getY()] + rho * initialPheromone;
        }
    }

    /**
     * Update the pheromones along a certain route according to a certain Q
     * @param r The route of the ants
     * @param Q Normalization factor for amount of dropped pheromone
     * @param rho Evaporation factor
     */
    public void addPheromoneRoute(Route r, double Q, double rho) {
        double deposit = rho * Q / r.size();
        Coordinate position = r.getStart();
        for (Direction direction : r.getRoute()) {
            position = position.add(direction);
            pheromones[position.getX()][position.getY()] += deposit;
        }
    }

    /**
     * Update pheromones for a list of routes
     * @param routes A list of routes
     * @param Q Normalization factor for amount of dropped pheromone
     * @param rho Evaporation factor
     */
    public void addPheromoneRoutes(List<Route> routes, double Q, double rho) {
        for (Route r : routes) {
            addPheromoneRoute(r, Q, rho);
        }
    }

    /**
     * Evaporate pheromone
     * @param rho evaporation factor
     */
    public void evaporate(double rho) {
        double factor = 1 - rho;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                pheromones[x][y] *= factor;
            }
        }
    }

    /**
     * Width getter
     * @return width of the maze
     */
    public int getWidth() {
        return width;
    }

    /**
     * Length getter
     * @return length of the maze
     */
    public int getLength() {
        return length;
    }


    /**
     * Returns a the amount of pheromones on the neighbouring positions (N/S/E/W).
     * @param position The position to check the neighbours of.
     * @return the pheromones of the neighbouring positions.
     */
    public SurroundingPheromone getSurroundingPheromone(Coordinate position) {
        return new SurroundingPheromone(getPheromone(position.add(Direction.North)), getPheromone(position.add(Direction.East)), getPheromone(position.add(Direction.South)), getPheromone(position.add(Direction.West)));
    }

    /**
     * Pheromone getter for a specific position. If the position is not in bounds returns 0
     * @param pos Position coordinate
     * @return pheromone at point
     */
    private double getPheromone(Coordinate pos) {
        return inBounds(pos) ? pheromones[pos.getX()][pos.getY()] : 0;
    }


    /**
     * Check whether a coordinate lies in the current maze.
     * @param position The position to be checked
     * @return Whether the position is in the current maze
     */
    private boolean inBounds(Coordinate position) {
        return position.xBetween(0, width) && position.yBetween(0, length);
    }

    /**
     * Representation of Maze as defined by the input file format.
     * @return String representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        sb.append(' ');
        sb.append(length);
        sb.append(" \n");
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++ ) {
                sb.append(walls[x][y]);
                sb.append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method that builds a mze from a file
     * @param filePath Path to the file
     * @param initialPheromone Initial pheromone
     * @return A maze object with pheromones initialized to 0's inaccessible and 1's accessible.
     */
    public static Maze createMaze(String filePath, double initialPheromone) throws FileNotFoundException {
        Scanner scan = new Scanner(new FileReader(filePath));
        int width = scan.nextInt();
        int length = scan.nextInt();
        int[][] mazeLayout = new int[width][length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                mazeLayout[x][y] = scan.nextInt();
            }
        }
        scan.close();
        return new Maze(mazeLayout, width, length, initialPheromone);
    }
}
