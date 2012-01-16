package sokobangame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sokobangame.controller.ModeManager;
import sokobangame.controller.ModeToolBar;
import sokobangame.model.ArrayMaze;
import sokobangame.model.Maze;
import sokobangame.view.MazeView;

public class ApplicationFrame extends JFrame {

	protected final Maze maze;
	protected final MazeView mazeView;
	
	/** A frame that glues the game's components together into a coherent application. */
	public ApplicationFrame () {
		super("Sokoban Game");
		
		//The Maze is the domain model of the program. It knows nothing of the view and controller.
		maze = new ArrayMaze(20,10);
		
		//The MazeView constitutes most of the view of the program, drawing the maze and win screen.
		//It also allows the ModeManager to request selection boxes be drawn.
		mazeView = new MazeView(maze);
		
		//The ModeManager is a controller associated with the MazeView, and (in essence) the application state model.
		//It keeps track of the current Mode, and allows the current Mode to interface with the Maze and MazeView.
		//It has minimal knowledge of the MazeView, allowing it to:
		// - detect when the user is using the mouse to interact with the MazeView, and
		// - request that selection boxes be drawn in the MazeView, or that the MazeView be redrawn to clear away such selection boxes.
		ModeManager modeManager = mazeView.getModeManager();
		
		JPanel contentPane = new JPanel(new BorderLayout());
		
		//The ModeToolBar is a part of the view separate from the MazeView, showing the current Mode of the application.
		//It is also a controller to the ModeManager, allowing the user to select from any of the application's Modes.
		//Finally, it allows Modes (through the ModeManager) to create Mode-specific tool-bar options (used by EditMode).
		contentPane.add(BorderLayout.NORTH, new ModeToolBar(modeManager));
		
		contentPane.add(BorderLayout.CENTER, mazeView);
		
		setContentPane(contentPane);
		
		pack();
	}

	public static void main(String[] args) {
		ApplicationFrame applicationFrame = new ApplicationFrame();
		applicationFrame.setLocation(100, 100);
		applicationFrame.setVisible(true);
		
		//For some reason this program doesn't terminate when closed without a nudge, so this is necessary.
		applicationFrame.addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				windowClosing(e);
			}

			public void windowClosing(WindowEvent e) {
				System.exit(NORMAL);
			}
			
		});
	}

}
