package com.bryanww.cycle.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.bryanww.cycle.model.Graph;

/**
 * Panel containing the main {@link Graph} instance which represents the graph
 * and its vertices, the {@link GraphToolbarPanel} which contains graph
 * manipulation functions, and the {@link GraphPlotPanel} that contains the
 * graph view rendering operations.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class GraphPanel extends JPanel {
	
	private static final long serialVersionUID = -3723766239035464407L;
	
	protected Graph             graph;              // the main {@link Graph} instance encapsulating the graph state
	
	protected GraphToolbarPanel graphToolbarPanel;  // the {@link GraphToolbarPanel} that contains graph manipulation functions
	protected GraphPlotPanel    graphPlotPanel;     // the {@link GraphPlotPanel} that contains the graph view rendering operations
	
	/**
	 * Creates a new {@link GraphPanel}.
	 */
	public GraphPanel() {
		this.graph = new Graph();
		this.graph.setRandomVertexList(GraphToolbarPanel.DEFAULT_VERTEX_COUNT);
		initUIComponents();
		addUIListeners();
	}
	
	/**
	 * Returns the main {@link Graph} instance encapsulating the graph state.
	 * @return the main {@link Graph} instance encapsulating the graph state
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * Returns the {@link GraphToolbarPanel} that contains graph manipulation functions.
	 * @return the {@link GraphToolbarPanel} that contains graph manipulation functions
	 */
	public GraphToolbarPanel getGraphToolbarPanel() {
		return graphToolbarPanel;
	}
	
	/**
	 * Returns the {@link GraphPlotPanel} that contains the graph view rendering operations.
	 * @return the {@link GraphPlotPanel} that contains the graph view rendering operations
	 */
	public GraphPlotPanel getGraphPlotPanel() {
		return graphPlotPanel;
	}
	
	/**
	 * Initializes UI components.
	 */
	protected void initUIComponents() {
		graphToolbarPanel = new GraphToolbarPanel(this);
		graphPlotPanel    = new GraphPlotPanel(this);
		
		graphToolbarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, GraphToolbarPanel.TOOLBAR_HEIGHT));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(GraphToolbarPanel.SPACING_SIZE));
		add(graphToolbarPanel);
		add(Box.createRigidArea(GraphToolbarPanel.SPACING_SIZE));
		add(Box.createHorizontalGlue());
		add(graphPlotPanel);
	}
	
	/**
	 * Adds UI listeners.
	 */
	protected void addUIListeners() {
		
		graphPlotPanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent event) {
				graphPlotPanel.requestFocusInWindow();
				graphToolbarPanel.stopTimers();
			}
			
			@Override
			public void mouseExited(MouseEvent event) {
				graphToolbarPanel.stopPhilFishModeTimer();
			}
		});
		
		KeyStroke resetKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.ALT_DOWN_MASK);
		String    resetCommand   = "reset";
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(resetKeyStroke, resetCommand);
		getActionMap().put(resetCommand, new AbstractAction() {
			
			private static final long serialVersionUID = 8081981611867340274L;
			
			@Override
			public void actionPerformed(ActionEvent event) {
				graphToolbarPanel.doClickResetVertexListButton();
			}
		});
		
		KeyStroke autoResetKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_DOWN_MASK);
		String    autoResetCommand   = "auto_reset";
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(autoResetKeyStroke, autoResetCommand);
		getActionMap().put(autoResetCommand, new AbstractAction() {
			
			private static final long serialVersionUID = -5275211152791485346L;
			
			@Override
			public void actionPerformed(ActionEvent event) {
				graphToolbarPanel.doClickAutoResetVertexListButton();
			}
		});
		
		KeyStroke printKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.ALT_DOWN_MASK);
		String    printCommand   = "print";
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(printKeyStroke, printCommand);
		getActionMap().put(printCommand, new AbstractAction() {
			
			private static final long serialVersionUID = -1674503051195915465L;
			
			@Override
			public void actionPerformed(ActionEvent event) {
				graphToolbarPanel.doClickPrintVertexListButton();
			}
		});
		
		KeyStroke philFishKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK);
		String    philFishCommand   = "phil_fish";
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(philFishKeyStroke, philFishCommand);
		getActionMap().put(philFishCommand, new AbstractAction() {
			
			private static final long serialVersionUID = 1194278717419182399L;
			
			@Override
			public void actionPerformed(ActionEvent event) {
				graphToolbarPanel.startPhilFishModeTimer();
			}
		});
	}
}