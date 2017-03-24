package org.eclipsercp.hyperbola.view;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;

public class MyView extends ViewPart {
	
	private TreeViewer tv;
	
	// the listener we register with the selection service
	private ISelectionChangedListener listener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// TODO Auto-generated method stub
			
		}
	};

	public MyView() {
		// TODO Auto-generated constructor stub
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

        GroupNode parentItem = new GroupNode(1, "Simon Scholz Group", null, null);
        Set<INode> children = new HashSet<INode>();
        ElementNode childItem1 = new ElementNode(2, "Lars Vogel", "Lars Vogel", parentItem);
        ElementNode childItem2 = new ElementNode(3, "Dirk Fauth", "Dirk Fauth", parentItem);
        ElementNode childItem3 = new ElementNode(4, "Wim Jongman", "Wim Jongman", parentItem);
        ElementNode childItem4 = new ElementNode(5, "Tom Schindl", "Tom Schindl", parentItem);
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
        
		// Create a menu manager and create context menu
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(tv.getTree());
		// set the menu on the SWT widget
		tv.getTree().setMenu(menu);
		// register the menu with the framework
		getSite().registerContextMenu(menuManager, tv);

		// make the viewer selection available
		getSite().setSelectionProvider(tv);
		
		tv.addSelectionChangedListener(listener);
		
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
	
	/**
	 * Shows the given selection in this view.
	 */
	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
		setContentDescription("");
		if (selection instanceof ITreeSelection) {
			ITreeSelection ts = (ITreeSelection) selection;
			Object firstElement = ts.getFirstElement();
			if (firstElement instanceof GroupNode) {
				setContentDescription(((GroupNode) firstElement).getTitle());
			}
		}
	}
	
	public void dispose() {
		// important: We need do unregister our listener when the view is disposed
		//getSite().getWorkbenchWindow().getSelectionService().rremoveSelectionListener(listener);
		super.dispose();
	}
	
}
