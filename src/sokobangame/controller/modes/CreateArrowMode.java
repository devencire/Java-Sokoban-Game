package sokobangame.controller.modes;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.model.objects.Arrow;

public class CreateArrowMode extends AbstractCreateMode {

	public CreateArrowMode(ModeManager modeManager) {
		super(modeManager);
	}

	public String getName() {
		//return "Create Arrow";
		return "+ Arrow";
	}
	
	protected boolean attemptObjectCreation(Maze maze, int tileX, int tileY) {
		return maze.addObject(new Arrow(maze), tileX, tileY, false);
	}
	
	protected String getFailureDialogText() {
		return "An arrow cannot be placed in the same tile as any other object.";
	}

}
