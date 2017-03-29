package org.eclipsercp.hyperbola.service;

import java.util.ResourceBundle;

/**
 * A service for creating new message box dialogs to inform or warn the user.
 */
public class PropertyService {

	private static PropertyService instance;

	/**
	 * Constructs a new instance of this class or return existing instance if it
	 * have been already instantiated.
	 */
	public static synchronized PropertyService getInstance() {
		if (instance == null) {
			instance = new PropertyService();
		}
		return instance;
	}

	/**
	 * Returns menu property value from resource bundle.
	 * 
	 * @param propertyName
	 *            the name of the property in .properties file
	 */
	public String getPropertyValue(String propertyName) {
		ResourceBundle rb = ResourceBundle.getBundle("org.eclipsercp.hyperbola.service.hyperbola");
		return rb.getString(propertyName);
	}

}
