package sokobangame.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import sokobangame.model.MazeObject;
import sokobangame.model.objects.Wall;

public class WallPainter implements MazeObjectPainter {
	public static final WallPainter INSTANCE = new WallPainter();

	public void paint(Graphics2D g, MazeObject o, MazeView mazeView) {
		Wall wall = (Wall)o;
		
		g.setColor(Color.BLACK);
		g.fill(new Rectangle2D.Double(wall.getX() * mazeView.TILE_WIDTH, wall.getY() * mazeView.TILE_HEIGHT,
				mazeView.TILE_WIDTH, mazeView.TILE_HEIGHT));
		
		g.setColor(Color.WHITE);
		
		String label = wall.getLabel();
		if (label != null) {
			Font oldFont = g.getFont();
			g.setFont(oldFont.deriveFont((float)(mazeView.TILE_HEIGHT * 0.7)));
			
			/*g.drawString(label, (int)((wall.getX() + 0.3) * mazeView.TILE_WIDTH),
					(int)((wall.getY() + 0.6) * mazeView.TILE_HEIGHT));*/
			mazeView.drawCenteredString(label, (int)((wall.getX() + 0.5) * mazeView.TILE_WIDTH),
					(int)((wall.getY() + 0.5) * mazeView.TILE_HEIGHT), g);
			
			g.setFont(oldFont);
		}
	}

}
