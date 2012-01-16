package sokobangame.controller.modes.editmode;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JToolBar;

import sokobangame.controller.modes.EditMode;
import sokobangame.model.MazeObject;
import sokobangame.model.objects.Crate;

public class CrateEditorDetailer extends ComboBoxUsingEditorDetailer {
	public static final CrateEditorDetailer INSTANCE = new CrateEditorDetailer();	
	
	public void populateToolbar(final EditMode mode, MazeObject o) {
		final Crate crate = (Crate)o;
		
		Map<String, Color> entryMap = new HashMap<String, Color>();
		entryMap.put("Red", Color.RED);
		entryMap.put("Green", Color.GREEN);
		entryMap.put("Blue", Color.BLUE);
		
		Color startOption = crate.getColor();
		
		MethodHolder<Color> methodHolder = new MethodHolder<Color>() {
			public void method(Color col) {
				crate.setColor(col);
				mode.repaintObject(crate);
			}
		};
		
		final JComboBox comboBox = createComboBox(entryMap, startOption, methodHolder);
		
		mode.getToolBar().addComponentFromSource(this, new JToolBar.Separator());
		mode.getToolBar().addComponentFromSource(this, comboBox);
	}
	
	public String getInvalidMoveDialogText() {
		return "A crate may not be moved on top of anything other than a target.";
	}

}
