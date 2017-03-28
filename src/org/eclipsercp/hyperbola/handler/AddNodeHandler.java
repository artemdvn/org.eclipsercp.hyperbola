package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.MessageBoxService;
import org.eclipsercp.hyperbola.view.MyView;

public class AddNodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		// get the selection
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection != null && selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			// if we had a selection lets add new node to the current group
			if (obj != null) {

				InputDialog dlg = new InputDialog(window.getShell(), "Add new node", "Enter the title of the new node:",
						"", null);
				if (dlg.open() == Window.OK) {
					
					String titleOfNewNode = dlg.getValue();					
					if (titleOfNewNode.length() == 0) {
						MessageBox dia = MessageBoxService.getInstance().getMessageBox(SWT.ICON_INFORMATION | SWT.OK, "Empty title",
								"The title of the new node cannot be empty!");
						dia.open();
						return null;
					}
					
					//get the parent of the new node
					INode parentOfNewNode = null;
					if (obj instanceof ElementNode) {
						parentOfNewNode = ((INode) obj).getParent();
					} else if (obj instanceof GroupNode) {
						parentOfNewNode = ((INode) obj);
					}
					
					ElementNode newNode = new ElementNode(NodeController.getMaxId(), titleOfNewNode, "", parentOfNewNode);
					if (parentOfNewNode != null) {
						parentOfNewNode.getChildren().add(newNode);
					} else {
						NodeController.getInstance().addItem(newNode);
					}

					// refresh node tree
					for (IViewReference ref : page.getViewReferences()) {
						IViewPart view = ref.getView(false);
						if (view instanceof MyView) {
							TreeViewer tv = ((MyView) view).getTv();
							tv.setInput(NodeController.getInstance().getItemList());
							tv.refresh();
							tv.expandToLevel(newNode.getParent(), AbstractTreeViewer.ALL_LEVELS);
						}
					}
				}
			}
		}
		return null;
	}

}
