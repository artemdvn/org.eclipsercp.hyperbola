package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.service.JsonProvider;
import org.eclipsercp.hyperbola.service.NodeService;

/**
 * A handler to implement open file command.
 */
public class OpenFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.json" });
		String selectedFile = dialog.open();
		if (selectedFile == null) {
			return null;
		}

		JsonProvider.loadTreeOfNodesFromFile(selectedFile);

		NodeService.getInstance().refreshTree(HandlerUtil.getActiveWorkbenchWindow(event).getActivePage(), null);

		return null;
	}

}
