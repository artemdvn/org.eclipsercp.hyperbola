package org.eclipsercp.hyperbola.view;

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

public class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

	private ImageDescriptor groupImage;
	private ImageDescriptor elementImage;
	private ResourceManager resourceManager;

	public ViewLabelProvider(ImageDescriptor groupImage, ImageDescriptor elementImage) {
		this.groupImage = groupImage;
		this.elementImage = elementImage;
	}

	@Override
	public StyledString getStyledText(Object element) {
		StyledString styledString = new StyledString(((INode) element).getTitle());
		return styledString;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof GroupNode) {
			return getResourceManager().createImage(groupImage);
		} else if (element instanceof ElementNode) {
			return getResourceManager().createImage(elementImage);
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

}
