package sokobangame.view;

import java.awt.Graphics2D;

import sokobangame.model.MazeObject;

/** The (minimal) interface for classes that provide paint methods for MazeObjects. */
public interface MazeObjectPainter {
	void paint(Graphics2D g, MazeObject o, MazeView mazeView);
}
