package com.bryanww.cycle.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

/**
 * Panel containing manipulation functions for the {@link Graph} and its
 * {@link Cycle}.  Also contains functions to manipulate the plot area rendered
 * by the {@link GraphPlotPanel}.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class GraphToolbarPanel extends JPanel {
	
	private static final long serialVersionUID = -6977034904497542618L;
	
	public static final int       DEFAULT_VERTEX_COUNT                  = 30;
	public static final int       MAX_VERTEX_COUNT                      = 1000;
	
	public static final Dimension SPACING_SIZE                          = new Dimension(4, 4);
	public static final int       TOOLBAR_HEIGHT                        = 44;
	public static final int       VERTEX_COUNT_SPINNER_WIDTH            = 40;
	public static final int       RESET_VERTEX_LIST_BUTTON_WIDTH        = 40;
	public static final int       AUTO_RESET_VERTEX_LIST_BUTTON_WIDTH   = 40;
	public static final int       PRINT_VERTEX_LIST_BUTTON_WIDTH        = 40;
	public static final int       RESET_TIME_DELAY_FAST                 = 750;
	public static final int       RESET_TIME_DELAY_NORMAL               = 2000;
	public static final int       RESET_TIME_DELAY_SLOW                 = 5000;
	public static final int       PHIL_FISH_MODE_TIME_DELAY             = 128;
	
	public static final String    VERTEX_COUNT_LABEL_TEXT               = "Vertex Count:";
	public static final String    RESET_VERTEX_LIST_BUTTON_TEXT         = "<html><u>R</u>eset</html>";
	public static final String    AUTO_RESET_VERTEX_LIST_BUTTON_TEXT    = "<html><u>A</u>uto-Reset</html>";
	public static final String    AUTO_RESET_SPEED_LABEL_TEXT           = "Speed:";
	public static final String    AUTO_RESET_FAST_BUTTON_TEXT           = "fast";
	public static final String    AUTO_RESET_NORMAL_BUTTON_TEXT         = "normal";
	public static final String    AUTO_RESET_SLOW_BUTTON_TEXT           = "slow";
	public static final String    PRINT_VERTEX_BUTTON_TEXT              = "<html><u>P</u>rint</html>";
	public static final String    DISPLAY_CYCLE_CHECK_BOX_TEXT          = "Cycle";
	public static final String    DISPLAY_CONVEX_HULLS_CHECK_BOX_TEXT   = "Hulls";
	public static final String    DISPLAY_VERTICES_CHECK_BOX_TEXT       = "Vertices";
	public static final String    DISPLAY_GRID_CHECK_BOX_TEXT           = "Grid";
	public static final String    DISPLAY_COORDINATES_CHECK_BOX_TEXT    = "(X, Y)";
	public static final String    DISPLAY_DARK_CHECK_BOX_TEXT           = "0xC001";
	
	public static final String    RESET_VERTEX_LIST_BUTTON_TOOLTIP      = "(Alt+R) Resets the graph with the specified count of random vertices";
	public static final String    AUTO_RESET_VERTEX_LIST_BUTTON_TOOLTIP = "(Alt+A) Automatically resets the random vertices on a timer at the specified speed (click graph to stop)";
	public static final String    PRINT_VERTEX_BUTTON_TOOLTIP           = "(Alt+P) Prints the (X/Y) vertex coordinates to console";
	
	protected GraphPanel    graphPanel;                  // the reference to the parent {@link GraphPanel}
	
	protected JSpinner      vertexCountSpinner;          // the spinner to control the number of randomly generated vertices
	protected JButton       resetVertexListButton;       // the button to generate a new set of random vertices and render the Hamiltonian circuit
	protected JRadioButton  autoResetFastButton;         // the radio button to select the "fast" auto-reset speed (for testing many iterations)
	protected JRadioButton  autoResetNormalButton;       // the radio button to select the "normal" auto-reset speed (for testing many iterations)
	protected JRadioButton  autoResetSlowButton;         // the radio button to select the "slow" auto-reset speed (for testing many iterations)
	protected JButton       printVertexListButton;       // the button to print the list of vertex coordinates to console
	protected JToggleButton autoResetVertexListButton;   // the button to quickly automate the action of pressing the reset button (for many test iterations)
	protected JCheckBox     displayCycleCheckBox;        // the checkbox controlling whether or not the cycle is displayed
	protected JCheckBox     displayConvexHullsCheckBox;  // the checkbox controlling whether or not the convex hulls are displayed
	protected JCheckBox     displayVerticesCheckBox;     // the checkbox controlling whether or not the vertices displayed
	protected JCheckBox     displayGridCheckBox;         // the checkbox controlling whether or not the grid is displayed
	protected JCheckBox     displayCoordinatesCheckBox;  // the checkbox controlling whether or not the mouse coordinates are displayed
	protected JCheckBox     displayDarkCheckBox;         // the checkbox controlling whether the colors are light or dark
	
	protected Timer         resetTimer;                  // the timer to automate resetting the randomly generated vertices
	protected Timer         philFishModeTimer;           // the timer to execute Phil Fish Mode
	
	/**
	 * Creates a new {@link GraphToolbarPanel}.
	 * @param graphPanel the reference to the parent {@link GraphPanel}
	 */
	public GraphToolbarPanel(GraphPanel graphPanel) {
		this.graphPanel = graphPanel;
		initUIComponents();
		addUIListeners();
	}
	
	/**
	 * Returns the reference to the parent {@link GraphPanel}.
	 * @return the reference to the parent {@link GraphPanel}
	 */
	public GraphPanel getGraphPanel() {
		return graphPanel;
	}
	
	/**
	 * Initializes UI components.
	 */
	protected void initUIComponents() {
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		JLabel vertexCountLabel          = new JLabel(VERTEX_COUNT_LABEL_TEXT);
		vertexCountSpinner               = new JSpinner(new SpinnerNumberModel(DEFAULT_VERTEX_COUNT, 1, MAX_VERTEX_COUNT, 1));
		resetVertexListButton            = new JButton(RESET_VERTEX_LIST_BUTTON_TEXT);
		resetVertexListButton.setToolTipText(RESET_VERTEX_LIST_BUTTON_TOOLTIP);
		autoResetVertexListButton        = new JToggleButton(AUTO_RESET_VERTEX_LIST_BUTTON_TEXT, false);
		autoResetVertexListButton.setToolTipText(AUTO_RESET_VERTEX_LIST_BUTTON_TOOLTIP);
		JLabel autoResetSpeedLabel       = new JLabel(AUTO_RESET_SPEED_LABEL_TEXT);
		autoResetFastButton              = new JRadioButton(AUTO_RESET_FAST_BUTTON_TEXT,   false);
		autoResetNormalButton            = new JRadioButton(AUTO_RESET_NORMAL_BUTTON_TEXT, true);
		autoResetSlowButton              = new JRadioButton(AUTO_RESET_SLOW_BUTTON_TEXT,   false);
		ButtonGroup autoResetButtonGroup = new ButtonGroup();
		autoResetButtonGroup.add(autoResetFastButton);
		autoResetButtonGroup.add(autoResetNormalButton);
		autoResetButtonGroup.add(autoResetSlowButton);
		printVertexListButton            = new JButton(PRINT_VERTEX_BUTTON_TEXT);
		printVertexListButton.setToolTipText(PRINT_VERTEX_BUTTON_TOOLTIP);
		displayCycleCheckBox             = new JCheckBox(DISPLAY_CYCLE_CHECK_BOX_TEXT,        true);
		displayConvexHullsCheckBox       = new JCheckBox(DISPLAY_CONVEX_HULLS_CHECK_BOX_TEXT, true);
		displayVerticesCheckBox          = new JCheckBox(DISPLAY_VERTICES_CHECK_BOX_TEXT,     true);
		displayGridCheckBox              = new JCheckBox(DISPLAY_GRID_CHECK_BOX_TEXT,         true);
		displayCoordinatesCheckBox       = new JCheckBox(DISPLAY_COORDINATES_CHECK_BOX_TEXT,  true);
		displayDarkCheckBox              = new JCheckBox(DISPLAY_DARK_CHECK_BOX_TEXT,         true);
		
		vertexCountSpinner.setMaximumSize(new Dimension(VERTEX_COUNT_SPINNER_WIDTH, Integer.MAX_VALUE));
		resetVertexListButton.setMaximumSize(new Dimension(RESET_VERTEX_LIST_BUTTON_WIDTH, Integer.MAX_VALUE));
		autoResetVertexListButton.setMaximumSize(new Dimension(AUTO_RESET_VERTEX_LIST_BUTTON_WIDTH, Integer.MAX_VALUE));
		printVertexListButton.setMaximumSize(new Dimension(PRINT_VERTEX_LIST_BUTTON_WIDTH, Integer.MAX_VALUE));
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createRigidArea(SPACING_SIZE));
		add(vertexCountLabel);
		add(Box.createRigidArea(SPACING_SIZE));
		add(vertexCountSpinner);
		add(Box.createRigidArea(SPACING_SIZE));
		add(resetVertexListButton);
		add(Box.createRigidArea(SPACING_SIZE));
		add(Box.createRigidArea(SPACING_SIZE));
		add(Box.createRigidArea(SPACING_SIZE));
		add(Box.createRigidArea(SPACING_SIZE));
		add(autoResetVertexListButton);
		add(Box.createRigidArea(SPACING_SIZE));
		add(autoResetSpeedLabel);
		add(Box.createRigidArea(SPACING_SIZE));
		add(autoResetFastButton);
		add(Box.createRigidArea(SPACING_SIZE));
		add(autoResetNormalButton);
		add(Box.createRigidArea(SPACING_SIZE));
		add(autoResetSlowButton);
		add(Box.createRigidArea(SPACING_SIZE));
		add(printVertexListButton);
		add(Box.createHorizontalGlue());
		add(Box.createRigidArea(SPACING_SIZE));
		add(displayCycleCheckBox);
		add(Box.createRigidArea(SPACING_SIZE));
		add(displayConvexHullsCheckBox);
		add(Box.createRigidArea(SPACING_SIZE));
		add(displayVerticesCheckBox);
		add(Box.createRigidArea(SPACING_SIZE));
		add(displayGridCheckBox);
		add(Box.createRigidArea(SPACING_SIZE));
		add(displayCoordinatesCheckBox);
		add(Box.createRigidArea(SPACING_SIZE));
		add(displayDarkCheckBox);
		add(Box.createRigidArea(SPACING_SIZE));
	}
	
	/**
	 * Adds UI listeners.
	 */
	protected void addUIListeners() {
		
		ActionListener resetVertexListActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				resetVertexList();
				stopTimers();
			}
		};
		
		resetVertexListButton.addActionListener(resetVertexListActionListener);
		JSpinner.NumberEditor vertexCountEditor = (JSpinner.NumberEditor) vertexCountSpinner.getEditor();
		vertexCountEditor.getTextField().addActionListener(resetVertexListActionListener);
		
		autoResetVertexListButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (autoResetVertexListButton.isSelected()) {
					resetVertexList();
					resetTimer.start();
				}
				else {
					stopTimers();
				}
			}
		});
		
		ActionListener autoResetSpeedActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (autoResetSlowButton.isSelected()) {
					resetTimer.setDelay(RESET_TIME_DELAY_SLOW);
				}
				else if (autoResetNormalButton.isSelected()) {
					resetTimer.setDelay(RESET_TIME_DELAY_NORMAL);
				}
				else if (autoResetFastButton.isSelected()) {
					resetTimer.setDelay(RESET_TIME_DELAY_FAST);
				}
			}
		};
		
		autoResetFastButton.addActionListener(autoResetSpeedActionListener);
		autoResetNormalButton.addActionListener(autoResetSpeedActionListener);
		autoResetSlowButton.addActionListener(autoResetSpeedActionListener);
		
		printVertexListButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				graphPanel.getGraph().printVertexList();
			}
		});
		
		ItemListener toogleRepaintItemListener = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				graphPanel.getGraphPlotPanel().repaint();
			}
		};
		
		displayCycleCheckBox.addItemListener(toogleRepaintItemListener);
		displayConvexHullsCheckBox.addItemListener(toogleRepaintItemListener);
		displayVerticesCheckBox.addItemListener(toogleRepaintItemListener);
		displayGridCheckBox.addItemListener(toogleRepaintItemListener);
		displayCoordinatesCheckBox.addItemListener(toogleRepaintItemListener);
		displayDarkCheckBox.addItemListener(toogleRepaintItemListener);
		
		resetTimer = new Timer(RESET_TIME_DELAY_NORMAL, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				resetVertexList();
			}
		});
		
		philFishModeTimer = new Timer(PHIL_FISH_MODE_TIME_DELAY, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				displayDarkCheckBox.setSelected(!displayDarkCheckBox.isSelected());
				graphPanel.getGraphPlotPanel().repaint();
			}
		});
	}
	
	/**
	 * Resets the {@link Vertex}es of the {@link Graph} with a random number of vertices specified by the spinner.
	 */
	protected void resetVertexList() {
		try {
			Number value       = (Number) vertexCountSpinner.getValue();
			int    vertexCount = value.intValue();
			graphPanel.getGraph().setRandomVertexList(vertexCount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		graphPanel.getGraphPlotPanel().repaint();
	}
	
	/**
	 * Returns {@code true} if the cycle is displayed.
	 * @return {@code true} if the cycle is displayed
	 */
	public boolean isDisplayingCycle() {
		return displayCycleCheckBox.isSelected();
	}
	
	/**
	 * Returns {@code true} if the convex hulls are displayed.
	 * @return {@code true} if the convex hulls are displayed
	 */
	public boolean isDisplayingConvexHulls() {
		return displayConvexHullsCheckBox.isSelected();
	}
	
	/**
	 * Returns {@code true} if the vertices are displayed.
	 * @return {@code true} if the vertices are displayed
	 */
	public boolean isDisplayingVertices() {
		return displayVerticesCheckBox.isSelected();
	}
	
	/**
	 * Returns {@code true} if the grid is displayed.
	 * @return {@code true} if the grid is displayed
	 */
	public boolean isDisplayingGrid() {
		return displayGridCheckBox.isSelected();
	}
	
	/**
	 * Returns {@code true} if the mouse coordinates are displayed.
	 * @return {@code true} if the mouse coordinates are displayed
	 */
	public boolean isDisplayingCoordinates() {
		return displayCoordinatesCheckBox.isSelected();
	}
	
	/**
	 * Returns {@code true} if the colors are dark, {@code false} if the colors are light.
	 * @return {@code true} if the colors are dark, {@code false} if the colors are light
	 */
	public boolean isDisplayingDark() {
		return displayDarkCheckBox.isSelected();
	}
	
	/**
	 * Stops the control timers.
	 */
	public void stopTimers() {
		resetTimer.stop();
		philFishModeTimer.stop();
		autoResetVertexListButton.setSelected(false);
	}
	
	/**
	 * Starts Phil Fish mode.
	 */
	public void startPhilFishModeTimer() {
		philFishModeTimer.start();
	}
	
	/**
	 * Stops Phil Fish mode.
	 */
	public void stopPhilFishModeTimer() {
		philFishModeTimer.stop();
	}
	
	/**
	 * Performs a "doClick" of the reset button.
	 */
	public void doClickResetVertexListButton() {
		resetVertexListButton.doClick();
	}
	
	/**
	 * Performs a "doClick" of the auto-reset button.
	 */
	public void doClickAutoResetVertexListButton() {
		autoResetVertexListButton.doClick();
	}
	
	/**
	 * Performs a "doClick" of the print button.
	 */
	public void doClickPrintVertexListButton() {
		printVertexListButton.doClick();
	}
}
