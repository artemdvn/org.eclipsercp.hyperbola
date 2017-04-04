package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.operation.AddNodeOperation;
import org.eclipsercp.hyperbola.service.MessageBoxService;
import org.eclipsercp.hyperbola.service.NodeService;
import org.eclipsercp.hyperbola.service.PropertyService;
import org.eclipsercp.hyperbola.view.MyView;

/**
 * A handler to implement add new node command.
 */
public class AddNodeHandler extends AbstractHandler {

	private final String ADD_NODE_TITLE = "ADD_NODE_TITLE";
	private final String ADD_NODE_LABEL = "ADD_NODE_LABEL";
	private final String ADD_NODE_EMPTY_TITLE = "ADD_NODE_EMPTY_TITLE";
	private final String ADD_NODE_EMPTY_MESSAGE = "ADD_NODE_EMPTY_MESSAGE";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		// get the parent of a new node
		INode parentOfNewNode = NodeService.getInstance().getParentOfCurrentNode(event);

		InputDialog dlg = new InputDialog(window.getShell(),
				PropertyService.getInstance().getPropertyValue(ADD_NODE_TITLE),
				PropertyService.getInstance().getPropertyValue(ADD_NODE_LABEL), "", null);
		if (dlg.open() == Window.OK) {

			// check the title of a new node
			String titleOfNewNode = dlg.getValue();
			if (titleOfNewNode.length() == 0) {
				MessageBox dia = MessageBoxService.getInstance().getMessageBox(SWT.ICON_INFORMATION | SWT.OK,
						PropertyService.getInstance().getPropertyValue(ADD_NODE_EMPTY_TITLE),
						PropertyService.getInstance().getPropertyValue(ADD_NODE_EMPTY_MESSAGE));
				dia.open();
				return null;
			}

			final IWorkbenchPart part = HandlerUtil.getActivePart(event);
			if (part instanceof MyView) {
				// Build the operation to be performed.
				AddNodeOperation op = new AddNodeOperation(page, parentOfNewNode, titleOfNewNode, false);
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
