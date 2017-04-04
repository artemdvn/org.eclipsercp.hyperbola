package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.editor.NodeEditorInput;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.operation.DeleteNodeOperation;
import org.eclipsercp.hyperbola.service.NodeService;
import org.eclipsercp.hyperbola.view.MyView;

/**
 * A handler to implement delete node command.
 */
public class DeleteNodeHandler extends AbstractHandler {
	
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
			
			final IWorkbenchPart part = HandlerUtil.getActivePart(event);
			if (part instanceof MyView) {
				// Build the operation to be performed.
				DeleteNodeOperation op = new DeleteNodeOperation(page, (INode) obj);
				op.addContext(((MyView) part).getUndoContext());

				// The progress monitor so the operation can inform the user.
				IProgressMonitor monitor = ((MyView) part).getViewSite().getActionBars().getStatusLineManager()
						.getProgressMonitor();

				// An adapter for providing UI context to the operation.
				IAdaptable info = new IAdaptable() {
					public Object getAdapter(Class adapter) {
						if (Shell.class.equals(adapter))
							return part.getSite().getShell();
						return null;
					}
				};

				// Execute the operation.
				try {
					((MyView) part).getOperationHistory().execute(op, monitor, info);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}			
			
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
