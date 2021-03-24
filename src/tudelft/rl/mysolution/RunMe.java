package tudelft.rl.mysolution;

import tudelft.rl.Action;
import tudelft.rl.Agent;
import tudelft.rl.EGreedy;
import tudelft.rl.Maze;
import tudelft.rl.QLearning;
import tudelft.rl.State;
import java.io.File;

public class RunMe {

    public static void main(String[] args) {

        //load the maze
        Maze maze = new Maze(new File("./data/toy_maze.txt"));

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
        final double alpha = 0.7;
        final double gamma = 0.9;
        final double epsilon = 0.1;
        int maxSteps = 30000;
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
                robot.reset();
            }
            // stopping criterion
            maxSteps--;
            stop = maxSteps <= 0;
        }

    }

}
