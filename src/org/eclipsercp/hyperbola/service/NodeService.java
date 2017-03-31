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
 * A service for additional operations with tree nodes.
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
	 * Refreshes the whole tree and expands it to the particular node.
	 * 
	 * @param page
	 *            the active page of the window
	 * @param node
	 *            the node to expand tree to its level
	 */
	public void refreshTree(IWorkbenchPage page, INode node) {

		TreeViewer tv = getTreeViewerFromPage(page);
		if (tv != null) {
			tv.setInput(NodeController.getInstance().getNodeList());
			tv.refresh();
			if (node != null) {
				tv.expandToLevel(node.getParent(), AbstractTreeViewer.ALL_LEVELS);
			}
		}
	}
	
	/**
	 * Refreshes only old parent's and new parent's parts of the tree after drag
	 * and drop.
	 * 
	 * @param page
	 *            the active page of the window
	 * @param oldParent
	 *            the old parent of the dragged node
	 * @param newParent
	 *            the new parent of the dragged node
	 */
	public void refreshTreeAfterDragAndDrop(IWorkbenchPage page, INode oldParent, INode newParent) {

		TreeViewer tv = getTreeViewerFromPage(page);
		if (tv != null) {
			tv.refresh(oldParent);
			tv.refresh(newParent);
			if (newParent != null) {
				tv.expandToLevel(newParent, AbstractTreeViewer.ALL_LEVELS);
			}
		}
	}
	
	
	/**
	 * Adds to the viewer a new node without refreshing the whole tree.
	 * 
	 * @param page
	 *            the active page of the window
	 * @param node
	 *            a new node to add to the tree
	 */
	public void addNewNodeToTree(IWorkbenchPage page, INode node) {

		if (node == null) {
			return;
		}

		TreeViewer tv = getTreeViewerFromPage(page);
		if (tv != null) {

			if (node.getParent() == null) {
				tv.setInput(NodeController.getInstance().getNodeList());
				tv.refresh();
				tv.expandToLevel(node, AbstractTreeViewer.ALL_LEVELS);
			} else {
				tv.add(node.getParent(), node);
				tv.expandToLevel(node.getParent(), AbstractTreeViewer.ALL_LEVELS);
			}
		}
	}
	
	/**
	 * Removes from the viewer an existing node without refreshing the whole
	 * tree.
	 * 
	 * @param page
	 *            the active page of the window
	 * @param node
	 *            an existing node to remove from the tree
	 */
	public void removeNodeFromTree(IWorkbenchPage page, INode node) {

		if (node == null) {
			return;
		}

		TreeViewer tv = getTreeViewerFromPage(page);
		if (tv != null) {
			tv.remove(node);
			tv.expandToLevel(node.getParent(), AbstractTreeViewer.ALL_LEVELS);
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
	
	private TreeViewer getTreeViewerFromPage(IWorkbenchPage page) {
		for (IViewReference ref : page.getViewReferences()) {
			IViewPart view = ref.getView(false);
			if (view instanceof MyView) {
				return ((MyView) view).getTv();
			}
		}
		return null;
	}

}
