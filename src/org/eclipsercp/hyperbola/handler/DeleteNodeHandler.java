package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.editor.NodeEditorInput;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.MessageBoxService;
import org.eclipsercp.hyperbola.service.NodeService;
import org.eclipsercp.hyperbola.service.PropertyService;

/**
 * A handler to implement delete node command.
 */
public class DeleteNodeHandler extends AbstractHandler {
	
	private final String DELETE_NODE_TITLE = "DELETE_NODE_TITLE";
	private final String DELETE_NODE_MESSAGE = "DELETE_NODE_MESSAGE";

	private IWorkbenchPage page;
	private INode node;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		// get the selection
		Object obj = NodeService.getInstance().getCurrentSelection(event);
		// if we have a node selected lets delete it
		if (obj != null) {
			
			MessageBox messageBox = MessageBoxService.getInstance().getMessageBox(
					SWT.ICON_QUESTION | SWT.YES | SWT.NO,
					PropertyService.getInstance().getPropertyValue(DELETE_NODE_TITLE),
					PropertyService.getInstance().getPropertyValue(DELETE_NODE_MESSAGE));
			int response = messageBox.open();
			if (response != SWT.YES) {
				return Status.CANCEL_STATUS;
			}

			// delete current node
			node = (INode) obj;
			NodeController.getInstance().deleteNode(node);

			// close node editors
			closeOpenedEditorsOfDeletedNodes();

			// refresh node tree
			NodeService.getInstance().removeNodeFromTree(page, node);
			
			/*final IEditorPart editor = HandlerUtil.getActiveEditor(event);
			if (editor instanceof NodeEditor) {
				// Build the operation to be performed.
				DeleteNodeOperation op = new DeleteNodeOperation(page, (INode) obj);
				op.addContext(((NodeEditor) editor).getUndoContext());

				// The progress monitor so the operation can inform the user.
				IProgressMonitor monitor = editor.getEditorSite().getActionBars().getStatusLineManager()
						.getProgressMonitor();

				// An adapter for providing UI context to the operation.
				IAdaptable info = new IAdaptable() {
					public Object getAdapter(Class adapter) {
						if (Shell.class.equals(adapter))
							return editor.getSite().getShell();
						return null;
					}
				};

				// Execute the operation.
				try {
					((NodeEditor) editor).getOperationHistory().execute(op, monitor, info);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}			*/
			
		}
		return null;
	}
	
	private void closeOpenedEditorsOfDeletedNodes() {
		page.closeEditor(page.findEditor(new NodeEditorInput(node.getId())), false);
		for (INode child : node.getChildren()) {
			page.closeEditor(page.findEditor(new NodeEditorInput(child.getId())), false);
		}
	}

}
