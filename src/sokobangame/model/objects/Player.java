package sokobangame.model.objects;

import java.awt.Point;
import java.util.List;

import sokobangame.model.Maze;
import sokobangame.model.MazeObject;

/** The player object. Controllers can have the player attempt to move in any of the four cardinal directions. */
public class Player extends MazeObject {
	
	protected Direction direction = Direction.LEFT;
	protected Direction savedDirection;
	
	public Player(Maze maze) {
		super(maze);
		layer = LAYER_OBSTACLE;
	}

	public boolean canOccupy(int x, int y) {		
		//if there is already a player, we don't want another one
		if (maze.getPlayer() != null && maze.getPlayer() != this)
			return false;
		
		List<MazeObject> list = maze.getObjectsInLayer(x, y, layer);
		return list.size() == 0;
	}
	
	public void justInserted() {
		//we must be the one and only player of this maze
		maze.setPlayer(this);
	}
	
	public void justRemoved() {
		//the maze must no longer have a player
		maze.setPlayer(null);
	}
	
	/*
	 * This method implements behaviour that was clarified to be incorrect after the code was written; whilst it would be easy enough to
	 * remove the loop and use of the oldDirection fields, thus preventing 'chain' movements,
	 * 		this would make this method (and MazeObject.modifyPlayerMovement) look rather strangely placed.
	 * So, this code has been left doing what it originally did, which DOES allow 'chain' movement through arrows/into crates.
	 */
	public void attemptDirectionalMove(Direction direction) throws InfiniteMoveException {
		this.direction = direction;
		//even if we get no further, we want the player's new direction to be painted
		maze.informOfObjectUpdate(this);
		
		Point destination = new Point(direction.getX() + getX(), direction.getY() + getY());
		Point oldDestination = new Point(-1,-1);
		
		//this loop allows any object in the destination square (lowest layer first) to redirect the player's move
		//if they do so, objects in the new destination square are checked, etc.
		//this allows a chain of move modifications, e.g. players can surf rows of arrows, and push blocks on the other side of arrows
		int i = 0; //infinite loop protection (not possible with the current objects, but...)
		
		while (!(oldDestination.equals(destination)) && i < 1000) {
			if (!maze.isInMaze((int)destination.getX(), (int)destination.getY())) return;
			
			oldDestination = (Point) destination.clone();
			for (MazeObject obj : maze.getObjects((int)destination.getX(), (int)destination.getY())) {
				destination = obj.modifyPlayerMovement(this, direction, destination);
			}
			
			i++;
		}
		
		if (i == 1000) throw new InfiniteMoveException();
		
		if (maze.moveObject(this, (int)destination.getX(), (int)destination.getY())) { //if the move happens
			maze.checkWinCondition();
		}
	}
	
	public Direction getDirection () {
		return direction;
	}
	
	public void saveState() {
		super.saveState();
		savedDirection = direction;
	}
	
	public void loadState() {
		super.loadState();
		direction = savedDirection;
	}
	
	public class InfiniteMoveException extends Exception {

	}

}
