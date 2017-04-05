package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.view.MyView;

public class RedoHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof MyView) {
			MyView view = (MyView) part;
			IOperationHistory history = view.getOperationHistory();
			IUndoContext context = view.getUndoContext();
			IUndoableOperation op = history.getRedoOperation(context);
			if (op != null) {
				history.redoOperation(op, null, null);
			}
		}
		return null;
	}

}
