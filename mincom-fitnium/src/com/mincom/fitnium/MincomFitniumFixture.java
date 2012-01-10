package com.mincom.fitnium;

import com.magneticreason.fitnium.api.FitniumVariableAPI;
import com.mincom.ellipse.rc.apiv2.Application;

public class MincomFitniumFixture extends BaseMincomFitniumFixture {
	
	private Application application;
	
	public MincomFitniumFixture() {
		super();
	}
	
	public void loadApplication(String app) {
		this.application = mfuiv2.loadApp(app).waitForReady();
	}

	public void setWidgetWithValue(String id, String value) {
		this.application = application.setWidgetValue(id, value);
	}

	public String getWidgetWithValue(String widgetName) {
		return application.getWidget(widgetName).getValue();
	}
	public void selectTheTab(String tabname) {
		this.application = application.selectTab(tabname).waitForReady();
	}
	public void callAnAction(String action ) {
		this.application = application.toolbarAction(action);
	}
	
	public void waitForLoadedApp() {
		this.application = application.waitForLoadedApplication();
	}

	public void confirmAction(String action) {
		this.application = application.dialogButton(action);
	}
}
