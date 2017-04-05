package org.eclipsercp.hyperbola.operation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.NodeService;

/**
 * An undoable operation to implement drag and drop in the tree.
 */
public class DragAndDropNodeOperation extends AbstractOperation {

	private DropTargetEvent event;
	
	private List<DraggedNode> undoList = new LinkedList<DraggedNode>();
	private List<DraggedNode> redoList = new LinkedList<DraggedNode>();

	public DragAndDropNodeOperation(DropTargetEvent event) {
		super("Drag and Drop node");
		this.event = event;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (event.data == null) {
			return Status.CANCEL_STATUS;
		}

		int nodeId = Integer.parseInt((String) event.data);
		INode draggedNode = NodeController.getInstance().getNodeById(nodeId);
		INode oldParentOfDraggedNode = draggedNode.getParent();

		if (event.item == null) {
			return Status.CANCEL_STATUS;
		}

		INode newParentOfDraggedNode = (INode) event.item.getData();
		if (newParentOfDraggedNode instanceof ElementNode) {
			newParentOfDraggedNode = newParentOfDraggedNode.getParent();
		}

		if (!(newParentOfDraggedNode instanceof GroupNode)) {
			return Status.CANCEL_STATUS;
		}

		// can't move parent group to children group
		if (newParentOfDraggedNode.getParent() != null) {
			if (newParentOfDraggedNode.getParent().equals(draggedNode)) {
				return Status.CANCEL_STATUS;
			}
		}

		if (!newParentOfDraggedNode.equals(oldParentOfDraggedNode)) {
			oldParentOfDraggedNode.getChildren().remove(draggedNode);
			newParentOfDraggedNode.getChildren().add(draggedNode);
			draggedNode.setParent(newParentOfDraggedNode);
		}

		// refresh node tree
		NodeService.getInstance().refreshTreeAfterDragAndDrop(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
				oldParentOfDraggedNode, newParentOfDraggedNode);
		
		// save dragged node to undo list
		undoList.add(new DraggedNode(draggedNode, oldParentOfDraggedNode));
		if (redoList.size() > 0) {
			redoList.remove(redoList.size() - 1);
		}

		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// save node in undo list
		if (redoList.size() > 0) {
			DraggedNode draggedNode = redoList.remove(redoList.size() - 1);
						
			INode nodeToMove = draggedNode.getNode();
			INode oldParent = nodeToMove.getParent();
			INode newParent = draggedNode.getOldParent();

			if (newParent instanceof ElementNode) {
				newParent = newParent.getParent();
			}

			if (!(newParent instanceof GroupNode)) {
				return Status.CANCEL_STATUS;
			}

			// can't move parent group to children group
			if (newParent.getParent() != null) {
				if (newParent.getParent().equals(newParent)) {
					return Status.CANCEL_STATUS;
				}
			}

			if (!newParent.equals(oldParent)) {
				oldParent.getChildren().remove(nodeToMove);
				newParent.getChildren().add(nodeToMove);
				nodeToMove.setParent(newParent);
			}

			// refresh node tree
			NodeService.getInstance().refreshTreeAfterDragAndDrop(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), oldParent, newParent);
			
			draggedNode.setOldParent(oldParent);
			undoList.add(draggedNode);

			return Status.OK_STATUS;

		}

		return Status.CANCEL_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (undoList.size() > 0) {
			DraggedNode draggedNode = undoList.remove(undoList.size() - 1);
			
			INode nodeToMove = draggedNode.getNode();
			INode oldParent = nodeToMove.getParent();
			INode newParent = draggedNode.getOldParent();

			if (newParent instanceof ElementNode) {
				newParent = newParent.getParent();
			}

			if (!(newParent instanceof GroupNode)) {
				return Status.CANCEL_STATUS;
			}

			// can't move parent group to children group
			if (newParent.getParent() != null) {
				if (newParent.getParent().equals(newParent)) {
					return Status.CANCEL_STATUS;
				}
			}

			if (!newParent.equals(oldParent)) {
				oldParent.getChildren().remove(nodeToMove);
				newParent.getChildren().add(nodeToMove);
				nodeToMove.setParent(newParent);
			}

			// refresh node tree
			NodeService.getInstance().refreshTreeAfterDragAndDrop(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), oldParent, newParent);
			
			draggedNode.setOldParent(oldParent);
			redoList.add(draggedNode);

			return Status.OK_STATUS;
		}

		return Status.CANCEL_STATUS;
	}

	class DraggedNode {
		private INode node;
		private INode oldParent;

		public DraggedNode(INode node, INode oldParent) {
			super();
			this.node = node;
			this.oldParent = oldParent;
		}

		public INode getNode() {
			return node;
		}

		public void setNode(INode node) {
			this.node = node;
		}

		public INode getOldParent() {
			return oldParent;
		}

		public void setOldParent(INode oldParent) {
			this.oldParent = oldParent;
		}		

	}

}
