package sokobangame.controller;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sokobangame.controller.modes.CreateArrowMode;
import sokobangame.controller.modes.CreateCrateMode;
import sokobangame.controller.modes.CreatePlayerMode;
import sokobangame.controller.modes.CreateTargetMode;
import sokobangame.controller.modes.CreateWallMode;
import sokobangame.controller.modes.EditMode;
import sokobangame.controller.modes.Mode;
import sokobangame.controller.modes.PlayMode;
import sokobangame.model.Maze;
import sokobangame.view.MazeView;


/**
 * This is (effectively) the application state model, responsible for the currently selected application mode.
 * As the currently selected object is only relevant to the Edit mode, it is stored in the instance of EditMode this manager creates.
 * 
 * Manages the modes of the game, keeping track of the current mode and informing modes when the current mode changes.
 * Implements the state pattern, in standing in as various listeners to serve its current mode. Also gives its modes access to parts of the view.
 * 
 * Finally, implements the Observable pattern, accepting ModeManagerListeners.
 * */

public class ModeManager implements ActionListener, MouseListener, MouseMotionListener {
	protected final List<Mode> modes;
	protected final Map<String, Mode> modeNameMap;
	protected final List<ModeManagerListener> listeners;
	protected final Maze maze;
	protected final MazeView mazeView;
	protected Mode currentMode;
	protected ModeToolBar toolBar;
	
	public ModeManager(Maze maze, MazeView mazeView) {
		this.maze = maze;
		this.mazeView = mazeView;
		modes = new ArrayList<Mode>();
		listeners = new ArrayList<ModeManagerListener>();
		modeNameMap = new HashMap<String, Mode>();
		
		Mode editMode = new EditMode(this);
		addMode(editMode);
		Mode wallMode = new CreateWallMode(this);
		addMode(wallMode);
		Mode crateMode = new CreateCrateMode(this);
		addMode(crateMode);
		Mode arrowMode = new CreateArrowMode(this);
		addMode(arrowMode);
		Mode targetMode = new CreateTargetMode(this);
		addMode(targetMode);
		Mode playerMode = new CreatePlayerMode(this);
		addMode(playerMode);
		Mode playMode = new PlayMode(this, playerMode);
		addMode(playMode);
		
		setCurrentMode(wallMode);
	}
	
	/** Get the maze to which this mode manager pertains. */
	public Maze getMaze() {
		return maze;
	}
	
	/** Get the maze view to which this mode manager pertains. */
	public MazeView getMazeView() {
		return mazeView;
	}
	
	public void setToolBar(ModeToolBar toolBar) {
		this.toolBar = toolBar;
		//refresh the current mode so the tool bar will show it as selected
		fireCurrentModeChanged(currentMode);
	}
	
	public ModeToolBar getToolBar() {
		return toolBar;
	}
	
	public void addMode (Mode mode) {
		modes.add(mode);
		modeNameMap.put(mode.getName(), mode);
	}
	
	public void addListener (ModeManagerListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener (ModeManagerListener listener) {
		listeners.remove(listener);
	}
	
	/** Changes the game's mode, informs modes when they've just been entered or exited, and informs listeners of the change. */
	public void setCurrentMode(Mode mode) {		
		if (mode != currentMode) {
			if (currentMode != null)
				currentMode.modeExited();
			
			currentMode = mode;
			fireCurrentModeChanged(mode);
			mode.modeEntered();
		} else {
			fireCurrentModeRefreshed(mode);
			mode.modeRefreshed();
		}
		
		//repaint the whole maze view because this saves the mode a lot of effort either way (it's not like this happens really regularly)
		mazeView.repaint();
	}

	private void fireCurrentModeChanged(Mode mode) {
		for (ModeManagerListener l : listeners)
			l.currentModeChanged(mode);
	}
	
	private void fireCurrentModeRefreshed(Mode mode) {
		for (ModeManagerListener l : listeners)
			l.currentModeRefreshed(mode);
	}
	
	/** Gets the game's current mode. */
	public Mode getCurrentMode() {
		return currentMode;
	}
	
	/** Get an iterator over the sequence of modes (for doing something to every mode, such as listing them) */
	public Iterator<Mode> getModesIterator() {
		return modes.iterator();
	}
	
	//Swing objects can change the current mode if the mode manager is listening to them, and they give a mode's name as their action command.
	public void actionPerformed(ActionEvent a) {
		Mode m = modeNameMap.get(a.getActionCommand());
		assert(m != null);
		setCurrentMode(m);
	}
	
	public void paint(Graphics2D g2d) {
		currentMode.paint(g2d);
	}
	
	//MouseListener/MouseMotionListener event methods, passed on to the current mode.
	public void mouseDragged(MouseEvent e) {currentMode.mouseDragged(e);}

	public void mouseMoved(MouseEvent e) {currentMode.mouseMoved(e);}

	public void mouseClicked(MouseEvent e) {currentMode.mouseClicked(e);}

	public void mouseEntered(MouseEvent e) {currentMode.mouseEntered(e);}

	public void mouseExited(MouseEvent e) {currentMode.mouseExited(e);}

	public void mousePressed(MouseEvent e) {currentMode.mousePressed(e);}

	public void mouseReleased(MouseEvent e) {currentMode.mouseReleased(e);}
	
}
