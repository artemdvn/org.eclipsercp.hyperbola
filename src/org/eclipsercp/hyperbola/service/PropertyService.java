package org.eclipsercp.hyperbola.service;

import java.util.ResourceBundle;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipsercp.hyperbola.model.INode;

/**
 * A service for creating new message box dialogs to inform or warn the user.
 */
public class PropertyService {

	private static PropertyService instance;

	/**
	 * Constructs a new instance of this class or return existing instance if it
	 * have been already instantiated.
	 */
	public static synchronized PropertyService getInstance() {
		if (instance == null) {
			instance = new PropertyService();
		}
		return instance;
	}

	/**
	 * Refreshes and expands tree.
	 * 
	 * @param page
	 *            the active page of the window
	 * @param node
	 *            the node to expand tree to its level
	 */
	public void refreshTree(IWorkbenchPage page, INode node) {
		ResourceBundle rb = ResourceBundle.getBundle("org.eclipsercp.hyperbola.service.menu");
		System.out.println(rb.getString("user"));
	}

}
