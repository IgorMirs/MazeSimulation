package M011_CellularAutomataWithObstacles;

import java.util.Stack;
import java.util.Vector;

public class boardStat
{
    public Vector<Vector<Integer>> visitedCells;
    public int nSteps = 0;
    public int xExit;
    public int yExit;
    public int xEntrance;
    public int yEntrance;
    public Stack<String> path = new Stack<String>();
    
    public boardStat(int height, int width, int [] exitCoord, int [] entrCoord) {
        xExit = exitCoord[0];
        yExit = exitCoord[1];
        xEntrance = entrCoord[0];
        yEntrance = entrCoord[1];
      //creating the matrix for visited cells initialized with zeros
        visitedCells = new Vector<Vector<Integer>>();
        for (int i = 0; i < height; i++) {
            Vector<Integer> temp = new Vector<Integer>();
            for (int j = 0; j < width; j++) {
                temp.add(0);
            }
            visitedCells.add(temp);
        }
    }
}
