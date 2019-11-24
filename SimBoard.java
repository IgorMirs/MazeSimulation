package M011_CellularAutomataWithObstacles;

import java.util.Random;

import model.modeling.CAModels.TwoDimCellSpace;

public class SimBoard extends TwoDimCellSpace {
    
    public Random R = new Random();
    
	public SimBoard() {
		
		// Create board with the given dimension and percentage of cells with obstacles
		this(40, 40, 30);
	}

	public SimBoard(int width, int height, int obstPercent) {
		super("Board", width, height);

		// Set the number of Cells
		this.numCells = width * height;
		
		//define the entrance and the exit cells
		int[] entrCoord = {Math.abs(R.nextInt() % (width)), Math.abs(R.nextInt() % (height))}; 
		int[] exitCoord = {Math.abs(R.nextInt() % (width)), Math.abs(R.nextInt() % (height))};
		do {
		    entrCoord[0] = Math.abs(R.nextInt() % (width));
		    entrCoord[1] = Math.abs(R.nextInt() % (height));
		}
		while (entrCoord[0] == exitCoord[0] && entrCoord[1] == exitCoord[1]);
        
		//create class with the statistics
        boardStat stat = new boardStat(height, width, exitCoord, entrCoord);
		
		// Create Board
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
			    
				
				// Create theCell at location i and j 
				SimCell theCell = new SimCell(i, j, stat);
				
				theCell.state = 0;  //unknown cell
				theCell.isObstacle = R.nextInt((Integer) 100/obstPercent) == 0;
                
				if (i == entrCoord[0] && j == entrCoord[1]) {
				    stat.visitedCells.elementAt(i).set(j, 1);
				    theCell.state = 2; //entrance cell
				    theCell.isObstacle = false;
				}
				else if (i == exitCoord[0] && j == exitCoord[1]) {
				    theCell.state = 3; //exit cell
				    theCell.isObstacle = false;
				}
				
				if (theCell.isObstacle) {
				    theCell.state = 1; //obstacle cell
				    stat.visitedCells.elementAt(i).set(j, 2);				
				}
				// Add Cell
				addCell(theCell);
			}
		}

		// Coupling
		doNeighborToNeighborCoupling();
	}
}
