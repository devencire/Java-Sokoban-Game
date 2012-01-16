package sokobangame.controller.modes;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.model.MazeListener;
import sokobangame.model.MazeObject.Direction;
import sokobangame.model.objects.Player;
import sokobangame.model.objects.Player.InfiniteMoveException;
import sokobangame.view.MazeView;

/** 
 * Entering this mode starts the game, from which point this mode listens for key input from the PageView.
 * The PlayMode instructs the player object to attempt moves, but is not responsible for game logic.
 * When entered, asks the maze to save its state. When exited or refreshed, asks the maze to restore its state.
 */
public class PlayMode extends Mode implements MazeListener {
	
	protected Mode createPlayerMode;
	
	protected boolean stateWillNeedRestoring = false;
	protected boolean gameHasBeenWon;
	
	protected KeyAdapter keyAdapter = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			keyPressedInMaze(e);
		}
	};
	
	public PlayMode(ModeManager modeManager, Mode createPlayerMode) {
		super(modeManager);
		this.createPlayerMode = createPlayerMode;
	}

	public String getName() {
		return "Play";
	}
	
	public void modeEntered() {
		Maze maze = getMaze();
		
		if (maze.getPlayer() != null) {
			maze.saveMazeState();
			stateWillNeedRestoring = true;
			
			MazeView mazeView = getMazeView();
			mazeView.addKeyListener(keyAdapter);
			mazeView.requestFocusInWindow();
			
			gameHasBeenWon = false;
			maze.addMazeListener(this);
		} else { //else we have no player - and should complain
			JOptionPane.showMessageDialog(modeManager.getMazeView(), "You must create a player in the maze before playing the game.", "Missing player", JOptionPane.WARNING_MESSAGE);
			modeManager.setCurrentMode(createPlayerMode);
		}
	}
	
	public void modeRefreshed() {
		//reset stuff
		gameHasBeenWon = false;
		
		getMaze().loadMazeState();
		getMazeView().requestFocusInWindow();
	}
	
	public void modeExited() {
		if (stateWillNeedRestoring)
			getMaze().loadMazeState();
		getMazeView().removeKeyListener(keyAdapter);
	}
	
	public void keyPressedInMaze(KeyEvent e) {
		if (gameHasBeenWon) return; //don't allow the player to move once the game is over
		
		Player player = getMaze().getPlayer();
		Direction direction;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			direction = Direction.UP;
			break;
		case KeyEvent.VK_DOWN:
			direction = Direction.DOWN;
			break;
		case KeyEvent.VK_LEFT:
			direction = Direction.LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			direction = Direction.RIGHT;
			break; 
		default:
			return; //skip out of the method here if we didn't get an arrow key press
		}
		try {
			player.attemptDirectionalMove(direction);
		} catch (InfiniteMoveException ex) {
			//humility in the face of a broken game ;(
			JOptionPane.showMessageDialog(modeManager.getMazeView(), "Something's went wrong - the game got stuck in a loop trying to resolve this move. Sorry.", "Help, I'm stuck in a loop!", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void paint(Graphics2D g2d) {
		//we paint nothing during play - the MazeView has the Maze to inform it about painting
	}
	
	//MazeListener methods
	public void gameWon(Maze maze) {
		gameHasBeenWon = true;
	}
	
	public void mazeTileUpdated(Maze maze, int tileX, int tileY) {}

}
