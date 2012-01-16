package sokobangame.model.objects;

import java.awt.Point;
import java.util.List;

import sokobangame.model.Maze;
import sokobangame.model.MazeObject;

/** The object representing push-arrows in the maze.
 * Arrows act as walls unless the player moves onto them in the direction of the arrow,
 * 		in which case they will modify the player's move to try and end on the other side of the arrow from the player. */
public class Arrow extends MazeObject {
	
	protected Direction direction = Direction.RIGHT;
	
	public Arrow(Maze maze) {
		super(maze);
		layer = LAYER_OBSTACLE; //Targets are on the floor - they don't get in the way of some other objects, and are rendered below most other objects.
	}

	public boolean canOccupy(int x, int y) {
		List<MazeObject> list = maze.getObjects(x, y);
		return list.size() == 0; // Arrows cannot occupy a space containing anything else.
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
		maze.informOfObjectUpdate(this);
	}
	
	public Point modifyPlayerMovement(Player player, Direction playerDirection, Point destination) {
		if (playerDirection == this.direction) {
			destination.translate(direction.getX(), direction.getY());
		}
		return destination;
	}

}
