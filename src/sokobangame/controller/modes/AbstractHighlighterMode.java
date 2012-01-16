package sokobangame.controller.modes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import sokobangame.controller.ModeManager;
import sokobangame.view.MazeView;
import sokobangame.view.MazeView.OutsideOfMazeException;

/**
 *  A Mode sub-classing this will draw a tile highlighter when the user moves their mouse around the maze view.
 * If the paint method is overridden, the tile highlighter can still be painted using the paintSquareHighlighter method,
 */
public abstract class AbstractHighlighterMode extends Mode {

	/** Dashed line strokes, out of phase from each other. */
	protected static final Stroke DASHED_STROKE = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[] { 2f, 2f }, 0f);
	protected static final Stroke DASHED_STROKE_OFFSET = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[] { 2f, 2f }, 2f);
	
	private int lastMouseTileX = -1;
	private int lastMouseTileY = -1;

	protected AbstractHighlighterMode(ModeManager modeManager) {
		super(modeManager);
	}

	public void mouseMoved(MouseEvent e) {
		MazeView view = getMazeView();
		try {
			Point pt = view.getTilePosition(e.getX(), e.getY());
			int tileX = (int) pt.getX();
			int tileY = (int) pt.getY();
			
			if (tileX != lastMouseTileX || tileY != lastMouseTileY) {
				//view.repaint(lastMouseTileX * MazeView.TILE_WIDTH, lastMouseTileY * MazeView.TILE_HEIGHT, MazeView.TILE_WIDTH, MazeView.TILE_HEIGHT);
				repaintTile(lastMouseTileX, lastMouseTileY);
				lastMouseTileX = tileX;
				lastMouseTileY = tileY;
				repaintTile(tileX, tileY);
			}
		} catch (OutsideOfMazeException ex) {
			if (lastMouseTileX != -1)
				mouseExited(e);
			return;
		}
	}

	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	public void mouseExited(MouseEvent e) {
		if (lastMouseTileX == -1) return; //if we don't have a last position, we have nothing to forget/repaint over
		//when the mouse exits, forget we were ever there, and repaint over where our selection square was
		int tileX = lastMouseTileX;
		int tileY = lastMouseTileY;
		lastMouseTileX = -1;
		lastMouseTileY = -1;
		repaintTile(tileX, tileY);
	}

	public void paint(Graphics2D g) {
		getMazeView().paintTileHighlighter(g,lastMouseTileX,lastMouseTileY);
	}

}