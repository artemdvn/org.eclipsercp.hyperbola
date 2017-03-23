package org.eclipsercp.hyperbola.view;

import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;

public class TreeContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ElementNode) {
			return false;
		} else if (element instanceof GroupNode) {
			Set<INode> children = ((GroupNode) element).getChildren();
			if (children == null) {
				return false;
			}
			return (children.size() > 0);
		}
		return false;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ElementNode) {
			return ((ElementNode) element).getParent();
		} else if (element instanceof GroupNode) {
			return ((GroupNode) element).getParent();
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ArrayContentProvider.getInstance().getElements(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ElementNode) {
			return new Object[0];
		} else if (parentElement instanceof GroupNode) {
			Set<INode> children = ((GroupNode) parentElement).getChildren();
			return children.toArray(new INode[children.size()]);
		}
		return new Object[0];
	}
}
