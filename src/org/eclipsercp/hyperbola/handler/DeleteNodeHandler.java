package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.operation.DeleteNodeOperation;
import org.eclipsercp.hyperbola.service.NodeService;
import org.eclipsercp.hyperbola.view.MyView;

/**
 * A handler to implement delete node command.
 */
public class DeleteNodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		// get the selection
		Object obj = NodeService.getInstance().getCurrentSelection(event);
		// if we have a node selected lets delete it
		if (obj != null) {

			final IWorkbenchPart part = HandlerUtil.getActivePart(event);
			if (part instanceof MyView) {
				// Build the operation to be performed.
				DeleteNodeOperation op = new DeleteNodeOperation(page, (INode) obj);
				op.addContext(((MyView) part).getUndoContext());

				// Execute the operation.
				try {
					((MyView) part).getOperationHistory().execute(op, null, null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

}
