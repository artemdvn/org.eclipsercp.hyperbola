package org.eclipsercp.hyperbola.operation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.editor.NodeEditorInput;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.NodeService;

/**
 * An undoable operation to implement add node command.
 */
public class AddNodeOperation extends AbstractOperation {

	private IWorkbenchPage page;
	private INode parentOfNewNode;
	private String titleOfNewNode;
	private boolean isGroup;

	private List<INode> undoList = new LinkedList<INode>();
	private List<INode> redoList = new LinkedList<INode>();

	public AddNodeOperation(IWorkbenchPage page, INode parentOfNewNode, String titleOfNewNode, boolean isGroup) {
		super("Add node");
		this.page = page;
		this.parentOfNewNode = parentOfNewNode;
		this.titleOfNewNode = titleOfNewNode;
		this.isGroup = isGroup;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (isGroup) {
			// create a new group
			GroupNode newGroup = new GroupNode(NodeController.getMaxId(), titleOfNewNode, parentOfNewNode);
			if (parentOfNewNode != null) {
				parentOfNewNode.getChildren().add(newGroup);
			} else {
				NodeController.getInstance().addNode(newGroup);
			}

			// add a new group to the tree
			NodeService.getInstance().addNewNodeToTree(page, newGroup);

			// save node in undo list
			undoList.add(newGroup);
			if (redoList.size() > 0) {
				redoList.remove(redoList.size() - 1);
			}

		} else {
			// create a new node
			ElementNode newNode = new ElementNode(NodeController.getMaxId(), titleOfNewNode, "", parentOfNewNode);
			if (parentOfNewNode != null) {
				parentOfNewNode.getChildren().add(newNode);
			}

			// add a new node to the tree
			NodeService.getInstance().addNewNodeToTree(page, newNode);

			// save node in undo list
			undoList.add(newNode);
			if (redoList.size() > 0) {
				redoList.remove(redoList.size() - 1);
			}
		}

		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// save node in undo list
		if (redoList.size() > 0) {
			INode nodeToAdd = redoList.remove(redoList.size() - 1);
			undoList.add(nodeToAdd);

			if (nodeToAdd.getParent() != null) {
				nodeToAdd.getParent().getChildren().add(nodeToAdd);
			}

			// add a new node to the tree
			NodeService.getInstance().addNewNodeToTree(page, nodeToAdd);

			return Status.OK_STATUS;

		}

		return Status.CANCEL_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (undoList.size() > 0) {
			INode nodeToDelete = undoList.remove(undoList.size() - 1);
			redoList.add(nodeToDelete);

			return deleteNode(nodeToDelete);
		}

		return Status.CANCEL_STATUS;
	}

	private IStatus deleteNode(INode nodeToDelete) {
		// delete current node
		NodeController.getInstance().deleteNode(nodeToDelete);

		// close node editors
		page.closeEditor(page.findEditor(new NodeEditorInput(nodeToDelete.getId())), false);
		for (INode child : nodeToDelete.getChildren()) {
			page.closeEditor(page.findEditor(new NodeEditorInput(child.getId())), false);
		}

		// refresh node tree
		NodeService.getInstance().removeNodeFromTree(page, nodeToDelete);

		return Status.OK_STATUS;
	}

}
