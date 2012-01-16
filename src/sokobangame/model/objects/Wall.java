package sokobangame.model.objects;

import java.util.List;

import sokobangame.model.Maze;
import sokobangame.model.MazeObject;

/** The object representing immovable walls, that cannot be under or on top of any object. Can have a label, which does not affect anything in the domain model. */
public class Wall extends MazeObject {
	
	protected String label;
	
	public Wall (Maze maze) {
		super(maze);
		layer = LAYER_OBSTACLE;
	}
	
	public boolean canOccupy(int x, int y) {
		List<MazeObject> list = maze.getObjects(x, y);
		return list.size() == 0; // Walls cannot occupy a space containing anything else.
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
		maze.informOfObjectUpdate(this);
	}
	
}
