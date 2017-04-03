package org.eclipsercp.hyperbola.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipsercp.hyperbola.service.PropertyService;

public class NodeEditorInput implements IEditorInput, IPersistableElement {

	private final String NODE_EDITOR_TOOLTIP = "NODE_EDITOR_TOOLTIP";
	private final int id;
	private String title;
	private String value;

	public NodeEditorInput(int id) {
		this.id = id;
	}
	
	public NodeEditorInput(int id, String title, String value) {
		this.id = id;
		this.title = title;
		this.value = value;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return String.valueOf(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return PropertyService.getInstance().getPropertyValue(NODE_EDITOR_TOOLTIP);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeEditorInput other = (NodeEditorInput) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public void saveState(IMemento memento) {
		memento.putString("nodeId", String.valueOf(getId()));
		memento.putString("nodeTitle", getTitle());	
		memento.putString("nodeValue", getValue());	
	}

	@Override
	public String getFactoryId() {
		return NodeEditorFactory.ID;
	}

}
