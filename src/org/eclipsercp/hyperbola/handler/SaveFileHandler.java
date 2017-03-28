package org.eclipsercp.hyperbola.handler;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipsercp.hyperbola.controller.NodeController;
import org.eclipsercp.hyperbola.model.INode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] {"*.json"});
		String fullName = dialog.open();
		
		if (fullName != null){
			
			try (FileWriter file = new FileWriter(fullName)) {
				Gson gson = new GsonBuilder().registerTypeAdapter(INode.class, new InterfaceAdapter<INode>())
		                .create();
				gson.toJson(NodeController.getInstance().getItemList(), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
