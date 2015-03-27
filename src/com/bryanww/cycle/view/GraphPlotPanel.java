package com.bryanww.cycle.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JPanel;

import com.bryanww.cycle.model.ConvexHull;
import com.bryanww.cycle.model.Cycle;
import com.bryanww.cycle.model.CycleLayer;
import com.bryanww.cycle.model.Graph;
import com.bryanww.cycle.model.Vertex;

/**
 * Panel that overrides {@link #paintComponent(Graphics)} to provide
 * {@link Graph} view rendering.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2015-04-27
 */
public class GraphPlotPanel extends JPanel {
	
	private static final long serialVersionUID = 317302138753026166L;
	
	public static final float  POINT_RADIUS            = 7.0f;
	public static final Stroke SOLID_STROKE            = new BasicStroke(1.0f);
	public static final Stroke DASH_STROKE             = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 8.0f, new float[] {8.0f, 4.0f}, 0.0f);
	public static final Stroke DOTTED_STROKE           = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4.0f, new float[] {2.0f, 8.0f}, 0.0f);
	public static final float  MOUSE_TEXT_OFFSET_X     = 6.0f;
	public static final float  MOUSE_TEXT_OFFSET_Y     = 18.0f;
	public static final Font   MOUSE_TEXT_FONT         = new Font("monospaced", Font.BOLD, 14);
	public static final String MOUSE_TEXT_PATTERN      = "(%.3f, %.3f)";
	
	public static final Color  DARK_BACKGROUND_COLOR   = Color.BLACK;
	public static final Color  DARK_GRID_COLOR         = Color.DARK_GRAY;
	public static final Color  DARK_VERTEX_COLOR       = Color.CYAN;
	public static final Color  DARK_CONVEX_HULL_COLOR  = new Color(96, 96, 96);
	public static final Color  DARK_NEW_CYCLE_COLOR    = new Color(0, 255, 0);
	public static final Color  DARK_CYCLE_COLOR        = new Color(255, 0, 0);
	public static final Color  DARK_COORDINATE_COLOR   = Color.WHITE;
	public static final Color  LIGHT_BACKGROUND_COLOR  = Color.WHITE;
	public static final Color  LIGHT_GRID_COLOR        = Color.LIGHT_GRAY;
	public static final Color  LIGHT_VERTEX_COLOR      = Color.BLUE;
	public static final Color  LIGHT_CONVEX_HULL_COLOR = new Color(160, 160, 160);
	public static final Color  LIGHT_NEW_CYCLE_COLOR   = new Color(128, 0, 0);
	public static final Color  LIGHT_CYCLE_COLOR       = new Color(0, 128, 0);
	public static final Color  LIGHT_COORDINATE_COLOR  = Color.BLACK;
	
	protected GraphPanel        graphPanel;  // the reference to the parent {@link GraphPanel}
	
	protected float             mouseX;      // the most recently tracked mouse X coordinate over the plot area
	protected float             mouseY;      // the most recently tracked mouse X coordinate over the plot area
	
	protected Rectangle2D.Float rectangle;   // the Rectangle2D instance used to render the background (and other rectangles)
	protected Ellipse2D.Float   ellipse;     // the Ellipse2D instance used to render points
	protected Path2D.Float      path;        // the Path2D instance used to render edges
	
	/**
	 * Creates a new {@link GraphPlotPanel}.
	 * @param graphPanel the reference to the parent {@link GraphPanel}
	 */
	public GraphPlotPanel(GraphPanel graphPanel) {
		this.graphPanel = graphPanel;
		this.rectangle  = new Rectangle2D.Float();
		this.ellipse    = new Ellipse2D.Float();
		this.path       = new Path2D.Float();
		initUIComponents();
		addUIListeners();
	}
	
	/**
	 * Returns the reference to the {@link GraphPanel}.
	 * @return the reference to the {@link GraphPanel}
	 */
	public GraphPanel getGraphPanel() {
		return graphPanel;
	}
	
	/**
	 * Initializes UI components.
	 */
	protected void initUIComponents() {
		setFocusable(true);
	}
	
	/**
	 * Adds UI listeners.
	 */
	protected void addUIListeners() {
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent event) {
				Point point = event.getPoint();
				if (point != null) {
					mouseX = (float) (point.getX() / getBounds().getWidth());
					mouseY = (float) (point.getY() / getBounds().getHeight());
					repaint();
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent event) {
				mouseMoved(event);
			}
		});
	}
	
	/**
	 * Returns the scaled display x-coordinate to render the normalized x-coordinate, with respect to the panel width.
	 * @param x the normalized x-coordinate to calculate the display x-coordinate for
	 * @return the scaled display x-coordinate to render the normalized x-coordinate, with respect to the panel width
	 */
	protected float getDisplayX(float x) {
		return (float) (x * getBounds().getWidth());
	}
	
	/**
	 * Returns the scaled display x-coordinate to render the normalized x-coordinate, with respect to the panel width.
	 * @param vertex the vertex containing the normalized x-coordinate to calculate the display x-coordinate for
	 * @return the scaled display x-coordinate to render the normalized x-coordinate, with respect to the panel width
	 */
	protected float getDisplayX(Vertex vertex) {
		return getDisplayX(vertex.getX());
	}
	
	/**
	 * Returns the scaled display y-coordinate to render the normalized y-coordinate, with respect to the panel height.
	 * @param y the normalized y-coordinate to calculate the display y-coordinate for
	 * @return the scaled display y-coordinate to render the normalized y-coordinate, with respect to the panel height
	 */
	protected float getDisplayY(float y) {
		return (float) (y * getBounds().getHeight());
	}
	
	/**
	 * Returns the scaled display y-coordinate to render the normalized y-coordinate, with respect to the panel height.
	 * @param vertex the vertex containing the normalized y-coordinate to calculate the display y-coordinate for
	 * @return the scaled display y-coordinate to render the normalized y-coordinate, with respect to the panel height
	 */
	protected float getDisplayY(Vertex vertex) {
		return getDisplayY(vertex.getY());
	}
	
	/**
	 * Renders the plot area background.
	 * @param g2d the {@link Graphics2D} reference
	 */
	protected void drawBackground(Graphics2D g2d) {
		Color color = graphPanel.getGraphToolbarPanel().isDisplayingDark() ? DARK_BACKGROUND_COLOR : LIGHT_BACKGROUND_COLOR;
		g2d.setColor(color);
		rectangle.setRect(0.0, 0.0, getBounds().getWidth(), getBounds().getHeight());
		g2d.fill(rectangle);
	}
	
	/**
	 * Renders the plot area grid.
	 * @param g2d the {@link Graphics2D} reference
	 */
	protected void drawGrid(Graphics2D g2d) {
		if (graphPanel.getGraphToolbarPanel().isDisplayingGrid()) {
			
			Color color = graphPanel.getGraphToolbarPanel().isDisplayingDark() ? DARK_GRID_COLOR : LIGHT_GRID_COLOR;
			g2d.setColor(color);
			g2d.setStroke(DOTTED_STROKE);
			
			path.reset();
			for (float x = 0.1f; x < 1.0f; x += 0.1f) {
				path.moveTo(getDisplayX(x), getDisplayY(0.0f));
				path.lineTo(getDisplayX(x), getDisplayY(1.0f));
			}
			for (float y = 0.1f; y < 1.0f; y += 0.1f) {
				path.moveTo(getDisplayX(0.0f), getDisplayY(y));
				path.lineTo(getDisplayX(1.0f), getDisplayY(y));
			}
			g2d.draw(path);
		}
	}
	
	/**
	 * Renders the {@link Vertex}es of the {@link Graph}.
	 * @param g2d the {@link Graphics2D} reference
	 */
	protected void drawVertices(Graphics2D g2d) {
		if (graphPanel.getGraphToolbarPanel().isDisplayingVertices()) {
			Graph        graph      = graphPanel.getGraph();
			List<Vertex> vertexList = graph.getVertexList();
			
			Color color = graphPanel.getGraphToolbarPanel().isDisplayingDark() ? DARK_VERTEX_COLOR : LIGHT_VERTEX_COLOR;
			g2d.setColor(color);
			g2d.setStroke(SOLID_STROKE);
			
			for (Vertex vertex : vertexList) {
				ellipse.setFrame(getDisplayX(vertex) - 0.5f * POINT_RADIUS, getDisplayY(vertex) - 0.5f * POINT_RADIUS, POINT_RADIUS, POINT_RADIUS);
				g2d.fill(ellipse);
			}
		}
	}
	
	/**
	 * Renders the {@link ConvexHull} paths of the {@link Graph}.
	 * @param g2d the {@link Graphics2D} reference
	 */
	protected void drawConvexHulls(Graphics2D g2d) {
		if (graphPanel.getGraphToolbarPanel().isDisplayingConvexHulls()) {
			Graph graph = graphPanel.getGraph();
			Cycle cycle = graph.getCycle();
			
			Color color = graphPanel.getGraphToolbarPanel().isDisplayingDark() ? DARK_CONVEX_HULL_COLOR : LIGHT_CONVEX_HULL_COLOR;
			g2d.setColor(color);
			g2d.setStroke(DASH_STROKE);
			
			for (CycleLayer cycleLayer : cycle.getCycleLayerList()) {
				ConvexHull convexHull = cycleLayer.getConvexHull();
				
				path.reset();
				List<Vertex> cycleVertexList = convexHull.getVertexList();
				if (cycleVertexList.isEmpty()) {
					continue;
				}
				
				Vertex lastVertex = cycleVertexList.get(cycleVertexList.size() - 1);
				path.moveTo(getDisplayX(lastVertex), getDisplayY(lastVertex));
				for (Vertex vertex : cycleVertexList) {
					path.lineTo(getDisplayX(vertex), getDisplayY(vertex));
				}
				g2d.draw(path);
			}
		}
	}
	
	/**
	 * Renders the {@link Cycle} path of the {@link Graph}.
	 * @param g2d             the {@link Graphics2D} reference
	 * @param color           the cycle line color
	 * @param cycleVertexList the sorted list of cycle {@link Vertex}es
	 */
	protected void drawCycle(Graphics2D g2d, Color color, List<Vertex> cycleVertexList) {
		g2d.setColor(color);
		g2d.setStroke(SOLID_STROKE);
		
		path.reset();
		if (cycleVertexList.isEmpty()) {
			return;
		}
		
		Vertex lastVertex = cycleVertexList.get(cycleVertexList.size() - 1);
		path.moveTo(getDisplayX(lastVertex), getDisplayY(lastVertex));
		for (Vertex vertex : cycleVertexList) {
			path.lineTo(getDisplayX(vertex), getDisplayY(vertex));
		}
		g2d.draw(path);
	}
	
	/**
	 * Renders the {@link Cycle} path of the {@link Graph}.
	 * @param g2d the {@link Graphics2D} reference
	 */
	protected void drawCycle(Graphics2D g2d) {
		if (graphPanel.getGraphToolbarPanel().isDisplayingCycle()) {
			Color        color           = graphPanel.getGraphToolbarPanel().isDisplayingDark() ? DARK_CYCLE_COLOR : LIGHT_CYCLE_COLOR;
			Graph        graph           = graphPanel.getGraph();
			Cycle        cycle           = graph.getCycle();
			List<Vertex> cycleVertexList = cycle.getCycleVertexList();
			drawCycle(g2d, color, cycleVertexList);
		}
	}
	
	/**
	 * Renders the {@link Graph} cycle using the newer, simpler algorithm.
	 * @param g2d the {@link Graphics2D} reference
	 */
	protected void drawCycleWithNewAlgorithm(Graphics2D g2d) {
		if (graphPanel.getGraphToolbarPanel().isDisplayingNewCycle()) {
			Color        color           = graphPanel.getGraphToolbarPanel().isDisplayingDark() ? DARK_NEW_CYCLE_COLOR : LIGHT_NEW_CYCLE_COLOR;
			Graph        graph           = graphPanel.getGraph();
			List<Vertex> cycleVertexList = graph.getSortedVertexList();
			drawCycle(g2d, color, cycleVertexList);
		}
	}
	
	/**
	 * Renders the logical X/Y mouse coordinates captured over the plot area.
	 * @param g2d the {@link Graphics2D} reference
	 */
	protected void drawCoordinates(Graphics2D g2d) {
		if (graphPanel.getGraphToolbarPanel().isDisplayingCoordinates()) {
			Color color = graphPanel.getGraphToolbarPanel().isDisplayingDark() ? DARK_COORDINATE_COLOR : LIGHT_COORDINATE_COLOR;
			g2d.setColor(color);
			g2d.setFont(MOUSE_TEXT_FONT);
			g2d.drawString(String.format(MOUSE_TEXT_PATTERN, mouseX, mouseY), MOUSE_TEXT_OFFSET_X, MOUSE_TEXT_OFFSET_Y);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		try {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING,         RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,     RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			drawBackground(g2d);
			drawGrid(g2d);
			drawVertices(g2d);
			drawConvexHulls(g2d);
			drawCycle(g2d);
			drawCycleWithNewAlgorithm(g2d);
			drawCoordinates(g2d);
		}
		catch (Exception e) {
			e.printStackTrace();
			graphPanel.getGraph().printVertexList();
		}
	}
}
