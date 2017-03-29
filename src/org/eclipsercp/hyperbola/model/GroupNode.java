package org.eclipsercp.hyperbola.model;

import java.util.HashSet;
import java.util.Set;

/**
 * A class for tree group.
 */
public class GroupNode implements INode {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String title;
	private transient INode parent;
	private Set<INode> children = new HashSet<INode>();

	public GroupNode() {
	}

	public GroupNode(int id, String title, INode parent) {
		this.id = id;
		this.title = title;
		this.parent = parent;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getValue() {
		return getTitle();
	}

	@Override
	public void setValue(String value) {
	}

	@Override
	public INode getParent() {
		return parent;
	}

	@Override
	public void setParent(INode parent) {
		this.parent = parent;
	}

	@Override
	public Set<INode> getChildren() {
		return children;
	}

	@Override
	public void setChildren(Set<INode> children) {
		this.children = children;
	}

	@Override
	public boolean isGroup() {
		return true;
	}

	@Override
	public String toString() {
		return title;
	}

}
