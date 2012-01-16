package sokobangame.model;

import java.awt.Point;

import sokobangame.model.objects.Player;

/**
 * MazeObject is an abstract class that all objects in the Maze extend. The only method subclasses must implement is canOccupy.
 * All MazeObjects have a layer. An object's layer should not change during the object's lifetime.
 * A MazeObject has a position in its Maze, stored in a field of the MazeObject itself for efficiency.
 * 		When the Maze places or moves a MazeObject, the MazeObject's internal knowledge of its position is updated.
 * 		Only Maze methods should change the MazeObject's position fields, to maintain the invariant that:
 * 			a MazeObject's position fields match where the Maze stores the MazeObject in its objects store.
 * 
 * It has default behaviour for saving and loading object state, and responding to the player's attempts to move.
 * Complex objects will need to override this behaviour.
 */
public abstract class MazeObject {
	/** The layer that floor-panel-type objects reside in, e.g. targets. */
	public static final int LAYER_FLOOR = 0;
	/** The layer containing the player, and everything that gets in the player's way (so, everything but targets). */
	public static final int LAYER_OBSTACLE = 1;
	
	/** Simple enumeration of directions that maps each direction to a x/y offset. */
	public enum Direction {
		UP(0,-1), DOWN(0,1), LEFT(-1,0), RIGHT(1,0);
		private final int x,y;
		Direction (int x, int y) {
			this.x = x; this.y = y;
		}
		public int getX() {return x;}
		public int getY() {return y;}
	};
	
	protected int layer = LAYER_OBSTACLE;
	private int x, y;
	private int savedX, savedY;
	protected Maze maze;
	
	protected MazeObject (Maze maze) {
		this.maze = maze;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	/** IMPORTANT: This should only be called by the Maze. As such, it is package-private. Updates the object's own knowledge of its position. */
	void updatePos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/** Returns whether the object can occupy the given coordinates. */
	public abstract boolean canOccupy(int x, int y);
	
	/**
	 * Saves the current state of the object, to be restored later.
	 * Objects with variable state while the game is being played should override this to save more than just position.
	 * (then use super.saveState to save position)
	 */
	public void saveState() {
		savedX = x;
		savedY = y;
	}
	
	/**
	 * Loads the saved state of the object (must re-add into position through the maze method).
	 * Objects with variable state while the game is being played should override this to load more than just position
	 * (then use super.loadState to load position)
	 */
	public void loadState() {
		maze.addObject(this, savedX, savedY, true);
	}
	
	/** Returns the layer this object resides in. */
	public final int getLayer() {
		return layer;
	}
	
	/** Called when the object has been inserted into the maze (after it's been confirmed the insertion is valid). */
	public void justInserted() {}

	/** Called when the object has been removed from the maze. */
	public void justRemoved() {}
	
	/**
	 * Called when the player tries to move into this object's tile.
	 * The returned Point is where the player will now attempt to go.
	 * As such, returning the passed destination is the 'continue unchanged' option.
	 * This method does not affect whether the player can occupy this object's tile should it not be directed to a different destination.
	 */
	public Point modifyPlayerMovement(Player player, MazeObject.Direction direction, Point destination) {
		return destination;
	}
	
}
