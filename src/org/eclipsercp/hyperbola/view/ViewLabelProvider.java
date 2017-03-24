package org.eclipsercp.hyperbola.view;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipsercp.hyperbola.model.ElementNode;
import org.eclipsercp.hyperbola.model.GroupNode;
import org.eclipsercp.hyperbola.model.INode;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

	private ResourceManager resourceManager;

	public ViewLabelProvider() {
	}

	@Override
	public StyledString getStyledText(Object element) {
		StyledString styledString = new StyledString(((INode) element).getTitle());
		return styledString;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof GroupNode) {
			return getResourceManager().createImage(createImageDescriptor("icons/folder.png"));
		} else if (element instanceof ElementNode) {
			return getResourceManager().createImage(createImageDescriptor("icons/text_align_justify.png"));
		}

		return super.getImage(element);
	}

	@Override
	public void dispose() {
		// garbage collect system resources
		if (resourceManager != null) {
			resourceManager.dispose();
			resourceManager = null;
		}
	}

	protected ResourceManager getResourceManager() {
		if (resourceManager == null) {
			resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return resourceManager;
	}
	
	private ImageDescriptor createImageDescriptor(String iconPath) {
		Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path(iconPath), null);
		return ImageDescriptor.createFromURL(url);
	}

}
