package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.MessageBoxService;
import org.eclipsercp.hyperbola.service.NodeService;
import org.eclipsercp.hyperbola.service.PropertyService;

/**
 * A handler to implement add new group command.
 */
public class AddGroupHandler extends AbstractHandler {

	private final String ADD_GROUP_TITLE = "ADD_GROUP_TITLE";
	private final String ADD_GROUP_LABEL = "ADD_GROUP_LABEL";
	private final String ADD_GROUP_EMPTY_TITLE = "ADD_GROUP_EMPTY_TITLE";
	private final String ADD_GROUP_EMPTY_MESSAGE = "ADD_GROUP_EMPTY_MESSAGE";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		// get the parent of a new node
		INode parentOfNewNode = NodeService.getInstance().getParentOfCurrentNode(event);

		InputDialog dlg = new InputDialog(window.getShell(),
				PropertyService.getInstance().getPropertyValue(ADD_GROUP_TITLE),
				PropertyService.getInstance().getPropertyValue(ADD_GROUP_LABEL), "", null);
		if (dlg.open() == Window.OK) {

			String titleOfNewNode = dlg.getValue();
			if (titleOfNewNode.length() == 0) {
				MessageBox dia = MessageBoxService.getInstance().getMessageBox(SWT.ICON_INFORMATION | SWT.OK,
						PropertyService.getInstance().getPropertyValue(ADD_GROUP_EMPTY_TITLE),
						PropertyService.getInstance().getPropertyValue(ADD_GROUP_EMPTY_MESSAGE));
				dia.open();
				return null;
			}

			// create a new group
			GroupNode newGroup = new GroupNode(NodeController.getMaxId(), titleOfNewNode, parentOfNewNode);
			if (parentOfNewNode != null) {
				parentOfNewNode.getChildren().add(newGroup);
			} else {
				NodeController.getInstance().addNode(newGroup);
			}

			// refresh node tree
			NodeService.getInstance().refreshTree(page, newGroup);
		}
		return null;
	}

}
