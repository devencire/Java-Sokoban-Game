package sokobangame.model;

import java.util.Iterator;
import java.util.List;

import sokobangame.model.objects.Player;

/**
 * Multiple layers of a two-dimensional grid, where each (x,y,layer) position is associated with zero or more MazeObjects.
 * No MazeObject appears more than once in a Maze (and an object only has one position and exists in only one maze).
 * Provides operations for adding, moving and removing MazeObjects, that update MazeObjects with their new positions when they are moved.
 * Additionally, it can have its MazeObjects save their state, have its MazeObjects request being moved into their previously saved state,
 * 		and detect when the game has been won.
 * Finally, it implements the Observable pattern, in accepting MazeListeners.
 */
public interface Maze {

	/** Get the width of the maze, in tiles. */
	public abstract int getWidth();

	/** Get the height of the maze, in tiles. */
	public abstract int getHeight();

	/** Get the objects at the given x/y position (a.k.a. tile) in the given layer. */
	public abstract List<MazeObject> getObjectsInLayer(int x, int y, int layer);

	/** Get the objects at the given x/y position (a.k.a. tile) as a list ordered by layer (lowest first). May return an empty list. */
	public abstract List<MazeObject> getObjects(int x, int y);

	/** Provides an iterator that iterates over the objects of the maze, ordered by layer (lowest first). */
	public abstract Iterator<MazeObject> getObjectsIterator();

	/** Checks whether the given tile position refers to a tile within the maze's boundaries. */
	public abstract boolean isInMaze(int x, int y);

	/** Adds an object at the given x/y, if the placement is allowed (or if ignoreConstraints is true).
	 * Returns true if the object has been added, false otherwise. */
	public abstract boolean addObject(MazeObject o, int x, int y, boolean ignoreConstraints);

	/** Moves an object to the given x/y position, if the move is allowed. Returns true if the object has been moved, false otherwise. */
	public abstract boolean moveObject(MazeObject o, int x, int y);

	/** Remove the object from the maze. */
	public abstract void removeObject(MazeObject o);

	/** Sets the player object. */
	public abstract void setPlayer(Player player);

	/** Gets the player object, or null if no player. */
	public abstract Player getPlayer();

	/** Saves everything about the maze, for later restoration using loadMazeState. */
	public abstract void saveMazeState();

	/** Loads everything about the maze that was saved using saveMazeState. */
	public abstract void loadMazeState();
	
	/** Adds a listener to maze events (when a tile's contents change, and when the game has been won). */
	public abstract void addMazeListener(MazeListener listener);
	
	/** Removes a listener to maze events. */
	public abstract void removeMazeListener(MazeListener listener);
	
	/** Objects call this when their state changes. At the very least, the Maze should inform listeners that the object's tile has changed. */
	public abstract void informOfObjectUpdate(MazeObject o);

	/** Checks the win condition (typically called during play after the player makes a move) */
	public abstract void checkWinCondition();
	
	/** Returns whether the game has been won yet. */
	public abstract boolean isInWinningState();

}