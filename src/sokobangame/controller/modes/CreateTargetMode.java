package sokobangame.controller.modes;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.model.objects.Target;

public class CreateTargetMode extends AbstractCreateMode {

	public CreateTargetMode(ModeManager modeManager) {
		super(modeManager);
	}

	public String getName() {
		//return "Create Target";
		return "+ Target";
	}
	
	protected boolean attemptObjectCreation(Maze maze, int tileX, int tileY) {
		return maze.addObject(new Target(maze), tileX, tileY, false);
	}
	
	protected String getFailureDialogText() {
		return "A target may not be placed on top of another object.";
	}

}
