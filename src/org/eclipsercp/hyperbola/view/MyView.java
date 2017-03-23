package org.eclipsercp.hyperbola.view;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class MyView extends ViewPart {

	public MyView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		final TreeViewer tv = new TreeViewer(parent);
		tv.getTree().setHeaderVisible(true);
        tv.getTree().setLinesVisible(true);
	    tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
	    tv.setContentProvider(new TreeContentProvider());
	    
	    TreeViewerColumn viewerColumn = new TreeViewerColumn(tv, SWT.NONE);
        viewerColumn.getColumn().setWidth(300);
        viewerColumn.getColumn().setText("Names");
        viewerColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
                new ViewLabelProvider(createImageDescriptor("icons/folder.png"), createImageDescriptor("icons/text_align_justify.png"))));

        GroupNode parentItem = new GroupNode("Simon Scholz Group", null, null);
        Set<INode> children = new HashSet<INode>();
        ElementNode childItem1 = new ElementNode("Lars Vogel", "Lars Vogel", parentItem);
        ElementNode childItem2 = new ElementNode("Dirk Fauth", "Dirk Fauth", parentItem);
        ElementNode childItem3 = new ElementNode("Wim Jongman", "Wim Jongman", parentItem);
        ElementNode childItem4 = new ElementNode("Tom Schindl", "Tom Schindl", parentItem);
        children.add(childItem1);
        children.add(childItem2);
        children.add(childItem3);
        children.add(childItem4);
        parentItem.setChildren(children);
        NodeController.getInstance().addItem(parentItem);
        //ItemController.getInstance().addItem(childItem1);
        //ItemController.getInstance().addItem(childItem2);
        //ItemController.getInstance().addItem(childItem3);
        //ItemController.getInstance().addItem(childItem4);
        tv.setInput(NodeController.getInstance().getItemList());
        tv.expandAll();

        GridLayoutFactory.fillDefaults().generateLayout(parent);
        
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private ImageDescriptor createImageDescriptor(String iconPath) {
		Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path(iconPath), null);
		return ImageDescriptor.createFromURL(url);
	}

}
