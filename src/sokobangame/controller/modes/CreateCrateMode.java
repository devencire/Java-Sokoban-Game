package sokobangame.controller.modes;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.model.objects.Crate;

public class CreateCrateMode extends AbstractCreateMode {

	public CreateCrateMode(ModeManager modeManager) {
		super(modeManager);
	}
	
	public String getName() {
		//return "Create Crate";
		return "+ Crate";
	}

	protected boolean attemptObjectCreation(Maze maze, int tileX, int tileY) {
		return maze.addObject(new Crate(maze), tileX, tileY, false);
	}
	
	protected String getFailureDialogText() {
		return "A crate may only be placed in empty spaces or on top of targets.";
	}

}
