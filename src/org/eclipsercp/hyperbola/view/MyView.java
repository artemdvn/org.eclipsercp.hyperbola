package org.eclipsercp.hyperbola.view;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;

public class MyView extends ViewPart {
	
	private TreeViewer tv;
	
	public MyView() {
		// TODO Auto-generated constructor stub
	}	

	public TreeViewer getTv() {
		return tv;
	}

	@Override
	public void createPartControl(Composite parent) {
		tv = new TreeViewer(parent);
		tv.getTree().setHeaderVisible(true);
        tv.getTree().setLinesVisible(true);
	    tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
	    tv.setContentProvider(new TreeContentProvider());
	    
	    TreeViewerColumn viewerColumn = new TreeViewerColumn(tv, SWT.NONE);
        viewerColumn.getColumn().setWidth(300);
        viewerColumn.getColumn().setText("Names");
        viewerColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new ViewLabelProvider()));

        //initial data
        GroupNode parentItem = new GroupNode(NodeController.getMaxId(), "Simon Scholz Group", null);
        ElementNode childItem1 = new ElementNode(NodeController.getMaxId(), "Lars Vogel", "Lars Vogel", parentItem);
        ElementNode childItem2 = new ElementNode(NodeController.getMaxId(), "Dirk Fauth", "Dirk Fauth", parentItem);
        ElementNode childItem3 = new ElementNode(NodeController.getMaxId(), "Wim Jongman", "Wim Jongman", parentItem);
        ElementNode childItem4 = new ElementNode(NodeController.getMaxId(), "Tom Schindl", "Tom Schindl", parentItem);
        parentItem.getChildren().add(childItem1);
        parentItem.getChildren().add(childItem2);
        parentItem.getChildren().add(childItem3);
        parentItem.getChildren().add(childItem4);
        NodeController.getInstance().addItem(parentItem);
        tv.setInput(NodeController.getInstance().getItemList());
        
		// Create a menu manager and create context menu
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(tv.getTree());
		// set the menu on the SWT widget
		tv.getTree().setMenu(menu);
		// register the menu with the framework
		getSite().registerContextMenu(menuManager, tv);

		// make the viewer selection available
		getSite().setSelectionProvider(tv);
		
		hookDoubleClickCommand();
        
        tv.expandAll();

        GridLayoutFactory.fillDefaults().generateLayout(parent);
        
	}
	
	private void hookDoubleClickCommand() {
		tv.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IHandlerService handlerService = getSite().getService(IHandlerService.class);
				try {
					handlerService.executeCommand("org.eclipsercp.hyperbola.openNodeEditor", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex.getMessage());
				}
			}
		});
	}

	@Override
	public void setFocus() {
		tv.getControl().setFocus();
	}
	
	public void dispose() {
		// important: We need do unregister our listener when the view is disposed
		//getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}
	
}
