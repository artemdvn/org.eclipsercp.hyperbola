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
import org.eclipsercp.hyperbola.service.PropertyService;

public class MyView extends ViewPart {

	private final String NODE_EDITOR_OPEN_COMMAND = "NODE_EDITOR_OPEN_COMMAND";
	private TreeViewer tv;
	private IDoubleClickListener doubleClickListenerForTreeViewer = new IDoubleClickListener() {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			IHandlerService handlerService = getSite().getService(IHandlerService.class);
			try {
				handlerService.executeCommand(PropertyService.getInstance().getPropertyValue(NODE_EDITOR_OPEN_COMMAND),
						null);
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
	};

	public MyView() {
	}

	public TreeViewer getTv() {
		return tv;
	}

	@Override
	public void createPartControl(Composite parent) {
		// create tree viewer
		tv = new TreeViewer(parent);
		tv.getTree().setHeaderVisible(true);
		tv.getTree().setLinesVisible(true);
		tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tv.setContentProvider(new TreeContentProvider());

		// create a column for nodes
		TreeViewerColumn viewerColumn = new TreeViewerColumn(tv, SWT.NONE);
		viewerColumn.getColumn().setWidth(300);
		viewerColumn.getColumn().setText("Nodes");
		viewerColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new ViewLabelProvider()));

		// fill the tree with initial data
		GroupNode parentItem = new GroupNode(NodeController.getMaxId(), "Simon Scholz Group", null);
		ElementNode childItem1 = new ElementNode(NodeController.getMaxId(), "Lars Vogel", "Lars Vogel", parentItem);
		ElementNode childItem2 = new ElementNode(NodeController.getMaxId(), "Dirk Fauth", "Dirk Fauth", parentItem);
		ElementNode childItem3 = new ElementNode(NodeController.getMaxId(), "Wim Jongman", "Wim Jongman", parentItem);
		ElementNode childItem4 = new ElementNode(NodeController.getMaxId(), "Tom Schindl", "Tom Schindl", parentItem);
		parentItem.getChildren().add(childItem1);
		parentItem.getChildren().add(childItem2);
		parentItem.getChildren().add(childItem3);
		parentItem.getChildren().add(childItem4);
		NodeController.getInstance().addNode(parentItem);
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

		tv.addDoubleClickListener(doubleClickListenerForTreeViewer);
		tv.expandAll();

		GridLayoutFactory.fillDefaults().generateLayout(parent);

	}

	@Override
	public void setFocus() {
		tv.getControl().setFocus();
	}

	public void dispose() {
		tv.removeDoubleClickListener(doubleClickListenerForTreeViewer);
		super.dispose();
	}

}
