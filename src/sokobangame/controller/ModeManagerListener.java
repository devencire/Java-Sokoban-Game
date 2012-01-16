package sokobangame.controller;

import sokobangame.controller.modes.Mode;


/** The listener interface for things wishing to receive information about changes to the application's mode. */
public interface ModeManagerListener {
	
	/**
	 * Notifies the listener that the current mode has changed.
	 * @param mode				The new current mode.
	 */
	public void currentModeChanged(Mode mode);
	public void currentModeRefreshed(Mode mode);
	
}
