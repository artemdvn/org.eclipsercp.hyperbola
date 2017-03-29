package org.eclipsercp.hyperbola.handler;

import java.util.ResourceBundle;

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
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.MessageBoxService;
import org.eclipsercp.hyperbola.service.NodeService;

/**
 * A handler to implement add new node command.
 */
public class AddNodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		// get the parent of a new node
		INode parentOfNewNode = NodeService.getInstance().getParentOfCurrentNode(event);
		
		ResourceBundle rb = ResourceBundle.getBundle("org.eclipsercp.hyperbola.service.menu");
		System.out.println(rb.getString("user"));

		InputDialog dlg = new InputDialog(window.getShell(), "Add new node", "Enter the title of a new node:", "",
				null);
		if (dlg.open() == Window.OK) {

			// check the title of a new node
			String titleOfNewNode = dlg.getValue();
			if (titleOfNewNode.length() == 0) {
				MessageBox dia = MessageBoxService.getInstance().getMessageBox(SWT.ICON_INFORMATION | SWT.OK,
						"Empty title", "The title of a new node cannot be empty!");
				dia.open();
				return null;
			}

			// create a new node
			ElementNode newNode = new ElementNode(NodeController.getMaxId(), titleOfNewNode, "", parentOfNewNode);
			if (parentOfNewNode != null) {
				parentOfNewNode.getChildren().add(newNode);
			} else {
				NodeController.getInstance().addNode(newNode);
			}

			// refresh node tree
			NodeService.getInstance().refreshTree(page, newNode);

		}
		return null;
	}

}
