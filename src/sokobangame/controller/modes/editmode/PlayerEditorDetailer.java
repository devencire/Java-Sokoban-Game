package sokobangame.controller.modes.editmode;

import sokobangame.controller.modes.EditMode;
import sokobangame.model.MazeObject;

public class PlayerEditorDetailer implements MazeObjectEditorDetailer {
	public static final PlayerEditorDetailer INSTANCE = new PlayerEditorDetailer();

	public void populateToolbar(EditMode mode, MazeObject o) {
		//do nothing - the player has no state worth editing other than position
	}

	public String getInvalidMoveDialogText() {
		return "A player may not be moved on top of anything other than a target.";
	}

}
