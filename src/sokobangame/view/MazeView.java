package sokobangame.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import sokobangame.controller.ModeManager;
import sokobangame.model.Maze;
import sokobangame.model.MazeListener;
import sokobangame.model.MazeObject;
import sokobangame.model.objects.Arrow;
import sokobangame.model.objects.Crate;
import sokobangame.model.objects.Player;
import sokobangame.model.objects.Target;
import sokobangame.model.objects.Wall;

@SuppressWarnings("serial")
/** The core of the view of the game. Draws the maze (using MazeObjectPainters), selection boxes when requested,
 * and the win screen when appropriate. */
public class MazeView extends JPanel implements MazeListener {
	protected int TILE_WIDTH = 40;
	protected int TILE_HEIGHT = 40;
	
	// A registry approach links MazeObjects in the model to their painters, so the model need not know about the view at all.
	protected static final Map<Class<? extends MazeObject>, MazeObjectPainter> painters = new HashMap<Class<? extends MazeObject>, MazeObjectPainter>();
	
	static {
		painters.put(Wall.class, WallPainter.INSTANCE);
		painters.put(Crate.class, CratePainter.INSTANCE);
		painters.put(Target.class, TargetPainter.INSTANCE);
		painters.put(Arrow.class, ArrowPainter.INSTANCE);
		painters.put(Player.class, PlayerPainter.INSTANCE);
	}
	
	protected final Maze maze;
	protected final ModeManager modeManager;
	
	protected boolean gameHasBeenWon;
	
	public MazeView (Maze maze) {
		this.maze = maze;
		maze.addMazeListener(this);
		
		modeManager = new ModeManager(maze, this);
		addMouseListener(modeManager);
		addMouseMotionListener(modeManager);
		
		setPreferredSize(new Dimension(maze.getWidth() * TILE_WIDTH, maze.getHeight() * TILE_HEIGHT));
		
		//we listen to component resizes - and moves, for if the tool-bar is pulled off of the frame - and resize the maze accordingly
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				adjustTileSize();
			}
			
			public void componentMoved(ComponentEvent arg0) {
				adjustTileSize();
			}
		});
	}
	
	/** Adjusts the tile-size based on the size of the panel
	 * (which depends on the size of the application frame, and whether the tool-bar is present in the frame). */
	protected void adjustTileSize() {
		TILE_WIDTH = Math.min(getWidth() / maze.getWidth(), getHeight() / maze.getHeight());
		TILE_HEIGHT = TILE_WIDTH;
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g; 
		// Keep the old state of g2d safe.
		Color oldColor = g2d.getColor();
		Shape oldClip = g2d.getClip();
		Stroke oldStroke = g2d.getStroke();

		// Background
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, maze.getWidth() * TILE_WIDTH, maze.getHeight() * TILE_HEIGHT);
		
		// Maze objects
		Iterator<MazeObject> iter = maze.getObjectsIterator(); //given to us in layer order already
		while (iter.hasNext()) {
			MazeObject o = iter.next();
			painters.get(o.getClass()).paint(g2d, o, this);
		}
		
		// Mode-specific drawing
		modeManager.paint(g2d);
		
		// Win screen (possibly)
		if (maze.isInWinningState()) {
			drawWinScreen(g2d);						
		}
		
		// Restore the old state of g2d.
		g2d.setColor(oldColor);
		g2d.setClip(oldClip);
		g2d.setStroke(oldStroke);
	}
	
	/** Draws the win screen when paintComponent requires it. */
	private void drawWinScreen(Graphics2D g2d) {
		
		int bufferSize = (int) Math.min((getWidth() * 0.1), (getHeight() * 0.2));
		int arcSize = (int) (bufferSize * 0.25);
		
		g2d.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
		g2d.fillRoundRect(bufferSize, bufferSize, (int)(getWidth() - bufferSize * 2), (int)(getHeight() - bufferSize * 2), arcSize, arcSize);
		
		g2d.setColor(Color.WHITE);
		g2d.setStroke(new BasicStroke((float) (bufferSize * 0.08)));
		g2d.drawRoundRect((int)(bufferSize * 1.15), (int)(bufferSize * 1.15), (int)(getWidth() - bufferSize * 2.3), (int)(getHeight() - bufferSize * 2.3), arcSize, arcSize);
		
		Font oldFont = g2d.getFont();
		g2d.setFont(g2d.getFont().deriveFont((float)(Math.min(getWidth() * 0.1, getHeight() * 0.2))).deriveFont(Font.BOLD));
		drawCenteredString("Game won!", (int)(getWidth() * 0.5), (int)(getHeight() * 0.5), g2d);
		g2d.setFont(oldFont);
		
	}
	
	/** Draws a string centred on the given coordinates. */
	public void drawCenteredString(String s, int x, int y, Graphics g2d) {
	    FontMetrics fm = g2d.getFontMetrics();
	    x = x - (fm.stringWidth(s) / 2);
	    y = y + (fm.getAscent() - fm.getDescent()) / 2;
	    g2d.drawString(s, x, y);
	  }
	
	/** Takes tile coordinates and returns pixel coordinates for the top-left corner of the tile (relative to the panel's own position). */
	public Point getTilePosition(int x, int y) throws OutsideOfMazeException {
		if (x < 0 || y < 0 || x >= maze.getWidth() * TILE_WIDTH || y >= maze.getHeight() * TILE_HEIGHT)
			throw new OutsideOfMazeException();
		else
			return new Point(x / TILE_WIDTH, y / TILE_HEIGHT); 
	}
	
	/** A (hardly necessary) exception, with a name that should make it clear what's going on. */
	public class OutsideOfMazeException extends Exception {
		
	}
	
	protected static final Stroke DASHED_STROKE = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[] { 2f, 2f }, 0f);
	protected static final Stroke DASHED_STROKE_OFFSET = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[] { 2f, 2f }, 2f);
	
	/** Paints a dotted line around a tile, to indicate the tile is selected. */
	public void paintTileHighlighter(Graphics2D g, int tileX, int tileY) {
		int x1 = tileX * TILE_WIDTH;
		int y1 = tileY * TILE_HEIGHT;
		int x2 = x1 + TILE_WIDTH-1;
		int y2 = y1 + TILE_HEIGHT-1;
		Color oldColor = g.getColor();
		g.setColor(Color.BLACK);
		Stroke oldStroke = g.getStroke();
		g.setStroke(DASHED_STROKE);
		drawBox(g,x1,y1,x2,y2);
		
		g.setColor(Color.WHITE);
		g.setStroke(DASHED_STROKE_OFFSET);
		drawBox(g,x1,y1,x2,y2);
		
		g.setStroke(oldStroke);
		g.setColor(oldColor);
	}
	
	protected void drawBox(Graphics2D g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y1);
		g.drawLine(x2, y1, x2, y2);
		g.drawLine(x1, y1, x1, y2);
		g.drawLine(x1, y2, x2, y2);
	}
	
	public ModeManager getModeManager() {
		return modeManager;
	}

	//MazeListener methods - we listen so we can repaint tiles affected by maze content changes
	public void mazeTileUpdated(Maze maze, int tileX, int tileY) {
		repaintTile(tileX, tileY);
	}
	
	public void gameWon(Maze maze) {
		repaint(); //the Game Won! screen takes up most of this panel's area
	}
	
	/** Requests the tile at the given coordinates be repainted.
	 * It's not necessary for this to be called to render newly created or moved objects - the MazeView listens to the Maze for these changes. */
	public void repaintTile(int x, int y) {
		repaint(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
	}
}
