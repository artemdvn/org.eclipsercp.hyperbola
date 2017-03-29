package org.eclipsercp.hyperbola.service;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.view.MyView;

/**
 * A service for creating new message box dialogs to inform or warn the user.
 */
public class NodeService {

	private static NodeService instance;

	/**
	 * Constructs a new instance of this class or return existing instance if it
	 * have been already instantiated.
	 */
	public static synchronized NodeService getInstance() {
		if (instance == null) {
			instance = new NodeService();
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
		for (IViewReference ref : page.getViewReferences()) {
			IViewPart view = ref.getView(false);
			if (view instanceof MyView) {
				TreeViewer tv = ((MyView) view).getTv();
				tv.setInput(NodeController.getInstance().getItemList());
				tv.refresh();
				if (node != null) {
					tv.expandToLevel(node.getParent(), AbstractTreeViewer.ALL_LEVELS);
				}
			}
		}
	}

	/**
	 * Returns current selection of the application or <code>null<code>.
	 * 
	 * @param event
	 *            an event containing all the information about the current
	 *            state of the application
	 */
	public Object getCurrentSelection(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection != null && selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).getFirstElement();
		}
		return null;
	}

	/**
	 * Returns the parent of the selected node or <code>null<code>.
	 * 
	 * @param event
	 *            an event containing all the information about the current
	 *            state of the application
	 */
	public INode getParentOfCurrentNode(ExecutionEvent event) {
		INode parentOfNewNode = null;
		// get the selection
		Object obj = getCurrentSelection(event);
		// if we had a selection lets add new node to the current group
		if (obj != null) {
			if (obj instanceof ElementNode) {
				parentOfNewNode = ((INode) obj).getParent();
			} else if (obj instanceof GroupNode) {
				parentOfNewNode = ((INode) obj);
			}
		}
		return parentOfNewNode;
	}

}
