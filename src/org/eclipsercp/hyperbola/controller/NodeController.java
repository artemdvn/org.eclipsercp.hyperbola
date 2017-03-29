package org.eclipsercp.hyperbola.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;

/**
 * A controller for performing CRUD operations with tree nodes.
 */
public class NodeController {

	private static NodeController instance;
	private List<GroupNode> nodeList = new ArrayList<GroupNode>();
	private static int maxId = 0;

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

	/**
	 * Increments and returns the max existing node id to use for creating a new
	 * node.
	 */
	public static int getMaxId() {
		return maxId++;
	}

	public List<GroupNode> getItemList() {
		return nodeList;
	}

	public void setItemList(List<GroupNode> itemList) {
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

	/**
	 * Adds new node to the list of nodes.
	 * 
	 * @param node
	 *            the node to add to the list
	 */
	public void addNode(GroupNode node) {
		nodeList.add(node);
	}

	/**
	 * Deletes existing node from the list of nodes.
	 * 
	 * @param node
	 *            the node to delete from the list
	 */
	public void deleteNode(INode node) {
		// first delete the node from the children of its parent
		INode parent = node.getParent();
		if (parent != null) {
			parent.getChildren().remove(node);
		}
		// then remove the node from the list
		nodeList.remove(node);
	}

}
