package org.eclipsercp.hyperbola;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipsercp.hyperbola.service.PropertyService;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private final String APP_WINDOW_TITLE = "APP_WINDOW_TITLE";

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(400, 300));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setTitle(PropertyService.getInstance().getPropertyValue(APP_WINDOW_TITLE));
	}

	@Override
	public void postWindowCreate() {
		Shell shell = getWindowConfigurer().getWindow().getShell();

		// set initial location at the center of the display
		shell.setLocation(shell.getDisplay().getPrimaryMonitor().getBounds().width / 2,
				shell.getDisplay().getPrimaryMonitor().getBounds().height / 2);
		super.postWindowCreate();
	}
}
