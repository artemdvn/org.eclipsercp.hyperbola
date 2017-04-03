package org.eclipsercp.hyperbola.editor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

/**
 * A factory for re-creating editor inputs from a previously saved memento.
 */
public class NodeEditorFactory implements IElementFactory {
	public static final String ID = "org.eclipsercp.hyperbola.nodeEditorFactory";

	public IAdaptable createElement(IMemento memento) {
		String id = memento.getString("nodeId");
		String title = memento.getString("nodeTitle");
		String value = memento.getString("nodeValue");
		if (id != null) {
			return new NodeEditorInput(Integer.valueOf(id), title, value);
		}
		return null;
	}

}
