package sokobangame.view;

import java.awt.Color;
import java.awt.Graphics2D;

import sokobangame.model.MazeObject;
import sokobangame.model.objects.Arrow;

public class ArrowPainter implements MazeObjectPainter {
	public static final ArrowPainter INSTANCE = new ArrowPainter();
	
	public void paint(Graphics2D g, MazeObject o, MazeView mazeView) {
		Arrow arrow = (Arrow)o;
		
		g.setColor(Color.BLACK);
		//origin (centre of tile)
		int ox = (int)((arrow.getX() + 0.5) * mazeView.TILE_WIDTH);
		int oy = (int)((arrow.getY() + 0.5) * mazeView.TILE_HEIGHT);
		//a unit distance
		int dist = (int)(mazeView.TILE_WIDTH * 0.4);
		//transformation matrix values
		int x1, y1, x2, y2;
		
		switch (arrow.getDirection()) {
		case UP:
			x1 = 1;	x2 = 0;	y1 = 0;	y2 = -1;
			break;
		case DOWN:
			x1 = 1;	x2 = 0;	y1 = 0;	y2 = 1;
			break;
		case LEFT:
			x1 = 0;	x2 = 1;	y1 = -1; y2 = 0;
			break;
		default:
			x1 = 0;	x2 = -1; y1 = 1; y2 = 0;
			break;
		}
		//a kludging of the above values into an arrow
		g.drawLine(ox + (x2) * dist, oy - (y2) * dist, ox - (x2) * dist, oy + (y2) * dist);
		g.drawLine(ox - (x2) * dist, oy + (y2) * dist, ox + (x1) * dist, oy + (y1) * dist);
		g.drawLine(ox - (x2) * dist, oy + (y2) * dist, ox - (x1) * dist, oy - (y1) * dist);
	}

}
