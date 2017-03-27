package org.eclipsercp.hyperbola.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;

public class NodeEditor extends EditorPart {
	
	public static final String ID = "org.eclipsercp.hyperbola.nodeEditor";
	private NodeEditorInput input;
	private INode node;
	private boolean isDirty = false;
	private Text text;
	private ModifyListener textModifyListener;

	public NodeEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (node instanceof GroupNode) {
			node.setTitle(text.getText());
		} else {
			node.setValue(text.getText());
		}
		setDirty(false);
	}

	@Override
	public void doSaveAs() {
		doSave(null);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof NodeEditorInput)) {
			throw new RuntimeException("Wrong input");
		}

		this.input = (NodeEditorInput) input;
		setSite(site);
		setInput(input);
		node = NodeController.getInstance().getNodeById(this.input.getId());
		setPartName(node.getTitle());
		
		
    
	}

	@Override
	public boolean isDirty() {
		return isDirty;
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
        
        text = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        text.setText(input.getValue());
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
		textModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		};
		text.addModifyListener(textModifyListener);

	}

	@Override
	public void setFocus() {
		text.setFocus();
	
	}
	
	private void setDirty(boolean value) {
        isDirty = value;
        firePropertyChange(PROP_DIRTY);
     }
	
	public void dispose() {
		//text.removeModifyListener(textModifyListener);
	}

}
