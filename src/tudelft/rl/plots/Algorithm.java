package tudelft.rl.plots;

import tudelft.rl.*;
import tudelft.rl.mysolution.MyEGreedy;
import tudelft.rl.mysolution.MyQLearning;

import java.io.File;
import java.util.*;

public class Algorithm {

    public static List<List<Integer>> algorithm(String filename, int numberOfTrials, final double alpha,
                                            final double gamma, final double epsilon, int steps) {


        List<List<Integer>> data = new ArrayList<>(numberOfTrials);


        for (int i = 0; i <= numberOfTrials; i++) {
            int maxSteps = steps;

            data.add(new ArrayList<>());

            List<Integer> trial = data.get(i);
            //load the maze
            Maze maze = new Maze(new File(filename));

            //Set the reward at the bottom right to 10
            final int x = 9;
            final int y = 9;
            maze.setR(maze.getState(x, y), 10);

            //create a robot at starting and reset location (0,0) (top left)
            Agent robot = new Agent(0, 0);

            //make a selection object (you need to implement the methods in this class)
            EGreedy selection = new MyEGreedy();

            //make a Qlearning object (you need to implement the methods in this class)
            QLearning learn = new MyQLearning();

            boolean stop = false;
            //keep learning until you decide to stop
            while (!stop) {
                // implement the action selection and learning cycle
                // selection
                Action action = selection.getEGreedyAction(robot, maze, learn, epsilon);
                State state = robot.getState(maze);
                // perform action and get new state
                State newState = robot.doAction(action, maze);
                // update Q
                learn.updateQ(state, action, maze.getR(newState), newState, maze.getValidActions(robot), alpha, gamma);
                // if arrived at goal -> reset
                if (robot.x == x && robot.y == y) {
                    trial.add(robot.nrOfActionsSinceReset);
                    robot.reset();
                }
                // stopping criterion
                maxSteps--;
                stop = maxSteps <= 0;
            }

        }

        return data;
    }

    public static List<List<Integer>> algorithmFixedTrials(String filename, int numberOfTrials, final double alpha,
                                                final double gamma, final double epsilon, int trials) {


        List<List<Integer>> data = new ArrayList<>(numberOfTrials);


        for (int i = 0; i <= numberOfTrials; i++) {
            int trialLimit = trials;

            data.add(new ArrayList<>());

            List<Integer> trial = data.get(i);
            //load the maze
            Maze maze = new Maze(new File(filename));

            //Set the reward at the bottom right to 10
            final int x = 9;
            final int y = 9;
            maze.setR(maze.getState(x, y), 10);

            //create a robot at starting and reset location (0,0) (top left)
            Agent robot = new Agent(0, 0);

            //make a selection object (you need to implement the methods in this class)
            EGreedy selection = new MyEGreedy();

            //make a Qlearning object (you need to implement the methods in this class)
            QLearning learn = new MyQLearning();

            boolean stop = false;
            //keep learning until you decide to stop
            while (!stop) {
                // implement the action selection and learning cycle
                // selection
                Action action = selection.getEGreedyAction(robot, maze, learn, epsilon);
                State state = robot.getState(maze);
                // perform action and get new state
                State newState = robot.doAction(action, maze);
                // update Q
                learn.updateQ(state, action, maze.getR(newState), newState, maze.getValidActions(robot), alpha, gamma);
                // if arrived at goal -> reset
                if (robot.x == x && robot.y == y) {
                    trial.add(robot.nrOfActionsSinceReset);
                    robot.reset();
                    trialLimit--;
                }
                // stopping criterion
                stop = trialLimit <= 0;
            }

        }

        return data;
    }

    public static List<List<Integer>> algorithmFixedTrialsTwoRewards(String filename, int numberOfTrials, final double alpha,
                                                           final double gamma, final double epsilon, int trials) {


        List<List<Integer>> data = new ArrayList<>(numberOfTrials);


        for (int i = 0; i <= numberOfTrials; i++) {
            int trialLimit = trials;

            data.add(new ArrayList<>());

            List<Integer> trial = data.get(i);
            //load the maze
            Maze maze = new Maze(new File(filename));

            //Set the reward at the bottom right to 10
            final int x1 = 9;
            final int y1 = 9;
            maze.setR(maze.getState(x1, y1), 10);
            final int x2 = 9;
            final int y2 = 0;
            maze.setR(maze.getState(x2, y2), 5);

            //create a robot at starting and reset location (0,0) (top left)
            Agent robot = new Agent(0, 0);

            //make a selection object (you need to implement the methods in this class)
            EGreedy selection = new MyEGreedy();

            //make a Qlearning object (you need to implement the methods in this class)
            QLearning learn = new MyQLearning();

            boolean stop = false;
            //keep learning until you decide to stop
            while (!stop) {
                // implement the action selection and learning cycle
                // selection
                Action action = selection.getEGreedyAction(robot, maze, learn, epsilon);
                State state = robot.getState(maze);
                // perform action and get new state
                State newState = robot.doAction(action, maze);
                // update Q
                learn.updateQ(state, action, maze.getR(newState), newState, maze.getValidActions(robot), alpha, gamma);
                // if arrived at goal -> reset
                if ((robot.x == x1 && robot.y == y1) || (robot.x == x2 && robot.y == y2)) {
                    trial.add(robot.nrOfActionsSinceReset);
                    robot.reset();
                    trialLimit--;
                }
                // stopping criterion
                stop = trialLimit <= 0;
            }

        }

        return data;
    }
}
