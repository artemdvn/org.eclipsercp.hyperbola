package org.eclipsercp.hyperbola.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipsercp.hyperbola.editor.NodeEditor;
import org.eclipsercp.hyperbola.editor.NodeEditorInput;
import org.eclipsercp.hyperbola.model.INode;
import org.eclipsercp.hyperbola.service.NodeService;

/**
 * A handler to implement edit node command.
 */
public class NodeEditorHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the selection
		Object obj = NodeService.getInstance().getCurrentSelection(event);
		if (obj != null) {
			INode node = (INode) obj;
			NodeEditorInput input = new NodeEditorInput(node.getId());
			input.setTitle(node.getTitle());
			input.setValue(node.getValue());
			try {
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().openEditor(input, NodeEditor.ID);
			} catch (PartInitException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

}
