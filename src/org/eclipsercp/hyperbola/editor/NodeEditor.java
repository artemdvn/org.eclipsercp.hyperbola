package org.eclipsercp.hyperbola.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class NodeEditor extends EditorPart {
	
	public static final String ID = "org.eclipsercp.hyperbola.nodeEditor";
	private NodeEditorInput input;

	public NodeEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof NodeEditorInput)) {
			throw new RuntimeException("Wrong input");
		}

		this.input = (NodeEditorInput) input;
		setSite(site);
		setInput(input);

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        parent.setLayout(layout);
        Text text = new Text(parent, SWT.BORDER);
        text.setText(input.getName());
        text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
