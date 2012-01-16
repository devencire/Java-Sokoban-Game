package sokobangame.controller.modes.editmode;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComboBox;

/** An abstract class that provides subclasses with a helper function for making useful JComboBoxes. */
public abstract class ComboBoxUsingEditorDetailer implements MazeObjectEditorDetailer {

	/**
	 * Creates a JComboBox containing the keys of the entryMap as options,
	 * 		that starts with the String selected that maps to the startOption object,
	 * 		and that passes references to Objects mapped to from the selected String to the given MethodHolder.
	 */
	public <S> JComboBox createComboBox (final Map<String,S> entryMap, S startOption, final MethodHolder<S> methodHolder) {
		final JComboBox comboBox = new JComboBox();
		
		for (Map.Entry<String, S> pair : entryMap.entrySet()) {
			comboBox.addItem(pair.getKey());
			if (pair.getValue() == startOption)
				comboBox.setSelectedItem(pair.getKey());
		}
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				S obj = entryMap.get(comboBox.getSelectedItem());
				methodHolder.method(obj);
			}
		});
		
		comboBox.setMaximumSize(new Dimension(100,75));
		
		return comboBox;
	}
	
	public interface MethodHolder<S> {
		void method(S arg);
	}

}
