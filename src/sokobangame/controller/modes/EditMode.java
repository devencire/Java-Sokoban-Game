package sokobangame.controller.modes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import sokobangame.controller.ModeManager;
import sokobangame.controller.ModeToolBar;
import sokobangame.controller.modes.editmode.ArrowEditorDetailer;
import sokobangame.controller.modes.editmode.CrateEditorDetailer;
import sokobangame.controller.modes.editmode.MazeObjectEditorDetailer;
import sokobangame.controller.modes.editmode.PlayerEditorDetailer;
import sokobangame.controller.modes.editmode.TargetEditorDetailer;
import sokobangame.controller.modes.editmode.WallEditorDetailer;
import sokobangame.model.Maze;
import sokobangame.model.MazeObject;
import sokobangame.model.objects.Arrow;
import sokobangame.model.objects.Crate;
import sokobangame.model.objects.Player;
import sokobangame.model.objects.Target;
import sokobangame.model.objects.Wall;
import sokobangame.view.MazeView;
import sokobangame.view.MazeView.OutsideOfMazeException;

/**
 * Allows selecting and dragging objects, and adds a 'Delete' button to the tool-bar when the mode is open.
 * Also allows for object-specific tool-bar components, if the object maps to an MazeObjectEditorDetailer in the editorDetailers Map.
 */
public class EditMode extends AbstractHighlighterMode {
	
	protected MazeObject selectedObject;
	protected JButton deleteButton;
	protected JLabel label;
	
	protected MazeObjectEditorDetailer detailer;
	
	protected boolean isDragging;
	
	//A registry approach links MazeObjects in the model with objects that provide various object-specific details to the editor.
	protected static final Map<Class<? extends MazeObject>, MazeObjectEditorDetailer> editorDetailers =
			new HashMap<Class<? extends MazeObject>, MazeObjectEditorDetailer>();
	
	static {
		editorDetailers.put(Crate.class,CrateEditorDetailer.INSTANCE);
		editorDetailers.put(Wall.class,WallEditorDetailer.INSTANCE);
		editorDetailers.put(Target.class, TargetEditorDetailer.INSTANCE);
		editorDetailers.put(Arrow.class, ArrowEditorDetailer.INSTANCE);
		editorDetailers.put(Player.class, PlayerEditorDetailer.INSTANCE);
	}
	
	public EditMode(ModeManager modeManager) {
		super(modeManager);
		label = new JLabel("Edit: ");
		deleteButton = new JButton("Delete");
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				getMaze().removeObject(selectedObject);
				deselectMazeObject();
			}
		});
	}
	
	public String getName() {
		return "Edit";
	}
	
	public void modeEntered() {
		// Add the always-relevant things to the tool-bar
		ModeToolBar toolBar = getToolBar();
		toolBar.addComponentFromSource(this, label);
		toolBar.addComponentFromSource(this, deleteButton);
		
		// If an object was selected when we last left this mode, refresh that selection
		if (selectedObject != null)
			selectMazeObject(selectedObject);
	}
	
	public void modeExited() {
		// Remove everything we (and our editor-detailer) added to the tool-bar.
		ModeToolBar toolBar = getToolBar();
		toolBar.removeComponentsFromSource(this);
		toolBar.removeComponentsFromSource(detailer);
	}
	
	public void mousePressed(MouseEvent e) {
		deselectMazeObject();
		MazeView view = getMazeView();
		try {
			
			Point pt = view.getTilePosition(e.getX(), e.getY());
			int tileX = (int) pt.getX();
			int tileY = (int) pt.getY();
			Maze maze = getMaze();
			List<MazeObject> list = maze.getObjects(tileX, tileY);
			if (list.size() != 0)
				selectMazeObject(list.get(list.size()-1)); //get the last (so highest layer, last to paint) element
			
		} catch (OutsideOfMazeException ex) {
			//let this go silently - it is enough that the user now has no selected object
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (selectedObject != null) {
			super.mouseDragged(e);
			isDragging = true;
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		isDragging = false;
		if (selectedObject != null) {
			
			int oldX = selectedObject.getX();
			int oldY = selectedObject.getY();
			
			Point pt;
			try {
				pt = getMazeView().getTilePosition(e.getX(), e.getY());
				int tileX = (int) pt.getX();
				int tileY = (int) pt.getY();
				
				if (tileX != oldX || tileY != oldY) { //if the move would result in a change of position
					
					if (!getMaze().moveObject(selectedObject, tileX, tileY)) {
						MazeObjectEditorDetailer detailer = editorDetailers.get(selectedObject.getClass());
						if (detailer != null)
							JOptionPane.showMessageDialog(modeManager.getMazeView(), detailer.getInvalidMoveDialogText(), "Invalid move", JOptionPane.WARNING_MESSAGE);
					}
					
				}
			} catch (OutsideOfMazeException e1) {
				
			}
			
		}
	}
	
	/**
	 * Selects a maze object;
	 * shows the user the object is now selected, and adds tool-bar components if an editor-detailer provides them.
	 */
	public void selectMazeObject (MazeObject o) {
		selectedObject = o;
		repaintObject(o); //will now paint the selection highlighter box for the first time
		
		deleteButton.setEnabled(true);
		
		//this is slightly dirty use of generics, I admit...
		detailer = editorDetailers.get(o.getClass());
		if (detailer != null) {
			detailer.populateToolbar(this, o);
		}
	}
	
	/**
	 * Deselects a maze object;
	 * shows the user the object is no longer selected, and removes its tool-bar components.
	 */
	public void deselectMazeObject () {
		MazeObject oldObject = selectedObject;
		if (oldObject != null) {
			selectedObject = null;
			repaintObject(oldObject); //will now clean off the selection highlighter box
		}
		
		deleteButton.setEnabled(false);
		getToolBar().removeComponentsFromSource(detailer);
		detailer = null;
	}
	
	public void paint (Graphics2D g) {
		if (selectedObject != null)
			getMazeView().paintTileHighlighter(g,selectedObject.getX(),selectedObject.getY());
		if (isDragging)
			super.paint(g);
	}

}
