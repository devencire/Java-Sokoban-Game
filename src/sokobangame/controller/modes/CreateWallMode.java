package sokobangame.controller.modes;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.model.objects.Wall;

public class CreateWallMode extends AbstractCreateMode {

	public CreateWallMode(ModeManager modeManager) {
		super(modeManager);
	}
	
	public String getName() {
		//return "Create Wall";
		return "+ Wall";
	}
	
	protected boolean attemptObjectCreation(Maze maze, int tileX, int tileY) {
		return maze.addObject(new Wall(maze), tileX, tileY, false);
	}
	
	protected String getFailureDialogText() {
		return "A wall cannot be placed in the same tile as any other object.";
	}

}
