package org.eclipsercp.hyperbola.service;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

/**
 * A service for creating new message box dialogs to inform or warn the user.
 */
public class MessageBoxService {
	
	private static MessageBoxService instance;
	
	/**
	 * Constructs a new instance of this class or return existing instance if it have been already instantiated.
	 */
	public static synchronized MessageBoxService getInstance() {
		if (instance == null) {
			instance = new MessageBoxService();
		}
		return instance;
	}
	
	/**
	 * Constructs and returns new message box dialog.
	 * 
	 * @param style
	 *            the style of dialog to construct
	 * @param text
	 *            the dialog's title
	 * @param message
	 *            the dialog's message
	 */
	public MessageBox getMessageBox(int style, String text, String message) {
		MessageBox dia = new MessageBox(Display.getDefault().getActiveShell(), style);
		dia.setText(text);
		dia.setMessage(message);
		return dia;
	}

}
