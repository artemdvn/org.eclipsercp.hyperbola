package org.eclipsercp.hyperbola.model;

import java.util.Set;

public class GroupNode implements INode {

	private int id;
	private String title;
	private transient INode parent;
	private Set<INode> children;

	public GroupNode() {
	}

	public GroupNode(int id, String title, GroupNode parent, Set<INode> children) {
		this.id = id;
		this.title = title;
		this.parent = parent;
		this.children = children;
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
