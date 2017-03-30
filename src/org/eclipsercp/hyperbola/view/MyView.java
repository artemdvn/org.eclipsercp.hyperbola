package org.eclipsercp.hyperbola.view;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
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
		tv.setInput(NodeController.getInstance().getNodeList());

		// Create a menu manager and create context menu
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(tv.getTree());
		// set the menu on the SWT widget
		tv.getTree().setMenu(menu);
		// register the menu with the framework
		getSite().registerContextMenu(menuManager, tv);
		// make the viewer selection available
		getSite().setSelectionProvider(tv);

		implementDragAndDrop();

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

	private void implementDragAndDrop() {
		// Allow data to be copied or moved from the drag source
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DragSource source = new DragSource(tv.getTree(), operations);

		// Provide data in Text format
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				// Only start the drag if there is tree node selected.
				//if (tv.getTree().getSelection().length == 0) {
				//	event.doit = false;
				//}
			}

			public void dragSetData(DragSourceEvent event) {
				// Provide the data of the requested type.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					IStructuredSelection selection = tv.getStructuredSelection();
					INode firstElement = (INode) selection.getFirstElement();
					event.data = String.valueOf(firstElement.getId());
				}
			}

			public void dragFinished(DragSourceEvent event) {
				// If a move operation has been performed, remove the data
				// from the source
				//if (event.detail == DND.DROP_MOVE) {}
				// tv.getTree().setText("");

			}
		});
		
		
		
		// Allow data to be copied or moved to the drop target
		DropTarget target = new DropTarget(tv.getTree(), operations);

		// Receive data in Text or File format
		final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		types = new Transfer[] { fileTransfer, textTransfer };
		target.setTransfer(types);

		target.addDropListener(new DropTargetListener() {
			public void dragEnter(DropTargetEvent event) {
				//event.detail = DND.DROP_COPY;
//				if (event.detail == DND.DROP_DEFAULT) {
//					if ((event.operations & DND.DROP_COPY) != 0) {
//						event.detail = DND.DROP_COPY;
//					} else {
//						event.detail = DND.DROP_NONE;
//					}
//				}
//				// will accept text but prefer to have files dropped
//				for (int i = 0; i < event.dataTypes.length; i++) {
//					if (fileTransfer.isSupportedType(event.dataTypes[i])) {
//						event.currentDataType = event.dataTypes[i];
//						// files should only be copied
//						if (event.detail != DND.DROP_COPY) {
//							event.detail = DND.DROP_NONE;
//						}
//						break;
//					}
//				}
			}

			public void dragOver(DropTargetEvent event) {
//				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
//				if (textTransfer.isSupportedType(event.currentDataType)) {
//					// NOTE: on unsupported platforms this will return null
//					Object o = textTransfer.nativeToJava(event.currentDataType);
//					String t = (String) o;
//					if (t != null)
//						System.out.println(t);
//				}
			}

			public void dragOperationChanged(DropTargetEvent event) {
//				if (event.detail == DND.DROP_DEFAULT) {
//					if ((event.operations & DND.DROP_COPY) != 0) {
//						event.detail = DND.DROP_COPY;
//					} else {
//						event.detail = DND.DROP_NONE;
//					}
//				}
//				// allow text to be moved but files should only be copied
//				if (fileTransfer.isSupportedType(event.currentDataType)) {
//					if (event.detail != DND.DROP_COPY) {
//						event.detail = DND.DROP_NONE;
//					}
//				}
			}

			public void dragLeave(DropTargetEvent event) {
			}

			public void dropAccept(DropTargetEvent event) {
			}

			public void drop(DropTargetEvent event) {
				if (textTransfer.isSupportedType(event.currentDataType)) {
					int nodeId = Integer.parseInt((String) event.data);
					INode draggedNode = NodeController.getInstance().getNodeById(nodeId);
					System.out.println(draggedNode.getTitle());
					// TableItem item = new TableItem(dropTable, SWT.NONE);
					// item.setText(text);
				}
				if (fileTransfer.isSupportedType(event.currentDataType)) {
					String[] files = (String[]) event.data;
					// for (int i = 0; i < files.length; i++) {
					// TableItem item = new TableItem(dropTable, SWT.NONE);
					// item.setText(files[i]);
					// }
				}
			}
		});
		
	}
	

}
