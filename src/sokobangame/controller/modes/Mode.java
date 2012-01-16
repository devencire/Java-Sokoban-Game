package sokobangame.controller.modes;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import sokobangame.controller.ModeManager;
import sokobangame.controller.ModeToolBar;
import sokobangame.model.Maze;
import sokobangame.model.MazeObject;
import sokobangame.view.MazeView;

/** A mode of the game. Listens for mode changes. Abstract to serve as an adapter for MouseListener and MouseMotionListener, so each mode need not implement every method of those interfaces. */
public abstract class Mode implements MouseListener, MouseMotionListener {
	
	protected ModeManager modeManager;
	
	protected Mode (ModeManager modeManager) {
		this.modeManager = modeManager;
	}
	
	/** The name of the mode, as should be displayed on toolbars and suchlike. */
	public abstract String getName();
	
	/** Called when this mode is entered (implementing this is not compulsory). */
	public void modeEntered() {};
	
	/** Called when this mode is reselected whilst it is still the current mode (implementation not compulsory). */
	public void modeRefreshed() {};
	
	/** Called when this mode is exited (implementing this is not compulsory). */
	public void modeExited() {};
	
	/** Called when the maze view repaints, so the mode can draw over the maze. */
	public abstract void paint(Graphics2D g2d);
	
	//convenience methods
	/** Gets the maze view the mode is working within. */
	public MazeView getMazeView() {
		return modeManager.getMazeView();
	}
	
	/** Gets the maze the mode is working on. */
	public Maze getMaze() {
		return modeManager.getMaze();
	}
	
	/** Gets the mode tool bar the mode can add to to give context-specific options. */
	public ModeToolBar getToolBar() {
		return modeManager.getToolBar();
	}
	
	/** Asks the maze view to repaint the given tile. */
	public void repaintTile (int x, int y) {
		//getMazeView().repaint(x * MazeView.TILE_WIDTH, y * MazeView.TILE_HEIGHT, MazeView.TILE_WIDTH, MazeView.TILE_HEIGHT);
		getMazeView().repaintTile(x,y);
	}
	
	/** Asks the maze view to repaint the given object. */
	public void repaintObject (MazeObject o) {
		repaintTile(o.getX(),o.getY());
	}
	
	//MouseListener/MouseMotionListener event methods.
	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
	
}
