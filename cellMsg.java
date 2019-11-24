package M011_CellularAutomataWithObstacles;

import GenCol.entity;

public class cellMsg extends entity
{
    protected boolean movBack;
    
    public cellMsg(String name, boolean movBack_) {
        this.name = name;
        movBack = movBack_;
    }
}
