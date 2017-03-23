package org.eclipsercp.hyperbola.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipsercp.hyperbola.model.INode;

/**
 * A service for performing CRUD operations with tree items.
 */
public class NodeController {

	private static NodeController instance;
	private List<INode> itemList = new ArrayList<INode>();

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
		return itemList;
	}

	public void setItemList(List<INode> itemList) {
		this.itemList = itemList;
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

	public void addItem(INode item) {
		itemList.add(item);
	}

}
