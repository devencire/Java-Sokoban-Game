package sokobangame.controller.modes;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.view.MazeView;
import sokobangame.view.MazeView.OutsideOfMazeException;

/**
 * Uses the template pattern to allow for minimal implementations of the various create modes
 * 		(with attemptObjectCreation as a template method).
 */
public abstract class AbstractCreateMode extends AbstractHighlighterMode {

	protected AbstractCreateMode(ModeManager modeManager) {
		super(modeManager);
	}

	public void mouseReleased(MouseEvent e) {
		MazeView view = getMazeView();
		try {
			Point pt = view.getTilePosition(e.getX(), e.getY());
			int tileX = (int) pt.getX();
			int tileY = (int) pt.getY();
		
			Maze maze = getMaze();
			if (!attemptObjectCreation(maze, tileX, tileY)) { //if is not successful
				JOptionPane.showMessageDialog(modeManager.getMazeView(), getFailureDialogText(), "Invalid placement", JOptionPane.WARNING_MESSAGE);
			}
		} catch (OutsideOfMazeException ex) {
			
		}
	}
	
	abstract protected boolean attemptObjectCreation(Maze maze, int tileX, int tileY);
	
	protected String getFailureDialogText() {
		//default behaviour for when the subclass provides nothing better
		return "The object cannot be placed here.";
	}

}