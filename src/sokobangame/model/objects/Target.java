package sokobangame.model.objects;

import java.util.List;

import sokobangame.model.Maze;
import sokobangame.model.MazeObject;

/** The object for floor-panel-like targets. All Crates must be on top of (in the same tile as) a Target for the game to be won. */
public class Target extends MazeObject {
	
	public Target(Maze maze) {
		super(maze);
		layer = LAYER_FLOOR; //Targets are on the floor - they don't get in the way of some other objects, and are rendered below most other objects.
	}

	public boolean canOccupy(int x, int y) {
		List<MazeObject> list = maze.getObjects(x, y);
		return list.size() == 0; //target cannot be placed on a space when anything else is in that space already
	}

}
