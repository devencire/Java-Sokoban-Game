package sokobangame.model;

/** The listener interface for receiving notable events in the Maze - tiles changing or the maze entering a winning state. */
public interface MazeListener {
	
	/** Notifies the listener that something has changed about the given maze tile. */
	public void mazeTileUpdated(Maze maze, int tileX, int tileY);
	
	/** Notifies the listener that the maze has entered a winning state. */
	public void gameWon(Maze maze);
	
}
