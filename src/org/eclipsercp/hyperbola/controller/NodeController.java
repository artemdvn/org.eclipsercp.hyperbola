package org.eclipsercp.hyperbola.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipsercp.hyperbola.model.INode;

/**
 * A service for performing CRUD operations with tree items.
 */
public class NodeController {

	private static NodeController instance;
	private List<INode> nodeList = new ArrayList<INode>();

	/**
	 * Constructs a new instance of this class or return existing instance if it
	 * have been already instantiated.
	 */
	public static NodeController getInstance() {
		if (instance == null) {
			instance = new NodeController();
		}
		return instance;
	}

	public List<INode> getItemList() {
		return nodeList;
	}

	public void setItemList(List<INode> itemList) {
		this.nodeList = itemList;
	}

	public boolean hasChildren(INode item) {
		
		if (item.getChildren() == null) {
			return false;
		}
		return (item.getChildren().size() != 0);
	}

	public INode getParent(INode item) {
		return item.getParent();
	}

	public INode[] getChildren(INode item) {
		return item.getChildren().toArray(new INode[item.getChildren().size()]);
	}

	public INode getNodeById(int id) {
		for (INode node : nodeList) {
			INode child = findNodeById(node, id);
			if (child != null) {
				return child;
			}
		}
		return null;
	}

	private INode findNodeById(INode parent, int id) {
		if (parent.getId() == id) {
			return parent;
		}
		for (INode child : parent.getChildren()) {
			INode foundNode = findNodeById(child, id);
			if (foundNode != null) {
				return foundNode;
			}
		}
		return null;
	}
	
	public void addItem(INode node) {
		nodeList.add(node);
	}
	
	public void deleteItem(INode node) {
		INode parent = node.getParent();
		if (parent != null) {
			parent.getChildren().remove(node);
		}
		nodeList.remove(node);
	}

}
