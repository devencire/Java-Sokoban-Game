package sokobangame.controller;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import sokobangame.controller.modes.Mode;

/** 
 * A JToolBar that provides toggle buttons for selecting the current mode from the modes given by a ModeManager.
 * Also provides a special method for adding components, allowing them to be removed later by only identifying their source.
 **/
public class ModeToolBar extends JToolBar {
	
	protected Map<Object,HashSet<JComponent>> componentsFromSources = new HashMap<Object, HashSet<JComponent>>();
	
	public ModeToolBar(ModeManager modeManager) {
		super();
		addSeparator();
		add(new JLabel("Mode: "));
		
		for (Iterator<Mode> iter = modeManager.getModesIterator(); iter.hasNext(); ) {
			addSeparator();
			Mode m = iter.next();
			
			final ModeToggleButton button = new ModeToggleButton(m);
			
			//the button's action command is used by the ModeManager to work out what mode's button has been pressed
			button.addActionListener(modeManager);
			
			//the button listens to the ModeManager for mode changes
			modeManager.addListener(button);
			
			add(button);
		}
		addSeparator(new Dimension(25,1));
		
		modeManager.setToolBar(this);
	}
	
	/** Adds to the tool bar a component and associates it with the source object. */
	public void addComponentFromSource (Object source, JComponent component) {
		HashSet<JComponent> set = componentsFromSources.get(source);
		
		if (set == null) {
			set = new HashSet<JComponent>();
			componentsFromSources.put(source, set);
		}
		
		set.add(component);
		add(component);
		
		doLayout();
		component.doLayout();
		invalidate();
		repaint();
	}
	
	/** Removes from the tool bar all components associated with the source object. */
	public void removeComponentsFromSource (Object source) {
		HashSet<JComponent> set = componentsFromSources.get(source);
		if (set == null) return; //nothing to get rid of, nothing to do.
		
		//remove the components from the tool bar
		for (JComponent comp : set) {
			remove(comp);
		}
		
		//remove the source's set from the map
		componentsFromSources.remove(source);
		
		invalidate();
		repaint();
	}
	
	/** A JButton that listens to mode changes. */
	private class ModeToggleButton extends JToggleButton implements ModeManagerListener {
		private Mode mode;
		public ModeToggleButton (Mode mode) {
			super(mode.getName());
			this.mode = mode;
			setActionCommand(mode.getName());
		}
		public void currentModeChanged(Mode currentMode) {
			setSelected(currentMode == mode);
		}
		public void currentModeRefreshed(Mode mode) {
			currentModeChanged(mode);
		}
	}
	
}
