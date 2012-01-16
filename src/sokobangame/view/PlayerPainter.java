package sokobangame.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import sokobangame.model.MazeObject;
import sokobangame.model.objects.Player;

public class PlayerPainter implements MazeObjectPainter {
	public static final PlayerPainter INSTANCE = new PlayerPainter();
	
	public void paint(Graphics2D g, MazeObject o, MazeView mazeView) {
		Player player = (Player)o;
		
		Ellipse2D ellipse = new Ellipse2D.Float(
				(float)((player.getX() + 0.15) * mazeView.TILE_WIDTH), (float)((player.getY() + 0.15) * mazeView.TILE_HEIGHT),
				(float)(mazeView.TILE_WIDTH * 0.7), (float)(mazeView.TILE_HEIGHT * 0.7));
		
		g.setColor(Color.WHITE);
		g.fill(ellipse);
		
		g.setColor(Color.GREEN.darker().darker());
		g.draw(ellipse);
		
		int ox = (int) ((player.getX() + 0.5) * mazeView.TILE_WIDTH);
		int oy = (int) ((player.getY() + 0.5) * mazeView.TILE_HEIGHT);
		g.drawLine(ox, oy,
				ox + (int)(mazeView.TILE_WIDTH * 0.35 * player.getDirection().getX()),
				oy + (int)(mazeView.TILE_HEIGHT * 0.35 * player.getDirection().getY()));
	}

}
