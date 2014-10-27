package com.bryanww.cycle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.bryanww.cycle.view.GraphPanel;

/**
 * Main method class to launch an instance of the {@link GraphPanel} in a frame.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class Main {
	
	/**
	 * Main method.
	 * @param args main arguments (ignored)
	 */
	public static void main(String[] args) {
		
		GraphPanel            graphPanel            = new GraphPanel();
		
		JFrame                frame                 = new JFrame(graphPanel.getClass().getSimpleName());
		GraphicsEnvironment   graphicsEnvironment   = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice        graphicsDevice        = graphicsEnvironment.getDefaultScreenDevice();
		GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
		Dimension             screenSize            = Toolkit.getDefaultToolkit().getScreenSize();
		Insets                screenInsets          = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration); // insets from taskbars, etc.
		int                   width                 = screenSize.width - (Math.abs(screenInsets.left) + Math.abs(screenInsets.right));
		int                   height                = screenSize.height - (Math.abs(screenInsets.top) + Math.abs(screenInsets.bottom));
		
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(graphPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
