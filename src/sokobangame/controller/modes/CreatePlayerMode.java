package sokobangame.controller.modes;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.model.objects.Player;

public class CreatePlayerMode extends AbstractCreateMode {

	public CreatePlayerMode(ModeManager modeManager) {
		super(modeManager);
	}

	public String getName() {
		//return "Create Player";
		return "+ Player";
	}
	
	protected boolean attemptObjectCreation(Maze maze, int tileX, int tileY) {
		return maze.addObject(new Player(maze), tileX, tileY, false);
	}
	
	protected String getFailureDialogText() {
		return "A player may only be placed in empty spaces or on top of targets, and only one may be placed.";
	}

}
