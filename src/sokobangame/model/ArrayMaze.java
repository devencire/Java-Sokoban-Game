package sokobangame.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sokobangame.model.objects.Crate;
import sokobangame.model.objects.Player;
import sokobangame.model.objects.Target;

/** An implementation of the Maze interface that uses a 3-dimensional array of LinkedLists to store the Maze's objects.
 *  The arrays (at least theoretically) allow fast implementations of the getObjects and getObjectsInLayer methods,
 *  	by avoiding scanning the entire set of Maze objects. */
public class ArrayMaze implements Maze {
	
	private final int width, height;
	private static final int layercount = 2;
	
	/**
	 * Objects are stored in a 4-dimensional space (x,y,layer,(tile list index)).
	 * This allows every two-dimensional position to contain multiple layers,
	 * 		and every layer to contain multiple objects (though this is not used with the current game rules).
	 * It also provides a reasonable order for processing objects:
	 * 		lowest to highest layer,
	 * 		lowest y coordinate to highest,
	 * 		lowest x coordinate to highest.
	 * (So the entire first layer is earlier before the second layer,
	 *  	the first column of the first layer is earlier before the second column, etc.)
	 *  The iterator can easily do an in-place sorted traversal of the Maze's objects.
	 */
	protected final List<MazeObject>[][][] objects;
	
	protected Player player;
	
	protected List<MazeListener> listeners;
	private boolean gameWon;
	
	@SuppressWarnings("unchecked")
	public ArrayMaze (int width, int height) {
		this.width = width;
		this.height = height;
		//we can't actually make a generic array, but we can pretend to
		objects = (List<MazeObject>[][][]) new LinkedList<?>[width][height][layercount];
		
		listeners = new ArrayList<MazeListener>();
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<MazeObject> getObjectsInLayer(int x, int y, int layer) {
		List<MazeObject> list = objects[x][y][layer];
		if (list == null) {
			list = new LinkedList<MazeObject>();
			objects[x][y][layer] = list;
		}
		return list;
	}

	public List<MazeObject> getObjects(int x, int y) {		
		//functionally, this is 'flatten (objects[x][y])'.
		List<MazeObject> returnList = new LinkedList<MazeObject>();
		List<MazeObject>[] lists = objects[x][y];
		for (int layer = 0; layer < layercount; layer++) {
			if (lists[layer] != null)
				returnList.addAll(lists[layer]);
		}
		return returnList;
	}
	
	public Iterator<MazeObject> getObjectsIterator() {
		// TODO Auto-generated method stub
		return new MazeIterator();
	}
	
	public class MazeIterator implements Iterator<MazeObject> {
		private int x = -1;
		private int y,layer = 0;
		private Iterator<MazeObject> currentIterator;
		private MazeObject nextObject;
		
		//Iterates through the three-dimensional array for iterators, from which it gets the next value.
		public boolean hasNext() {
			while (currentIterator == null || !currentIterator.hasNext()) {
				x++;
				if (x >= width) {
					x = 0; y++;
				}
				if (y >= height) {
					y = 0; layer++;
				}
				if (layer >= layercount) return false;
				List<MazeObject> list = objects[x][y][layer];
				if (list != null)
					currentIterator = list.iterator();
			}
			nextObject = currentIterator.next();
			return true;
		}

		public MazeObject next() {
			MazeObject o = nextObject;
			return o;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	/** (INTERNAL) Insert the object into the maze's data, at the given x/y position. */
	private void insertObjectIntoMazeData(MazeObject o, int x, int y) {
		int layer = o.getLayer();
		List<MazeObject> list = objects[x][y][layer];
		if (list == null) {
			list = new LinkedList<MazeObject>();
			objects[x][y][layer] = list;
		}
		list.add(o);
		fireMazeTileUpdated(x, y);
	}
	
	/** (INTERNAL) Remove the object from the maze's data, if it's there. */
	private void removeObjectFromMazeData(MazeObject o) {
		objects[o.getX()][o.getY()][o.getLayer()].remove(o);
		fireMazeTileUpdated(o.getX(), o.getY());
	}
	
	public boolean isInMaze (int x, int y) {
		return (x >= 0 && x < width && y >= 0 && y < height);
	}
	
	/** Checks whether the object can be there (in terms of maze boundaries and other objects) */
	protected boolean placementAllowed(MazeObject o, int x, int y) {
		return (isInMaze(x,y) && o.canOccupy(x, y));
	}
	
	public boolean addObject(MazeObject o, int x, int y, boolean ignoreConstraints) {
		//check if position in layer is free and object is okay with the placement
		if (!ignoreConstraints && !placementAllowed(o, x, y))
			return false;
		else {
			//update the maze
			insertObjectIntoMazeData(o, x, y);
			//update the object's knowledge of its position
			o.updatePos(x, y);
			//tell the object it's been inserted into the maze
			o.justInserted();
			return true;
		}
	}
	
	public boolean moveObject(MazeObject o, int x, int y) {
		//check if position in layer is free and object is okay with the move
		if (!placementAllowed(o, x, y)) {
			return false;
		} else {
			//check a move is even necessary
			if (o.getX() == x && o.getY() == y) return true;
			//update the maze
			removeObjectFromMazeData(o);
			insertObjectIntoMazeData(o, x, y);
			//update the object's knowledge of its position
			o.updatePos(x, y);
			return true;
		}
	}
	
	public void removeObject(MazeObject o) {
		// remove from the maze's knowledge
		removeObjectFromMazeData(o);
		// tell the object it's been removed from the maze
		o.justRemoved();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void saveMazeState() {
		Iterator<MazeObject> iter = getObjectsIterator();
		while (iter.hasNext()) {
			MazeObject obj = iter.next();
			obj.saveState(); //gets the object to store knowledge of what it is now and where, for restoration later
		}
	}
	
	public void loadMazeState() {
		/*
		 * This is not pretty, because we can't modify the maze while iterating through it.
		 * So we get a list of everything, remove everything, then reintroduce it.
		 * The maze will probably contain a few hundred objects in the worst case, and this only happens on mode transitions...
		 * So it'll do.
		 */
		
		gameWon = false;
		
		List<MazeObject> list = new ArrayList<MazeObject>();
		Iterator<MazeObject> iter = getObjectsIterator();
		while (iter.hasNext()) {
			MazeObject obj = iter.next();
			list.add(obj);
		}
		
		for (MazeObject o : list) {
			removeObjectFromMazeData(o);
			o.loadState();
		}
	}
	
	public void addMazeListener(MazeListener listener) {
		listeners.add(listener);
	}
	
	public void removeMazeListener(MazeListener listener) {
		listeners.remove(listener);
	}
	
	public void informOfObjectUpdate(MazeObject o) {
		fireMazeTileUpdated(o.getX(), o.getY());
	}
	
	protected void fireMazeTileUpdated(int x, int y) {
		for (MazeListener listener : listeners)
			listener.mazeTileUpdated(this, x, y);
	}
	
	public void checkWinCondition() {
		Iterator<MazeObject> iter = getObjectsIterator();
		while (iter.hasNext()) {
			MazeObject obj = iter.next();
			if (obj instanceof Crate) {
				boolean gotTarget = false;
				for (MazeObject otherOccupant : getObjects(obj.getX(), obj.getY())) {
					if (otherOccupant instanceof Target) {
						gotTarget = true;
					}
				}
				if (!gotTarget) {
					gameWon = false;
					return;
				}
			}
		}
		//we got to here? The player has won.
		gameWon = true; //we record this so the win condition isn't checked unnecessarily after it is reached
		fireGameWon();		
	}
	
	protected void fireGameWon() {
		for (MazeListener listener : listeners)
			listener.gameWon(this);
	}
	
	public boolean isInWinningState() {
		return gameWon;
	}

}
