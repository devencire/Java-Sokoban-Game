package sokobangame.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import sokobangame.model.MazeObject;
import sokobangame.model.objects.Target;

public class TargetPainter implements MazeObjectPainter {
	public static final TargetPainter INSTANCE = new TargetPainter();

	public void paint(Graphics2D g, MazeObject o, MazeView mazeView) {
		Target target = (Target)o;
		
		g.setColor(Color.MAGENTA);
		g.fill(new Ellipse2D.Float(
				(float)((target.getX() + 0.25) * mazeView.TILE_WIDTH), (float)((target.getY() + 0.25) * mazeView.TILE_HEIGHT),
				(float)(mazeView.TILE_WIDTH * 0.5), (float)(mazeView.TILE_HEIGHT * 0.5)));
	}

}
