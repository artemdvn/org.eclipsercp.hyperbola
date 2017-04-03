package org.eclipsercp.hyperbola.editor;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorInputTransfer;

/**
 * Drop adapter, gets data from event and opens an editor.
 */
public class EditorAreaDropAdapter extends DropTargetAdapter {

	private IWorkbenchWindow window;

	public EditorAreaDropAdapter(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		event.detail = DND.DROP_COPY;
		super.dragEnter(event);
	}

	public void drop(DropTargetEvent event) {

		handleDrop(window.getActivePage(), event);

	}

	public void handleDrop(IWorkbenchPage page, DropTargetEvent event) {
		if (EditorInputTransfer.getInstance().isSupportedType(event.currentDataType)) {
			EditorInputTransfer.EditorInputData[] editorInputs = (EditorInputTransfer.EditorInputData[]) event.data;

			for (int i = 0; i < editorInputs.length; i++) {
				IEditorInput editorInput = editorInputs[i].input;

				try {
					window.getActivePage().openEditor(editorInput, NodeEditor.ID);
				} catch (PartInitException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}