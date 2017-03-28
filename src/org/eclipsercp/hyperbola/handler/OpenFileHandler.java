package org.eclipsercp.hyperbola.handler;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipsercp.hyperbola.model.INode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class OpenFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.json" });
		String selectedFile = dialog.open();
		if (selectedFile == null) {
			return null;
		}

		Gson gson = new GsonBuilder().registerTypeAdapter(INode.class, new InterfaceAdapter<INode>())
                .create();
		
		try (FileReader file = new FileReader(selectedFile)) {
			Type listOfTestObject = new TypeToken<List<INode>>() {
			}.getType();
			
			List<INode> itemList = (List<INode>) gson.fromJson(file, listOfTestObject);
			//NodeController.getInstance().setItemList(itemList);
			//viewer.setInput(personList);
			//viewer.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}	

}
