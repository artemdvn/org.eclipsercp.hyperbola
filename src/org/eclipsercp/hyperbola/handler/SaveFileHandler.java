package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipsercp.hyperbola.service.JsonProvider;

/**
 * A handler to implement save to file command.
 */
public class SaveFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.json" });
		String fullName = dialog.open();

		if (fullName != null) {
			JsonProvider.saveTreeOfNodesToFile(fullName);
		}

		return null;
	}

}
