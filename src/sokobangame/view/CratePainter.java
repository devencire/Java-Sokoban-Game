package sokobangame.view;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import sokobangame.model.MazeObject;
import sokobangame.model.objects.Crate;

public class CratePainter implements MazeObjectPainter {
	public static final CratePainter INSTANCE = new CratePainter();

	public void paint(Graphics2D g, MazeObject o, MazeView mazeView) {
		Crate crate = (Crate)o;
		
		g.setColor(crate.getColor());
		//has to be floats for the corners to round nicely, it seems
		g.fill(new RoundRectangle2D.Float(
				(float)((crate.getX() + 0.15) * mazeView.TILE_WIDTH), (float)((crate.getY() + 0.15) * mazeView.TILE_HEIGHT),
				(float)(mazeView.TILE_WIDTH * 0.7), (float)(mazeView.TILE_HEIGHT * 0.7),
				(float)(mazeView.TILE_WIDTH * 0.3), (float)(mazeView.TILE_WIDTH * 0.3)));
	}

}
