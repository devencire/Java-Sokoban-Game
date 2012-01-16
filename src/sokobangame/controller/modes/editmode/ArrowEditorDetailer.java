package sokobangame.controller.modes.editmode;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JToolBar;

import sokobangame.controller.modes.EditMode;
import sokobangame.model.MazeObject;
import sokobangame.model.MazeObject.Direction;
import sokobangame.model.objects.Arrow;

public class ArrowEditorDetailer extends ComboBoxUsingEditorDetailer {
	public static ArrowEditorDetailer INSTANCE = new ArrowEditorDetailer();

	public void populateToolbar(final EditMode mode, MazeObject o) {
		final Arrow arrow = (Arrow)o;
		
		Map<String, Direction> entryMap = new HashMap<String, Direction>();
		entryMap.put("Left", Direction.LEFT);
		entryMap.put("Right", Direction.RIGHT);
		entryMap.put("Up", Direction.UP);
		entryMap.put("Down", Direction.DOWN);
		
		Direction startOption = arrow.getDirection();
		
		MethodHolder<Direction> methodHolder = new MethodHolder<Direction>() {
			public void method(Direction dir) {
				arrow.setDirection(dir);
				mode.repaintObject(arrow);
			}
		};
		
		final JComboBox comboBox = createComboBox(entryMap, startOption, methodHolder);
		
		mode.getToolBar().addComponentFromSource(this, new JToolBar.Separator());
		mode.getToolBar().addComponentFromSource(this, comboBox);
	}

	public String getInvalidMoveDialogText() {
		return "An arrow cannot be moved on top of anything else.";
	}

}
