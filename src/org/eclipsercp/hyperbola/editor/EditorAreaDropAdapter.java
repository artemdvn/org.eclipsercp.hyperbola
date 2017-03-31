package org.eclipsercp.hyperbola.editor;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorInputTransfer;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.NodeService;

public class EditorAreaDropAdapter extends DropTargetAdapter {

	private IWorkbenchWindow window;

	public EditorAreaDropAdapter(IWorkbenchWindow window) {
		this.window = window;
	}
	
	public void drop(DropTargetEvent event) {
			
			if (event.data == null) {
				return;
			}
			
			int nodeId = Integer.parseInt((String) event.data);
			INode draggedNode = NodeController.getInstance().getNodeById(nodeId);
			INode oldParentOfDraggedNode = draggedNode.getParent();

			if (event.item == null) {
				return;
			}

			INode newParentOfDraggedNode = (INode) event.item.getData();
			if (newParentOfDraggedNode instanceof ElementNode) {
				newParentOfDraggedNode = newParentOfDraggedNode.getParent();
			}

			if (!(newParentOfDraggedNode instanceof GroupNode)) {
				return;
			}

			// can't move parent group to children group
			if (newParentOfDraggedNode.getParent() != null) {
				if (newParentOfDraggedNode.getParent().equals(draggedNode)) {
					return;
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

	}

	public void handleDrop(IWorkbenchPage page, DropTargetEvent event) {
		if (EditorInputTransfer.getInstance().isSupportedType(event.currentDataType)) {
			EditorInputTransfer.EditorInputData[] editorInputs = (EditorInputTransfer.EditorInputData[]) event.data;

			for (int i = 0; i < editorInputs.length; i++) {
				IEditorInput editorInput = editorInputs[i].input;
				String editorId = editorInputs[i].editorId;
				
				try {
					window.getActivePage().openEditor(editorInput, NodeEditor.ID);
				} catch (PartInitException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}