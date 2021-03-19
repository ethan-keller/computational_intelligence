import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        Set<Route> routes = Collections.synchronizedSet(new HashSet<>());
        int n = 10;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(n);
        CountDownLatch latch = new CountDownLatch(n);
        for (int i = 0; i < n; i++) {
            executor.execute(() -> {
                try {
                    Maze maze = Maze.createMaze("./data/insane maze.txt", 1);
                    AntColonyOptimization aco = new AntColonyOptimization(maze, 150, 10000, 500, 0.1, 0.1, 0.9, 100);
                    PathSpecification spec = PathSpecification.readCoordinates("./data/insane coordinates.txt");
                    Route route = aco.findShortestRoute(spec);
                    System.out.println(route.size());
                    routes.add(route);
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            routes.stream().min(Comparator.comparingInt(Route::size)).orElseThrow().writeToFile("./data/insane_solution.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void numberOfAntsPerGeneration(String size) {
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        for (int i : new int[]{20, 50, 100, 150, 200}) {
            executor.execute(() -> {
                try {
                    Maze maze = Maze.createMaze("./data/" + size + " maze.txt", 1);
                    AntColonyOptimization aco = new AntColonyOptimization(maze, i, 10000, 1500, 0.1, 0.1, 0.9, 100);
                    PathSpecification spec = PathSpecification.readCoordinates("./data/" + size + " coordinates.txt");
                    aco.findShortestRoute(spec);
                    try (PrintWriter pw = new PrintWriter("./plot/" + size + "/ants/" + i + ".txt")) {
                        pw.println(aco.getGlobalBests().toString());
                        pw.println(aco.getLocalBests().toString());
                        pw.println(aco.getAverages().toString());
                    }
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    private static void q(String size) {
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        for (double i : new double[]{500, 800, 1000, 1500, 2000}) {
            executor.execute(() -> {
                try {
                    Maze maze = Maze.createMaze("./data/" + size + " maze.txt", 1);
                    AntColonyOptimization aco = new AntColonyOptimization(maze, 100, 10000, i, 0.1, 0.1, 0.9, 100);
                    PathSpecification spec = PathSpecification.readCoordinates("./data/" + size + " coordinates.txt");
                    aco.findShortestRoute(spec);
                    try (PrintWriter pw = new PrintWriter("./plot/" + size + "/q/" + i + ".txt")) {
                        pw.println(aco.getGlobalBests().toString());
                        pw.println(aco.getLocalBests().toString());
                        pw.println(aco.getAverages().toString());
                    }
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    private static void evaporationRate(String size) {
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        for (double i : new double[]{0.1, 0.2, 0.35, 0.6, 0.8}) {
            executor.execute(() -> {
                try {
                    Maze maze = Maze.createMaze("./data/" + size + " maze.txt", 1);
                    AntColonyOptimization aco = new AntColonyOptimization(maze, 100, 10000, 1500, i, i, 0.9, 100);
                    PathSpecification spec = PathSpecification.readCoordinates("./data/" + size + " coordinates.txt");
                    aco.findShortestRoute(spec);
                    try (PrintWriter pw = new PrintWriter("./plot/" + size + "/evaporation/" + i + ".txt")) {
                        pw.println(aco.getGlobalBests().toString());
                        pw.println(aco.getLocalBests().toString());
                        pw.println(aco.getAverages().toString());
                    }
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    private static void exploitationProbability(String size) {
        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        for (double i : new double[]{0, 0.2, 0.5, 0.7, 0.9}) {
            executor.execute(() -> {
                try {
                    Maze maze = Maze.createMaze("./data/" + size + " maze.txt", 1);
                    AntColonyOptimization aco = new AntColonyOptimization(maze, 100, 10000, 1500, 0.1, 0.1, i, 100);
                    PathSpecification spec = PathSpecification.readCoordinates("./data/" + size + " coordinates.txt");
                    aco.findShortestRoute(spec);
                    try (PrintWriter pw = new PrintWriter("./plot/" + size + "/exploitation/" + i + ".txt")) {
                        pw.println(aco.getGlobalBests().toString());
                        pw.println(aco.getLocalBests().toString());
                        pw.println(aco.getAverages().toString());
                    }
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
    }
}
