package sokobangame.controller.modes.editmode;

import sokobangame.controller.modes.EditMode;
import sokobangame.model.MazeObject;

public class TargetEditorDetailer implements MazeObjectEditorDetailer {
	public static final TargetEditorDetailer INSTANCE = new TargetEditorDetailer();

	public void populateToolbar(EditMode mode, MazeObject o) {
		//do nothing - the target has no state to change other than position
	}

	public String getInvalidMoveDialogText() {
		return "A target cannot be moved on top of anything else.";
	}

}
