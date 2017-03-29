package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
			MessageBox messageBox = MessageBoxService.getInstance().getMessageBox(SWT.ICON_QUESTION | SWT.YES | SWT.NO,
					PropertyService.getInstance().getPropertyValue(DELETE_NODE_TITLE),
					PropertyService.getInstance().getPropertyValue(DELETE_NODE_MESSAGE));
			int response = messageBox.open();
			if (response == SWT.YES) {
				// delete current node
				node = (INode) obj;
				NodeController.getInstance().deleteNode(node);

				// close node editors
				closeOpenedEditorsOfDeletedNodes();

				// refresh node tree
				NodeService.getInstance().refreshTree(page, node);
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
