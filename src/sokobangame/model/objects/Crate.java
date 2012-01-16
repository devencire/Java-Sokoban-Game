package sokobangame.model.objects;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import sokobangame.model.Maze;
import sokobangame.model.MazeObject;

/** The object representing pushable crates. When the player moves into a crate, the crate will move in the same direction if possible.
 * Every crate in the maze must be on top of a Target for the game to be won.
 */
public class Crate extends MazeObject {
	//I considered using a custom enum type so that this part of the model wasn't importing java.awt.Color, but it seemed like overkill
	protected Color color = Color.GREEN;
	
	public Crate(Maze maze) {
		super(maze);
		layer = LAYER_OBSTACLE;
	}
	
	public boolean canOccupy(int x, int y) {
		List<MazeObject> list = maze.getObjectsInLayer(x, y, layer);
		return list.size() == 0; //Crates cannot occupy a space when anything else is in that space and layer already (so targets are fine)
	}
	
	public void setColor(Color color) {
		this.color = color;
		maze.informOfObjectUpdate(this);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Point modifyPlayerMovement(Player player, Direction direction, Point destination) {
		//the player can't move here while this crate is here... but the crate may be able to move, so we just try it
		maze.moveObject(this, getX() + direction.getX(), getY() + direction.getY());
		
		return destination;
	}
	
}
