package org.eclipsercp.hyperbola.model;

import java.util.HashSet;
import java.util.Set;

public class ElementNode implements INode {

	private int id;
	private String title;
	private String value;
	private transient INode parent;
	
	public ElementNode() {
	}

	public ElementNode(int id, String title, String value, GroupNode parent) {
		this.id = id;
		this.title = title;
		this.value = value;
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
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
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
		return new HashSet<INode>();
	}

	@Override
	public void setChildren(Set<INode> children) {
	}
	
	@Override
	public boolean isGroup(){
		return false;
	}
	
	@Override
	public String toString() {
		return title;
	}

}
