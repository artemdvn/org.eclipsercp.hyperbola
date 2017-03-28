package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.editor.NodeEditorInput;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.MessageBoxService;
import org.eclipsercp.hyperbola.view.MyView;

public class DeleteNodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		// get the selection
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection != null && selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			// if we had a selection lets delete it
			if (obj != null) {

				MessageBox messageBox = MessageBoxService.getInstance().getMessageBox(
						SWT.ICON_QUESTION | SWT.YES | SWT.NO, "Delete node",
						"Do you really want to delete current node?");
				int response = messageBox.open();
				if (response == SWT.YES) {
					INode node = (INode) obj;
					NodeController.getInstance().deleteItem(node);
					
					// close node editors
					page.closeEditor(page.findEditor(new NodeEditorInput(node.getId())), false);
					for (INode child : node.getChildren()) {
						page.closeEditor(page.findEditor(new NodeEditorInput(child.getId())), false);
					}
					
					// refresh node tree
					for (IViewReference ref : page.getViewReferences()) {
						IViewPart view = ref.getView(false);
						if (view instanceof MyView) {
							TreeViewer tv = ((MyView) view).getTv();
							tv.setInput(NodeController.getInstance().getItemList());
							tv.refresh();
							tv.expandToLevel(node.getParent(), AbstractTreeViewer.ALL_LEVELS);
						}
					}
				}
			}
		}
		return null;
	}

}
