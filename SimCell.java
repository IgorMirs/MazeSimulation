package M011_CellularAutomataWithObstacles;

import java.util.*;

import javafx.scene.paint.Color;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;
import view.CAView.CAViewUI;

public class SimCell extends TwoDimCell {

	public int state = 0;
	public Stack<String> path;
	public boolean deadEnd;
	public boolean movingBack;
	public boolean isObstacle;
	
	public boardStat stat;
	
	public int xEntrance;
	public int yEntrance;
	public int xExit;
    public int yExit;
    
	public SimCell(int x, int y, boardStat stat_) {
		super(x, y);
		stat = stat_;
		path = stat.path;
		xEntrance = stat.xEntrance;
		yEntrance = stat.yEntrance;
		xExit = stat.xExit;
		yExit = stat.yExit;
	}

	public void initialize() {
		super.initialize();
		deadEnd = false;
		movingBack = false;
		if (state == 0) {
			holdIn("unknown", INFINITY);
		} else if (state == 1) {
			holdIn("obstacle", INFINITY);
		} else if (state == 2) {
			holdIn("actor", 1);
		} else if (state == 3) {
		    holdIn("exit", INFINITY);
		}
		CAViewUI.addPhaseColor("entrance", Color.RED);
		CAViewUI.addPhaseColor("actor", Color.CHOCOLATE);
		CAViewUI.addPhaseColor("unknown", Color.BLACK);
		CAViewUI.addPhaseColor("path", Color.WHITE);
		CAViewUI.addPhaseColor("exit", Color.LIME);
		CAViewUI.addPhaseColor("obstacle", Color.GRAY);
	}

	public void deltext(double e, message x) {

		Continue(e);
		for (int i = 0; i < x.getLength(); i++) {
		    stat.nSteps++;
		    if (xcoord == xExit && ycoord == yExit) {
                System.out.println("I FOUND THE EXIT IN " + stat.nSteps + " STEPS");
                holdIn("actor", INFINITY);
                break;
            }
		    
		    if (somethingOnPort(x, "inS")) {
			    Boolean movBack  = ((cellMsg) x.getValOnPort("inS", i)).movBack;
			    stat.visitedCells.elementAt(xcoord).set(ycoord, 1);
			    if (movBack == false)
			        path.push("North");
				holdIn("actor", 1);
			}
			
			if (somethingOnPort(x, "inW")) {
			    Boolean movBack  = ((cellMsg) x.getValOnPort("inW", i)).movBack;
			    if (movBack == false) 
			        path.push("East");
                stat.visitedCells.elementAt(xcoord).set(ycoord, 1);
                holdIn("actor", 1);
            }
			
			if (somethingOnPort(x, "inN")) {
			    Boolean movBack  = ((cellMsg) x.getValOnPort("inN", i)).movBack;
			    if (movBack == false)
			        path.push("South");
			    stat.visitedCells.elementAt(xcoord).set(ycoord, 1);
                holdIn("actor", 1);
            }
			
			if (somethingOnPort(x, "inE")) {
			    Boolean movBack  = ((cellMsg) x.getValOnPort("inE", i)).movBack;
			    if (movBack == false)
			        path.push("West");
			    stat.visitedCells.elementAt(xcoord).set(ycoord, 1);
                holdIn("actor", 1);
            }
		}
	}

	public void deltint() {
	    if (!deadEnd) {
	        if (xcoord == xEntrance && ycoord == yEntrance) {
	            holdIn("entrance", INFINITY);
	        }  
	       
	        else {
	            holdIn("path", INFINITY);
	        }
	    }
	    //if we saw all the surroundings around the entrance - so we coudn't find the exit
	    else if (xcoord == xEntrance && ycoord == yEntrance) {
	        System.out.println("I COULD NOT FIND THE EXIT");
	        holdIn(phase, INFINITY);
	    } else {
	        deadEnd = false;
	        movingBack = true;
	        stepBack(path.pop());
	    }
	}
	
	public void stepBack(String direction) {
	    switch(direction) {
        case "North":
            stat.visitedCells.elementAt(xcoord).set(ycoord + 1, 0);
            break;
    	case "South":
    	    stat.visitedCells.elementAt(xcoord).set(ycoord - 1, 0);
            break;
    	case "East":
    	    stat.visitedCells.elementAt(xcoord - 1).set(ycoord, 0);
            break;
    	case "West":
    	    stat.visitedCells.elementAt(xcoord + 1).set(ycoord, 0);
            break;
    }
	    
	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}
	
	public boolean couldGoNorth() {
	    if (ycoord > 0 && stat.visitedCells.elementAt(xcoord).elementAt(ycoord - 1) != 1 && 
	        stat.visitedCells.elementAt(xcoord).elementAt(ycoord - 1) != 2) {
	        return true;
	    }
	    return false;
	}
	
	public boolean couldGoEast() {
        if (xcoord < stat.visitedCells.elementAt(0).size() - 1 && stat.visitedCells.elementAt(xcoord + 1).elementAt(ycoord) != 1 &&
            stat.visitedCells.elementAt(xcoord + 1).elementAt(ycoord) != 2) {
            return true;
        }
        return false;
    }
	
	public boolean couldGoSouth() {
        if (ycoord < stat.visitedCells.size() - 1 && stat.visitedCells.elementAt(xcoord).elementAt(ycoord + 1) != 1 && 
            stat.visitedCells.elementAt(xcoord).elementAt(ycoord + 1) != 2) {
            return true;
        }
        return false;
    }
	
	public boolean couldGoWest() {
	    if (xcoord > 0 && stat.visitedCells.elementAt(xcoord - 1).elementAt(ycoord) != 1 &&
	        stat.visitedCells.elementAt(xcoord - 1).elementAt(ycoord) != 2){
	        return true;
	    }
        return false;
    }

	public message out() {
		message m = new message();
		if (phaseIs("actor") && couldGoNorth()) {
		    m.add(makeContent("outN", new cellMsg("cellMsg", movingBack)));
		}
		else if (phaseIs("actor") && couldGoEast()) {
		    m.add(makeContent("outE", new cellMsg("cellMsg", movingBack)));
		}
		else if (phaseIs("actor") && couldGoSouth()) {
		    m.add(makeContent("outS", new cellMsg("cellMsg", movingBack)));
		}
		else if (phaseIs("actor") && couldGoWest()) {
		    m.add(makeContent("outW", new cellMsg("cellMsg", movingBack)));
		}
		else
		    deadEnd = true;
		return m;
	}

}
