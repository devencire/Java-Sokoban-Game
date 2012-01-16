package sokobangame.controller.modes.editmode;

import sokobangame.controller.modes.EditMode;
import sokobangame.model.MazeObject;

/** An interface for classes providing object-specific dialog text and tool-bar components for editing a MazeObject in the EditMode.
 * Uses generics for the convenience of its implementations. */
public interface MazeObjectEditorDetailer {
	
	/** Add additional options to the toolbar (using mode.addToToolBar) to let the user modify the maze object. */
	void populateToolbar(EditMode mode, MazeObject o);
	
	/** The error message to show when the user attempts to move the maze object to an invalid position. */
	public String getInvalidMoveDialogText();
	
}
