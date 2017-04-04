package org.eclipsercp.hyperbola.operation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.editor.NodeEditorInput;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.MessageBoxService;
import org.eclipsercp.hyperbola.service.NodeService;
import org.eclipsercp.hyperbola.service.PropertyService;

public class DeleteNodeOperation extends AbstractOperation {

	private final String DELETE_NODE_TITLE = "DELETE_NODE_TITLE";
	private final String DELETE_NODE_MESSAGE = "DELETE_NODE_MESSAGE";
	
	private IWorkbenchPage page;
	private INode node;
	private List<INode> undoList = new LinkedList<INode>();
	private List<INode> redoList = new LinkedList<INode>();
	
	public DeleteNodeOperation(IWorkbenchPage page, INode node) {
		super("Delete node");
		this.page = page;
		this.node = node;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// If a UI context has been provided,
		// then prompt the user to confirm the operation.
		if (info != null) {
			Shell shell = (Shell) info.getAdapter(Shell.class);
			if (shell != null) {
				MessageBox messageBox = MessageBoxService.getInstance().getMessageBox(
						SWT.ICON_QUESTION | SWT.YES | SWT.NO,
						PropertyService.getInstance().getPropertyValue(DELETE_NODE_TITLE),
						PropertyService.getInstance().getPropertyValue(DELETE_NODE_MESSAGE));
				int response = messageBox.open();
				if (response != SWT.YES) {
					return Status.CANCEL_STATUS;
				}
			}
		}
		// Perform the operation.
		return redo(monitor, info);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// save node in undo list
		undoList.add(node);
		if (redoList.size() > 0) {
			redoList.remove(redoList.size() - 1);
		}
		
		// delete current node
		NodeController.getInstance().deleteNode(node);

		// close node editors
		closeOpenedEditorsOfDeletedNodes();

		// refresh node tree
		NodeService.getInstance().removeNodeFromTree(page, node);

		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (undoList.size() > 0) {
			INode nodeToAdd = undoList.remove(undoList.size() - 1);
			redoList.add(nodeToAdd);
			
			INode parentOfNodeToAdd = nodeToAdd.getParent();
			if (parentOfNodeToAdd != null) {
				parentOfNodeToAdd.getChildren().add(nodeToAdd);
			} else {
				NodeController.getInstance().addNode(nodeToAdd);
			}
			// add a new node to the tree
			NodeService.getInstance().addNewNodeToTree(page, nodeToAdd);
		}

		return Status.OK_STATUS;
	}

	private void closeOpenedEditorsOfDeletedNodes() {
		page.closeEditor(page.findEditor(new NodeEditorInput(node.getId())), false);
		for (INode child : node.getChildren()) {
			page.closeEditor(page.findEditor(new NodeEditorInput(child.getId())), false);
		}
	}

}
