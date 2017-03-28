package org.eclipsercp.hyperbola.model;

import java.io.Serializable;
import java.util.Set;

public interface INode extends Serializable {
	int getId();
	
	INode getParent();

	void setParent(INode parent);

	Set<INode> getChildren();

	void setChildren(Set<INode> children);

	String getValue();

	void setValue(String value);

	String getTitle();

	void setTitle(String title);

	boolean isGroup();
}
